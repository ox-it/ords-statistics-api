/*
 * Copyright 2015 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ox.it.ords.api.statistics.services.impl.hibernate;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import uk.ac.ox.it.ords.api.statistics.configuration.AuthenticationDetails;
import uk.ac.ox.it.ords.api.statistics.model.OrdsStatistics;
import uk.ac.ox.it.ords.api.statistics.services.MessagingService;
import uk.ac.ox.it.ords.api.statistics.services.ServerConfigurationService;
import uk.ac.ox.it.ords.api.statistics.services.StatisticsService;

public class StatisticsServiceImpl implements StatisticsService {

	Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public StatisticsServiceImpl(){
		setSessionFactory(HibernateUtils.getSessionFactory());
	}
	
	

	@Override
	public OrdsStatistics getStatistics() throws Exception {
		
		OrdsStatistics statistics  = null;
		Session session = sessionFactory.getCurrentSession();

		try {
			session.beginTransaction();
			statistics = (OrdsStatistics) session.createCriteria(OrdsStatistics.class)
					.addOrder(Order.desc("statsId"))
					.setFirstResult(0)
					.setMaxResults(1)
					.uniqueResult();

			session.getTransaction().commit();
		}
		catch (NonUniqueResultException n){
			log.error("Problem obtaining Statistics record; probably there are no records yet", n);	
			session.getTransaction().rollback();
		}
		catch (RuntimeException e) {
			log.error("Problem obtaining Statistics record", e);
			session.getTransaction().rollback();
		} 
		finally {
			HibernateUtils.closeSession();
		}

		return statistics;
		
	}
	
	protected void create(OrdsStatistics stats){
		Session session = sessionFactory.getCurrentSession();

		try {
			session.beginTransaction();
			session.save(stats);
			session.getTransaction().commit();
		}
		catch (RuntimeException e) {
			log.error("Problem creating Statistics record", e);
			session.getTransaction().rollback();
		} 
		finally {
			HibernateUtils.closeSession();
		}
	}

	/**
	 * Gather project-level statistics
	 * @param stats
	 */
	protected void getProjectStatistics(OrdsStatistics stats) {
        
		BigInteger count = null;
		Session session = sessionFactory.getCurrentSession();

		try {
			session.beginTransaction();
			
			count = (BigInteger) session.createSQLQuery("select count(*) from project where privateProject=false and deleted=false and trialproject=false").uniqueResult();
			stats.setNumberOfOpenProjects(count.intValue());
			
			count = (BigInteger) session.createSQLQuery("select count(*) from project where privateProject=true and deleted=false and trialproject=false").uniqueResult();
			stats.setNumberOfClosedProjects(count.intValue());
			
			count = (BigInteger) session.createSQLQuery("select count(*) from project where deleted=false and trialproject=false").uniqueResult();
			stats.setNumberOfFullProjects(count.intValue());
			
			count = (BigInteger) session.createSQLQuery("select count(*) from project where deleted=false and trialproject=true").uniqueResult();
			stats.setNumberOfTrialProjects(count.intValue());
			
			count = (BigInteger) session.createSQLQuery("select count(*) from project where deleted=false and age(datecreated) < '6 months'").uniqueResult();
			stats.setNumberOfProjectsInLastSixMonths(count.intValue());
			
			count = (BigInteger) session.createSQLQuery("select count(*) from project where deleted=false and age(datecreated) < '30 days'").uniqueResult();
			stats.setNumberOfRecentProjects(count.intValue());
			
			count = (BigInteger) session.createSQLQuery("select count(*) from project where deleted=false").uniqueResult();
			stats.setNumberOfProjectsManagedByOrds(count.intValue());
			
			session.getTransaction().commit();
		}
		catch (RuntimeException e) {
			log.error("Problem creating Statistics record", e);
			session.getTransaction().rollback();
		} 
		finally {
			HibernateUtils.closeSession();
		}
		
		
		
	}

    public void computeLatestStatistics() throws Exception {
        log.debug("computeLatestStats");

        int numberOfRecords = 0;

        // Let's now find a server to use
        for (String server : ServerConfigurationService.Factory.getInstance().getServers()) {
            numberOfRecords += getNumberOfRecordsForServer(server);
        }
        OrdsStatistics stats = new OrdsStatistics();
        stats.setNumberOfRecordsManagedByOrds(numberOfRecords);
        
        //
        // Gather project stats
        //
        getProjectStatistics(stats);
        create(stats);

        log.debug("computeLatestStats:return");
    }
    
    public int getNumberOfRecordsForServer(String server) throws Exception {
	   	Long numberOfRecords = 0L;
	
	   	//
	   	// Get database list for server
	   	//
	   	List<String> databases = getDatabases(server);
	   	
	   	//
	   	// Get row count for each database
	   	//
		for (String database : databases){
			numberOfRecords += getRecordCountForDatabase(server, database);
		}

		return numberOfRecords.intValue();
	}
	
	private int getRecordCountForDatabase(String server, String database) throws Exception{
		int numberOfRecords = 0;

		AuthenticationDetails ad = new AuthenticationDetails();
    	String url = "jdbc:postgresql://" + server + "/" + database;    	
    	
		Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", ad.getRootDbUser());
	    connectionProps.put("password", ad.getRootDbPassword());
	    try {
			conn = DriverManager.getConnection(url);
			//
			// This is the query used internally by PostgreSQL for the ANALYZE command. Its 
			// much more up to date than relying on PG_STAT, but also performs better than a
			// SELECT on rach table.
			//
			ResultSet rs = conn.prepareStatement("SELECT SUM(c.reltuples) FROM pg_class C LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace) WHERE nspname NOT IN ('pg_catalog', 'information_schema') AND relkind='r';").executeQuery();
			rs.next();
			numberOfRecords = rs.getInt(1);
			rs.close();
			conn.close();
		} finally {
            if (conn != null) conn.close();
        }
	    return numberOfRecords;
	}

	
	@SuppressWarnings("unchecked")
	private List<String> getDatabases(String server) {
		List<String> databases = null;
		Session session = sessionFactory.getCurrentSession();
	
		try {
			session.beginTransaction();
			databases = session.createSQLQuery("SELECT datname FROM pg_database WHERE datistemplate = false AND datname <> 'postgres';").list();
			session.getTransaction().commit();
		}
		catch (Exception e) {
			log.error("Problem creating Statistics record", e);
			session.getTransaction().rollback();
		} 
		finally {
			HibernateUtils.closeSession();
		}
		return databases;
	}
	
    /* (non-Javadoc)
     * @see uk.ac.ox.it.ords.api.statistics.services.StatisticsService#generateAndSendStatsEmail()
     */
    public void generateAndSendStatsEmail() throws Exception {
    	log.debug("generateAndSendStatsEmail");
    	String messageToSend = generateStatsEmail();
    	MessagingService.Factory.getInstance().sendMessage(messageToSend);
    }
    
    protected String generateStatsEmail() throws Exception{
    	
    	String messageToSend = "Hi Ords Admin. Here are the current statistics:\n\n";
    	
    	OrdsStatistics stats = getStatistics();
    	
        messageToSend += String.format("There are %d projects defined.\n", stats.getNumberOfProjectsManagedByOrds());
        messageToSend += String.format("Number of open projects:%d, Number of closed projects: %d.\n", stats.getNumberOfOpenProjects(), stats.getNumberOfClosedProjects());
    	messageToSend += String.format("Number of full projects:%d, Number of trial projects: %d\n", stats.getNumberOfFullProjects(), stats.getNumberOfTrialProjects());
    	messageToSend += String.format("%d projects have been created in the last 30 days\n", stats.getNumberOfRecentProjects());
    	messageToSend += String.format("%d projects have been created in the last 6 months\n", stats.getNumberOfProjectsInLastSixMonths());
    	messageToSend += "\n";
    	messageToSend += "\n\nTTFN\n\nYour humble ORDS servant";
    	
    	return messageToSend;
    	
    }

}

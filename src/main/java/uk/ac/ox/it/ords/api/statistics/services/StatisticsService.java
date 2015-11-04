package uk.ac.ox.it.ords.api.statistics.services;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ServiceLoader;

import uk.ac.ox.it.ords.api.statistics.model.OrdsStatistics;
import uk.ac.ox.it.ords.api.statistics.services.impl.hibernate.StatisticsServiceImpl;

public interface StatisticsService {

	public OrdsStatistics getStatistics() throws Exception;
	
	public void generateAndSendStatsEmail() throws Exception;
	
	/**
     * Generate the latest statistics for ORDS. This is so that ORDS can display the number 
     * of Projects, databases, records, currently under management. Since processing for this 
     * can get quite intense, this function is designed to be run periodically (maybe once per
     * night). Statistics are written to the ORDS statistics table so that they may be queried
     * and trending applied if desired.
     * 
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws JAXBException
     */
	public void computeLatestStatistics() throws Exception;
	
	/**
	 * Factory for obtaining implementations
	 */
    public static class Factory {
		private static StatisticsService provider;
	    public static StatisticsService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	// Place a file called uk.ac.ox.oucs.ords.utilities.csv in src/main/resources/META-INF/services
	    	// containing the classname to load as the CsvService implementation. 
	    	// By default we load the Hibernate implementation.
	    	//
	    	if (provider == null){
	    		ServiceLoader<StatisticsService> ldr = ServiceLoader.load(StatisticsService.class);
	    		for (StatisticsService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new StatisticsServiceImpl();
	    	}
	    	
	    	return provider;
	    }
	}
}

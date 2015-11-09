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
package uk.ac.ox.it.ords.api.statistics.resources;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.local.LocalConduit;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import uk.ac.ox.it.ords.api.statistics.services.impl.hibernate.HibernateUtils;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class AbstractResourceTest {

	protected final static String ENDPOINT_ADDRESS = "local://audit-api";
	protected static Server server;
	protected static void startServer() throws Exception {

	}
	
	public WebClient getClient(){
		List<Object> providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());
		WebClient client = WebClient.create(ENDPOINT_ADDRESS, providers);
		client.type("application/json");
		client.accept("application/json");
		WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);
		return client;
	}
	


	/**
	 * Configure Shiro and start the server
	 * @throws Exception
	 */
	@BeforeClass
	public static void initialize() throws Exception {
		
		//
		// Delete existing statistics records
		//
		Session session  = HibernateUtils.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.createSQLQuery("truncate ordsstatistics").executeUpdate();
		session.getTransaction().commit();
		HibernateUtils.closeSession();
		
		//
		// Create an embedded server with JSON processing
		//
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		
		ArrayList<Object> providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());
		sf.setProviders(providers);
		
		//
		// Add our REST resources to the server
		//
		ArrayList<ResourceProvider> resources = new ArrayList<ResourceProvider>();
		resources.add(new SingletonResourceProvider(new Statistics(), true));
		sf.setResourceProviders(resources);
		
		//
		// Start the server at the endpoint
		//
		sf.setAddress(ENDPOINT_ADDRESS);
		server = sf.create(); 
		startServer();
	}

	@AfterClass
	public static void destroy() throws Exception {
		server.stop();
		server.destroy();
	}

}

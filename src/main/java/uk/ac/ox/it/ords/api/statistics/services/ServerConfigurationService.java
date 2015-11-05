package uk.ac.ox.it.ords.api.statistics.services;

import java.util.List;
import java.util.ServiceLoader;

import uk.ac.ox.it.ords.api.statistics.services.impl.ServerConfigurationServiceImpl;

public interface ServerConfigurationService {

	public List<String> getServers() throws Exception;
		
	/**
	 * Factory for obtaining implementations
	 */
    public static class Factory {
		private static ServerConfigurationService provider;
	    public static ServerConfigurationService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	// Place a file called uk.ac.ox.oucs.ords.utilities.csv in src/main/resources/META-INF/services
	    	// containing the classname to load as the CsvService implementation. 
	    	// By default we load the Hibernate implementation.
	    	//
	    	if (provider == null){
	    		ServiceLoader<ServerConfigurationService> ldr = ServiceLoader.load(ServerConfigurationService.class);
	    		for (ServerConfigurationService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new ServerConfigurationServiceImpl();
	    	}
	    	
	    	return provider;
	    }
	}
}

package uk.ac.ox.it.ords.api.statistics.services.impl;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.services.ServerConfigurationService;

public class ServerConfigurationServiceImpl implements ServerConfigurationService {

	Logger log = LoggerFactory.getLogger(ServerConfigurationServiceImpl.class);
	
	public static final String DEFAULT_SERVER_CONFIG_LOCATION = "/etc/ordsConfig/serverConfig.xml";


	@Override
	public List<String> getServers() throws Exception {
		
		String serverConfigurationLocation = DEFAULT_SERVER_CONFIG_LOCATION;

		try {
			//
			// Load the meta-configuration file
			//
			XMLConfiguration config = new XMLConfiguration("config.xml");
			serverConfigurationLocation = config.getString("serverConfigurationLocation");
			if (serverConfigurationLocation == null){
				log.warn("No server configuration location set; using defaults");
				serverConfigurationLocation = DEFAULT_SERVER_CONFIG_LOCATION;
			}
		} catch (Exception e) {
			log.warn("No server configuration location set; using defaults");
			serverConfigurationLocation = DEFAULT_SERVER_CONFIG_LOCATION;
		}
		
		try {
			//
			// Load the configuration file
			//
			XMLConfiguration serverConfig = new XMLConfiguration(serverConfigurationLocation);

			//
			// Read the server list
			//
			String[] servers = serverConfig.getStringArray("serverList.server[@name]");
			return Arrays.asList(servers);
		} catch (Exception e) {
			log.error("Unable to find server configuration file in " + serverConfigurationLocation);
			throw new FileNotFoundException();
		}
		

	}
}

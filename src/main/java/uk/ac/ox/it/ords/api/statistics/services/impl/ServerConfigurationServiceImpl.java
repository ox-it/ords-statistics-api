package uk.ac.ox.it.ords.api.statistics.services.impl;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.configuration.CommonVars;
import uk.ac.ox.it.ords.api.statistics.services.ServerConfigurationService;

public class ServerConfigurationServiceImpl implements ServerConfigurationService {

	Logger log = LoggerFactory.getLogger(ServerConfigurationServiceImpl.class);

	@Override
	public List<String> getServers() throws Exception {
		
		try {
			//
			// Load the configuration file
			//
			XMLConfiguration config = new XMLConfiguration(CommonVars.serverConfig);
			
			//
			// Read the server list
			//
			String[] servers = config.getStringArray("serverList.server[@name]");
			return Arrays.asList(servers);
		} catch (Exception e) {
			log.error("Unable to find server configuration file in " + CommonVars.serverConfig);
			throw new FileNotFoundException();
		}
		

	}
}

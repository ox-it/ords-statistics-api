package uk.ac.ox.it.ords.api.statistics.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.configuration.CommonVars;
import uk.ac.ox.it.ords.api.statistics.configuration.servers.OrdsServerConfig;
import uk.ac.ox.it.ords.api.statistics.configuration.servers.Server;
import uk.ac.ox.it.ords.api.statistics.services.ServerConfigurationService;

public class ServerConfigurationServiceImpl implements ServerConfigurationService {

	Logger log = LoggerFactory.getLogger(ServerConfigurationServiceImpl.class);

	@Override
	public List<Server> getServers() throws Exception {
		return getServerConfiguration().getServerList().getServer();
	}

	@Override
	public OrdsServerConfig getServerConfiguration() throws Exception {
		File serverConfig = new File(CommonVars.serverConfig);
		if (!serverConfig.exists()) {
			log.error("Unable to find server configuration file in " + CommonVars.serverConfig);
			throw new FileNotFoundException();
		}

		if (log.isDebugEnabled()) {
			log.debug("Config file exists at " + CommonVars.serverConfig);
		}

		OrdsServerConfig osc = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(OrdsServerConfig.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			osc = (OrdsServerConfig)jaxbUnmarshaller.unmarshal(serverConfig);
		} catch (JAXBException e) {
			log.error("Configuration file cannot be read");
			e.printStackTrace();
		}
		return osc;
	}
}

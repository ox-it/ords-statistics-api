package uk.ac.ox.it.ords.api.statistics.services;

import static org.junit.Assert.*;

import org.junit.Test;

public class ServerConfigurationServiceTest {
	
	@Test
	public void testLoadConfiguration() throws Exception{
		assertEquals(1, ServerConfigurationService.Factory.getInstance().getServers().size());
	}

}

package uk.ac.ox.it.ords.api.statistics.services;

import org.junit.Test;

public class StatisticsServiceTest {

	@Test
	public void getStats() throws Exception{
		StatisticsService.Factory.getInstance().computeLatestStatistics();
		System.out.println("Projects:"+StatisticsService.Factory.getInstance().getStatistics().getNumberOfClosedProjects());
	}
}

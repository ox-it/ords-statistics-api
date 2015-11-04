package uk.ac.ox.it.ords.api.statistics.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.model.OrdsStatistics;
import uk.ac.ox.it.ords.api.statistics.services.StatisticsService;

public class Statistics {
	
	static Logger log = LoggerFactory.getLogger(Statistics.class);
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatistics(){
		
		OrdsStatistics stats;
		try {
			stats = StatisticsService.Factory.getInstance().getStatistics();
		} catch (Exception e) {
			log.error("Problem obtaining stats: returning 500", e);
			return Response.status(500).build();
		}
		
		return Response.ok(stats).build();
	}

}

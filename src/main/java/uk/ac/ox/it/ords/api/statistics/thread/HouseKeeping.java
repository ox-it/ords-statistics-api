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
package uk.ac.ox.it.ords.api.statistics.thread;

import java.text.ParseException;
import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.services.StatisticsService;

/**
 * From the original Housekeeping class - thread that loads stats daily
 */
public class HouseKeeping extends Thread implements ServletContextListener  {
	
	private HouseKeeping statsGatherer;
	
    private Logger log = LoggerFactory.getLogger(HouseKeeping.class);
    
    public void doShutdown() {
        log.debug("doShutdown called");
    }
    
    
    @Override
    public void run() {
        performHouseKeeping();
    }
    
    public void performHouseKeeping() {
        
        log.debug("Start of Stats gatherer processing");
        waitUntilAfter2();
        
        performComputationsAndEmail(false);
    }
    
    public void performComputationsAndEmail(boolean isTest) {
        int performanceCounter = 1;
        long sleepTime = 1000*60*60*24;// Run once a day
        
        while (true) {
            
            try {
                StatisticsService.Factory.getInstance().computeLatestStatistics();
            }
            catch (Exception ex) {
                log.error("Unable to gather stats", ex);
            }
            
            /*
             * Generate a (weekly) email on ORDS performance
             */
            if (performanceCounter == 1) {
            	// Generate performance metrics and email
            	try {
            		StatisticsService.Factory.getInstance().generateAndSendStatsEmail();
                }
                catch (Exception e) {
                    log.error("Exception generating stats email", e);
                }
            }
            
            try {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException ex) {
            	log.error("Exception caught", ex);
            }

        }
    }
    
    private void waitUntilAfter2() {
        try {
            /*
             * First wait until after 2am or so, so that stats gathering may be done at night
             * when it's quiet
             */
            while (!isAfterTime(2)) {
                Thread.sleep(1000*60*30); // Wait 30 minutes
            }
        }
        catch (ParseException ex) {
            log.error("Error waiting for the right time", ex);
        }
        catch (InterruptedException ex) {
        	log.error("Exception caught", ex);
        }
    }
    
    /**
     * Check whether the current time is after a specific time
     * @param hour The hour to check, e.g. 14
     * @return true if the current time is later than that supplied
     * @throws ParseException 
     */
    public static boolean isAfterTime(int hour) throws ParseException {
        boolean after = false;


        Calendar cal = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.set(Calendar.HOUR_OF_DAY, hour);
        if (future.before(cal)) {
            after = true;
        }

        return after;
    }


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
        try {
            statsGatherer.doShutdown();
            statsGatherer.interrupt();
        }
        catch (Exception ex) {
        	log.error("Exception while shutting down stats gathering thread", ex);
        }
	}


	@Override
	public void contextInitialized(ServletContextEvent arg0) {
        statsGatherer = new HouseKeeping();
        statsGatherer.start();
	}
    
}

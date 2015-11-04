/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ox.it.ords.api.statistics.thread;

import java.text.ParseException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ox.it.ords.api.statistics.services.StatisticsService;

/**
 * From the original Housekeeping class - thread that loads stats daily
 */
public class HouseKeeping extends Thread {
	
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
    
}

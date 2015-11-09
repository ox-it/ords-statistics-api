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
package uk.ac.ox.it.ords.api.statistics.configuration;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Local configuration manager
 */
public class MetaConfiguration {

	public static Logger log = LoggerFactory.getLogger(MetaConfiguration.class);

	private static XMLConfiguration config;
	
	/**
	 * Load meta-configuration
	 */
    private static void load(){
		try {
			config = new XMLConfiguration("config.xml");
		} catch (Exception e) {
			config = null;
			log.warn("No server configuration location set; using defaults");
		}
	}

    /**
     * Get the specified configuration
     * @param identifier
     * @return the configuration, or Null if there is no match
     */
	public static String getConfigurationLocation(String identifier){
		if (config == null) load();
		String location = config.getString(identifier);
		if (location == null){
			log.warn("No configuration location set for "+identifier+"; using defaults");
		}
		return location;
	}

}

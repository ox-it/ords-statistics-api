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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationDetails {

	private static Logger log = LoggerFactory.getLogger(AuthenticationDetails.class);

	public static final String DEFAULT_DATABASE_CONFIG_LOCATION = "/etc/ordsConfig/db.properties";

	private String ordsUser, ordsPassword;
	private String datasetViewer, datasetViewerPassword;
	private String rootDbUser, rootDbPassword;
	private String ordsOdbcUserMasterPassword;

	public AuthenticationDetails() {

		//
		// Load the Database Properties
		//

		/**
		 * The user that can connect to and read/write from/to the ords database. Used for hibernate.
		 */
		ordsUser = MetaConfiguration.getConfiguration().getString("user");
		ordsPassword = MetaConfiguration.getConfiguration().getString("password");

		/**
		 * A read only user used to view public datasets
		 */
		datasetViewer = MetaConfiguration.getConfiguration().getString("datasetViewer");
		datasetViewerPassword = MetaConfiguration.getConfiguration().getString("datasetViewerPassword");

		/**
		 * A root user on the server that can create databases and set the public schema permissions
		 */
		rootDbUser = MetaConfiguration.getConfiguration().getString("rootDbUser");
		rootDbPassword = MetaConfiguration.getConfiguration().getString("rootDbPassword");

		/**
		 * When ords accesses data via a user command (e.g. the user logs in to a project and adds rows to a table in a 
		 * database), ords will do that via a special user linked to that of the user logged in. E.g., fred logs in to
		 * the project and performs the update: under the covers, ords will access the database with user fred_ords. 
		 * ordsOdbcUserMasterPassword provides the password for this access.
		 */
		ordsOdbcUserMasterPassword = MetaConfiguration.getConfiguration().getString("ordsOdbcUserMasterPassword");
		if ((ordsOdbcUserMasterPassword == null) || (ordsOdbcUserMasterPassword.length() == 0) ) {
			log.error("Unable to get odbc master password - defaulting");
			ordsOdbcUserMasterPassword = ordsPassword;
		}
	}

	public String getDatasetViewer() {
		return datasetViewer;
	}

	public String getDatasetViewerPassword() {
		return datasetViewerPassword;
	}

	public String getOrdsUser() {
		return ordsUser;
	}

	public String getOrdsPassword() {
		return ordsPassword;
	}

	public String getRootDbUser() {
		return rootDbUser;
	}

	public String getRootDbPassword() {
		return rootDbPassword;
	}

	public String getOrdsOdbcUserMasterPassword() {
		return ordsOdbcUserMasterPassword;
	}
}

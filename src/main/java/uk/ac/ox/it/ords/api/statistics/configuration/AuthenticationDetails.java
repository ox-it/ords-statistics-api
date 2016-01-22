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
	
	private String rootDbUser, rootDbPassword;
	private String ordsOdbcUserMasterPassword;

	public AuthenticationDetails() {

		//
		// Load the Database Properties
		//

		/**
		 * A root user on the server that can create databases and set the public schema permissions
		 */
		rootDbUser = MetaConfiguration.getConfiguration().getString("ords.database.rootdbuser");
		rootDbPassword = MetaConfiguration.getConfiguration().getString("ords.database.rootdbpassword");

		/**
		 * When ords accesses data via a user command (e.g. the user logs in to a project and adds rows to a table in a 
		 * database), ords will do that via a special user linked to that of the user logged in. E.g., fred logs in to
		 * the project and performs the update: under the covers, ords will access the database with user fred_ords. 
		 * ordsOdbcUserMasterPassword provides the password for this access.
		 */
		 ordsOdbcUserMasterPassword = MetaConfiguration.getConfiguration().getString("ords.odbc.masterpassword");
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

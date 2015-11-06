package uk.ac.ox.it.ords.api.statistics.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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

		String dbConfigurationLocation = DEFAULT_DATABASE_CONFIG_LOCATION;
		dbConfigurationLocation = MetaConfiguration.getConfigurationLocation("databasePropertiesLocation");
		if (dbConfigurationLocation == null){
			log.warn("No server configuration location set; using defaults");
			dbConfigurationLocation = DEFAULT_DATABASE_CONFIG_LOCATION;
		}

		try {

			//
			// Load the Database Properties
			//
			PropertiesConfiguration prop = new PropertiesConfiguration(dbConfigurationLocation);

			/**
			 * The user that can connect to and read/write from/to the ords database. Used for hibernate.
			 */
			ordsUser = prop.getString("user");
			ordsPassword = prop.getString("password");

			/**
			 * A read only user used to view public datasets
			 */
			datasetViewer = prop.getString("datasetViewer");
			datasetViewerPassword = prop.getString("datasetViewerPassword");

			/**
			 * A root user on the server that can create databases and set the public schema permissions
			 */
			rootDbUser = prop.getString("rootDbUser");
			rootDbPassword = prop.getString("rootDbPassword");

			/**
			 * When ords accesses data via a user command (e.g. the user logs in to a project and adds rows to a table in a 
			 * database), ords will do that via a special user linked to that of the user logged in. E.g., fred logs in to
			 * the project and performs the update: under the covers, ords will access the database with user fred_ords. 
			 * ordsOdbcUserMasterPassword provides the password for this access.
			 */
			ordsOdbcUserMasterPassword = prop.getString("ordsOdbcUserMasterPassword");
			if ((ordsOdbcUserMasterPassword == null) || (ordsOdbcUserMasterPassword.length() == 0) ) {
				log.error("Unable to get odbc master password - defaulting");
				ordsOdbcUserMasterPassword = ordsPassword;
			}
		} catch (ConfigurationException e) {
			log.error("Unable to read main properties file " + dbConfigurationLocation, e);
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

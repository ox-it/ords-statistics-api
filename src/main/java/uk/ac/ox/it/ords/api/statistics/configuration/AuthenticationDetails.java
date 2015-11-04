package uk.ac.ox.it.ords.api.statistics.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationDetails {
	
	private static Logger log = LoggerFactory.getLogger(AuthenticationDetails.class);
	
	private String ordsUser, ordsPassword;
	private String datasetViewer, datasetViewerPassword;
	private String rootDbUser, rootDbPassword;
	private String ordsOdbcUserMasterPassword;

	public AuthenticationDetails() {
		try {
			Properties prop = readProperties(CommonVars.mainPropertiesFile);
			
			/**
			 * The user that can connect to and read/write from/to the ords database. Used for hibernate.
			 */
			ordsUser = prop.getProperty("user");
			ordsPassword = prop.getProperty("password");
			
			/**
			 * A read only user used to view public datasets
			 */
			datasetViewer = prop.getProperty("datasetViewer");
			datasetViewerPassword = prop.getProperty("datasetViewerPassword");
			
			/**
			 * A root user on the server that can create databases and set the public schema permissions
			 */
			rootDbUser = prop.getProperty("rootDbUser");
			rootDbPassword = prop.getProperty("rootDbPassword");
			
			/**
			 * When ords accesses data via a user command (e.g. the user logs in to a project and adds rows to a table in a 
			 * database), ords will do that via a special user linked to that of the user logged in. E.g., fred logs in to
			 * the project and performs the update: under the covers, ords will access the database with user fred_ords. 
			 * ordsOdbcUserMasterPassword provides the password for this access.
			 */
			ordsOdbcUserMasterPassword = prop.getProperty("ordsOdbcUserMasterPassword");
            if ((ordsOdbcUserMasterPassword == null) || (ordsOdbcUserMasterPassword.length() == 0) ) {
                log.error("Unable to get odbc master password - defaulting");
                ordsOdbcUserMasterPassword = ordsPassword;
            }
		}
		catch (IOException e) {
			log.error("Unable to read main properties file " + CommonVars.mainPropertiesFile, e);
		}
	}

//	public String getOrdsReadOnlyUser() {
//		return ordsReadOnlyUser;
//	}
//
//	public String getOrdsReadOnlyPassword() {
//		return ordsReadOnlyPassword;
//	}

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
	
	
	private Properties readProperties(File f) throws IOException{
		Properties properties = new Properties();

		if (f == null) {
			throw new IOException("Null properties file");
		}

		if (!f.exists()) {
			f = new File(CommonVars.DB_PROPS);
		}

		properties.load(new FileInputStream(f));

		return properties;
	}
	
    /**
     * Read the provided properties file.
     * 
     * @param props The path of the properties file to read
     * @return a Properties object containing project properties
     * @throws IOException if the file is null or cannot be read
     */
    private Properties readProperties(String props) throws IOException {
    	if (props == null) {
    		throw new IOException("Null properties file");
    	}
        return readProperties(new File(props));
    }

}

package uk.ac.ox.it.ords.api.statistics.configuration;

public class CommonVars {

    public static final String configurationFolder = "/etc/ordsConfig/";
    public static String serverConfig = configurationFolder + "serverConfig.xml";
    public static String DB_PROPS = "./src/test/resources/props.properties";
    
    public static String hibernateFile = configurationFolder + "/hibernateSubset.cfg.xml";
    public static String mainHibernateFile = configurationFolder + "/hibernate.cfg.xml";
    public static String mainPropertiesFile = configurationFolder + "/db.properties";

}

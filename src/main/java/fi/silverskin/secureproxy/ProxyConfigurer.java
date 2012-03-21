package fi.silverskin.secureproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peltoel
 */
public class ProxyConfigurer {

    private final String CONFIGFILENAME = "config.properties";
    private Properties configures;
    private static final Logger LOGGER = Logger.getLogger(ProxyConfigurer.class.getName(), null);

    public ProxyConfigurer() {
        configures = new Properties();
        FileInputStream input;
 
        try {
            String basePath = System.getProperty("catalina.base");
            File baseDir = new File(basePath);
            File configFile = new File(baseDir, "conf/secureproxy/" + CONFIGFILENAME);

            if (!configFile.exists()) {
                LOGGER.log(Level.SEVERE, "Config file does not exist.");
                LOGGER.log(Level.SEVERE, "Catalina base dir: {0}", System.getProperty("catalina.base"));
                LOGGER.log(Level.SEVERE, "Config file location: {0}", configFile);
                throw new RuntimeException("Config file didn't exist! " + configFile );
            }
            
            input = new FileInputStream(configFile);
            configures.load(input);
            input.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Received IOException", ex);
            ex.printStackTrace();
        } catch (SecurityException ex) {
            LOGGER.log(Level.SEVERE, "Received SecurityException", ex);
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Received Exception", ex);
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, "Received IllegalArgumentException", ex);
            ex.printStackTrace();
        }
    }

    public ProxyConfigurer(String configFilePath) {
        LOGGER.entering(ProxyConfigurer.class.getName(),
                        "ProxyConfigurer", configFilePath);
        
        configures = new Properties();
        InputStream input;
        try {
            input = getClass().getClassLoader().getResourceAsStream(configFilePath);
            if (input == null) {
                throw new RuntimeException("Config file didn't exists!");
            }
            configures.load(input);
            input.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        LOGGER.exiting(ProxyConfigurer.class.getName(), "ProxyConfigurer");
    }

    /**
     * Get configure by the parameter's name
     *
     * @param key configure the parameter's name
     * @return values of the key in String array
     */
    public String[] getConfigure(String key) {
        String config = configures.getProperty(key);
        if (config == null) 
            return null;
        else
            return config.split(", ");
    }


    /**
     * Getter for read configuration properties
     * @return Properties of the read configuration file.
     */
    public Properties getConfigurationProperties() {
        return configures;
    }


    /**
     * Searches for the property with the specified key in this property list.
     *
     * @param key the property key
     * @return the value in this property list or null if the specified key
     *         cannot be found
     */
    public String getProperty(String key) {
        return configures.getProperty(key);
    }
}

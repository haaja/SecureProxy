package fi.silverskin.secureproxy;

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

    private final String FILE = "config.properties";
    private Properties configures;
    private static final Logger LOGGER = Logger.getLogger(ProxyConfigurer.class.getName(), null);
    
    public ProxyConfigurer() {
        configures = new Properties();
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(FILE);
            if (input == null) {
                throw new RuntimeException("Config file didn't exists!");
            }
            configures.load(input);
            input.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public ProxyConfigurer(String path) {
        configures = new Properties();
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(path);
            if (input == null) {
                throw new RuntimeException("Config file didn't exists!");
            }
            configures.load(input);
            input.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } 
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
    
}

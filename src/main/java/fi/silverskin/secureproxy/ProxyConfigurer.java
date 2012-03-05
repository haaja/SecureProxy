/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import java.io.*;
import java.util.*;
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
            configures.load(input);
            input.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Get configure by the parameter's name
     *
     * @param key configure the parameter's name
     * @return value of key (only one)
     */
    public String getConfigure(String key) {
        return configures.getProperty(key);
    }
    
}

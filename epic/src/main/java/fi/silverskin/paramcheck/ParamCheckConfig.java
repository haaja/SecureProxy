/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.paramcheck;

import fi.silverskin.secureproxy.ProxyConfigurer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author peltoel
 */
public class ParamCheckConfig {
    
    private String rule;
    
    /* Hard coded, no idea how to get it any more flexible, maybe when PluginLoader
     * loads this plugin ParamCheck.
     */
    private final String CONFIGFILE = "/plugins/paramcheck.config";
    
    private Properties rules;
    private File configFile;
    private static final Logger LOGGER = Logger.getLogger(ProxyConfigurer.class.getName(), null);
    
    /*
     * Constructor is in same style as in ProxyConfigurer. It uses Properties even if 
     * it needs to read only one configure, that's because of possible later modifies.
     */
    public ParamCheckConfig() {
        rules = new Properties();
        FileInputStream input;
 
        try {
            String basePath = System.getProperty("catalina.base");
            File baseDir = new File(basePath);
            configFile = new File(baseDir, CONFIGFILE);

            if (!configFile.exists()) {
                LOGGER.log(Level.SEVERE, "Config file does not exist.");
                LOGGER.log(Level.SEVERE, "Catalina base dir: {0}", System.getProperty("catalina.base"));
                LOGGER.log(Level.SEVERE, "Config file location: {0}", configFile);
                throw new RuntimeException("Config file of plugin ParamCheck didn't exist! " + configFile );
            }
            
            input = new FileInputStream(configFile);
            rules.load(input);
            input.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Received IOException", ex);
        }
    }

    /*
     * @return rule for parameter validating regex
     */
    public String getRule() {
        rule = rules.getProperty("rule");
        if (rule == null) {
            throw new RuntimeException("Config file of plugin ParamCheck must contain the rule!" + configFile );
        }
        return rule;
    }
    
}
package fi.silverskin.secureproxy;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ProxyLogger {

    //this might be problematic as tomcat also uses logging.properties
    private static final String CONFIGFILENAME = "logging.properties";
    private static final Logger LOGGER = Logger.getLogger(ProxyLogger.class.getName());

    /**
     * Initializes logger.
     */
    public static void setup() {
        LOGGER.entering(ProxyLogger.class.getName(), "setup");

        Properties systemProperties = System.getProperties();
        Properties loggingProperties = readConfigFile();
        if (validateConfig(loggingProperties)) {
            systemProperties.setProperty("java.util.logging.config.file",
                                         CONFIGFILENAME);
        }
        else {
            LOGGER.info("Configuration file was invalid. Generating default configuration file.");
            initLogConfigFile();
            systemProperties.setProperty("java.util.logging.config.file",
                                         CONFIGFILENAME);
        }

        File logDirectory = new File(System.getProperty("catalina.base") + "logs");
        if (!logDirectory.exists()) {
            logDirectory.mkdir();
        }

        try {
            LogManager.getLogManager().readConfiguration();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to load configuration file", ex);
        }
        LOGGER.exiting(ProxyLogger.class.getName(), "setup");
    }

    /**
     * Initialized configuration file for logger with default values.
     * If configuration file already exists, method does nothing
     */
    private static void initLogConfigFile() {
        LOGGER.entering(ProxyLogger.class.getName(), "initLogConfigFile");
        File loggerConfigFile = null;
        
        try {
            String tomcatBase = System.getProperty("catalina.base");
            File basedir = new File(tomcatBase);
            loggerConfigFile = new File(basedir, "conf/.secureproxy/" + CONFIGFILENAME);

            if (!loggerConfigFile.exists()) {
                Writer out = new BufferedWriter(new FileWriter(loggerConfigFile));
                Properties logConfig = new Properties();

                //logging handlers
                logConfig.setProperty("handlers",
                                      "java.util.logging.FileHandler," +
                                      "java.util.logging.ConsoleHandler");

                //Common logging level
                logConfig.setProperty(".level", "INFO");

                //ConsoleHandler settings
                logConfig.setProperty("java.util.logging.ConsoleHandler.level",
                                      "INFO");
                logConfig.setProperty("java.util.logging.ConsoleHandler.formatter",
                                      "java.util.logging.SimpleFormatter");

                //FileHandler settings
                logConfig.setProperty("java.util.logging.FileHandler.level",
                                      "INFO");
                logConfig.setProperty("java.util.logging.FileHandler.pattern",
                                      "log/secureproxy.log");
                logConfig.setProperty("java.util.logging.FileHandler.directory",
                                      System.getProperty("catalina.base") + "logs");
                logConfig.setProperty("java.util.logging.FileHandler.limit",
                                      "50000");
                logConfig.setProperty("java.util.logging.FileHandler.count",
                                      "1");
                logConfig.setProperty("java.util.logging.FileHandler.formatter",
                                      "java.util.logging.SimpleFormatter");

                logConfig.store(out, "Configuration file generated");
                out.close();
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to open config file.", ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,
                       "Unable to create config file with default values.",
                       ex);
            ex.printStackTrace();
        }
        LOGGER.exiting(ProxyLogger.class.getName(), "initLogConfigFile");
    }

    /**
     * Reads configuration file of the logger
     * @return parsed configuration properties
     */
    private static Properties readConfigFile() {
        LOGGER.entering(ProxyLogger.class.getName(), "readConfigFile");

        FileInputStream input;
        File baseDir = new File(System.getProperty("catalina.base"));
        File config = new File(baseDir,
                               "conf/.secureproxy/" + CONFIGFILENAME);
        Properties configuration = new Properties();
        if (config == null) {
            throw new RuntimeException("Config file didn't exists!");
        }
        try {
            input = new FileInputStream(config);
            configuration.load(input);
            input.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        LOGGER.exiting(ProxyLogger.class.getName(),
                       "readConfigFile",
                       configuration);

        return configuration;
    }


    /**
     * Validates that config has all needed keys.
     *
     * @param pluginConfig file to validate
     * @return true if file has all the required keys, otherwise false
     */
    public static boolean validateConfig(Properties pluginConfig) {
        LOGGER.entering(ProxyLogger.class.getName(), "validateConfig", pluginConfig);
        boolean isValid = true;

        if (pluginConfig == null) {
            return false;
        }

        if (!pluginConfig.containsKey("handlers")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey(".level")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.ConsoleHandler.level")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.ConsoleHandler.formatter")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.FileHandler.level")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.FileHandler.pattern")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.FileHandler.limit")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.FileHandler.directory")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.FileHandler.count")) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("java.util.logging.FileHandler.formatter")) {
            isValid = false;
        }

        LOGGER.exiting(ProxyLogger.class.getName(), "validateConfig", isValid);
        return isValid;
    }
}

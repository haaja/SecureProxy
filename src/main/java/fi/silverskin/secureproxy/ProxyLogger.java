package fi.silverskin.secureproxy;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ProxyLogger {

    private static final String LOGFILEPATH = "logging.properties";
    private static final Logger LOGGER = Logger.getLogger(ProxyLogger.class.getName());
    private static ProxyConfigurer configurer;

    /**
     * Initializes logger.
     */
    public static void setup() {
        LOGGER.entering(ProxyLogger.class.getName(), "setup");

        Properties systemProperties = System.getProperties();
        configurer = new ProxyConfigurer(LOGFILEPATH);
        Properties loggingProperties = configurer.getConfigurationProperties();
        if (validateConfig(loggingProperties)) {
            systemProperties.setProperty("java.util.logging.config.file",
                                         LOGFILEPATH);
        }
        else {
            LOGGER.info("Configuration file was invalid. Generating new.");
            initLogConfigFile();
            systemProperties.setProperty("java.util.logging.config.file",
                                         LOGFILEPATH);
        }

        File logDirectory = new File("log");
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
        File logConfigFile = new File(LOGFILEPATH);

        if (!logConfigFile.exists()) {
            try {
                Writer out = new BufferedWriter(new FileWriter(logConfigFile));
                Properties logConfig = new Properties();

                //logging handlers
                logConfig.setProperty("handlers",
                                      "java.util.logging.FileHandler,"+
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
                logConfig.setProperty("java.util.logging.FileHandler.limit",
                                      "50000");
                logConfig.setProperty("java.util.logging.FileHandler.count",
                                      "1");
                logConfig.setProperty("java.util.logging.FileHandler.formatter",
                                      "java.util.logging.SimpleFormatter");

                logConfig.store(out, "Configuration file generated");
                out.close();
            } catch (IOException ex) {
                LOGGER.severe("Unable to create config file with default values.");
                ex.printStackTrace();
            }
        }
        LOGGER.exiting(ProxyLogger.class.getName(), "initLogConfigFile");
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

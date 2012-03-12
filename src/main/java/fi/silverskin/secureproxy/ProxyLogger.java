package fi.silverskin.secureproxy;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ProxyLogger {

/*   Hard coded config file untill we know where to put the path to the config
 *   file.
 *
 *   private static ProxyConfigurer configurer = new ProxyConfigurer();
 *   private static final String LOGFILE = configurer.
 *                                         getConfigure("loggingConfigFile")[0];
 */
    private static final String LOGFILE = "logging.properties";
    private static Logger LOGGER = Logger.getLogger(ProxyLogger.class.getName());

    /**
     * Initializes logger.
     */
    public static void setup() {

        initLogConfigFile();

        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("java.util.logging.config.file",
                                     LOGFILE);

        File logDirectory = new File("log");
        if (!logDirectory.exists()) {
            logDirectory.mkdir();
        }

        try {
            LogManager.getLogManager().readConfiguration();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to load configuration file", ex);
        }

    }

    /**
     * Initialized configuration file for logger with default values. 
     * If configuration file already exists, method does nothing
     */
    private static void initLogConfigFile() {
        File logConfigFile = new File(LOGFILE);

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
    }
}

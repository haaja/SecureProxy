package fi.silverskin.secureproxy;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProxyLogger extends Logger {

    private static FileHandler logFileHandler;
    private static ConsoleHandler logConsoleHandler;
    private static SimpleFormatter logFormatter;

    //these should be eventually read from config
    private Level logLevel = Level.INFO;
    private String logFile = "secureproxy.log";


    public ProxyLogger(String loggerName, String resourceBundleName) {
        super(loggerName, resourceBundleName);

        try {
            setup(loggerName);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to create log file.");
        }
    }

    public void setup(String loggerName) throws IOException {

        Logger logger = getLogger(loggerName);
        logger.setLevel(logLevel);

        logFileHandler = new FileHandler(logFile);
        logConsoleHandler = new ConsoleHandler();

        logFormatter = new SimpleFormatter();
        logFileHandler.setFormatter(logFormatter);

        logger.addHandler(logConsoleHandler);
        logger.addHandler(logFileHandler);
    }
}

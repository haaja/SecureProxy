package fi.silverskin.secureproxy;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProxyLogger {

    private static FileHandler logFileHandler;
    private static ConsoleHandler logConsoleHandler;
    private static SimpleFormatter logFormatter;

    //these should be eventually read from config
    private static Level logLevel = Level.INFO;
    private static String logFile = "secureproxy.log";

    /**
     * Initializes logger.
     *
     * @throws IOException In case it cannot create log file.
     */
    public static void setup() throws IOException {

        Logger logger = Logger.getLogger("fi.silverskin");
        logger.setLevel(logLevel);

        logFileHandler = new FileHandler(logFile);
        logConsoleHandler = new ConsoleHandler();

        logFormatter = new SimpleFormatter();
        logFileHandler.setFormatter(logFormatter);

        logger.addHandler(logConsoleHandler);
        logger.addHandler(logFileHandler);
    }
}

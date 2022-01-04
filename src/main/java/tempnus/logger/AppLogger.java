package tempnus.logger;

import tempnus.file.FileCreator;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {

    private static Logger logger;

    private static final String LOG_DIR = "data";
    private static final String LOG_FILEPATH = "data/log.txt";

    /**
     * Constructor for the AppLogger class.
     *
     * @throws IOException If there are IO problems opening the log file.
     */
    public AppLogger() throws IOException {
        logger = Logger.getLogger(AppLogger.class.getName());
        FileHandler fileHandler = setUpFileHandler();
        logger.addHandler(fileHandler);
    }

    /**
     * Creates a log entry.
     *
     * @param level   Labels attached to each log entry to indicate the severity level.
     * @param message Message of the log entry.
     */
    public static void log(Level level, String message) {
        getLogger().log(level, message);
    }

    /**
     * Getter for logger.
     *
     * @return Logger object used for logging messages.
     */
    private static Logger getLogger() {
        if(logger == null){
            try {
                new AppLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    /**
     * Sets up the file handler by defining how and where to append the log entries.
     *
     * @return FileHandler object.
     * @throws IOException If there are IO problems opening the log file.
     */
    private static FileHandler setUpFileHandler() throws IOException {
        FileCreator.createFileIfNotExist(LOG_DIR, LOG_FILEPATH);
        FileHandler fileHandler = new FileHandler(LOG_FILEPATH, true);
        SimpleFormatter plainText = new SimpleFormatter();
        fileHandler.setFormatter(plainText);
        return fileHandler;
    }

}

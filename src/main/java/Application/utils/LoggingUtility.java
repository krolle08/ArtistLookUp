package Application.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class LoggingUtility {
    private static final Logger LOGGING = LoggerFactory.getLogger(LoggingUtility.class);

    public static void info(String message) {
        LOGGING.info(message);
    }
    public static void warn(String message) {
        LOGGING.warn(message);
    }
    public static void error(String message) {
        LOGGING.error(message);
    }
    public static void info(String format, Object... arguments) {
        LOGGING.info(format, arguments);
    }
    public static void warn(String format, Object... arguments) {
        LOGGING.warn(format, arguments);
    }
    public static void error(String format, Object... arguments) {
        LOGGING.error(format, arguments);
    }
}

package es.etg.dam.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {

    private static final String LOGGER = "Logger";

    public static Logger crearLog(String ficheroLog) throws IOException {
        Logger logger = Logger.getLogger(LOGGER);

        if (logger.getHandlers().length == 0) {
            FileHandler fh = new FileHandler(ficheroLog, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        }
        return logger;
    }

    public static void escribirLog(Logger logger, Level nivel, String mensaje) {
        logger.log(nivel, mensaje);
    }

    public static void escribirLog(Logger logger, Level nivel, String mensaje, Throwable e) {
        logger.log(nivel, mensaje, e);
    }
}

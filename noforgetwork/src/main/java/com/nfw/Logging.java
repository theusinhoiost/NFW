package com.nfw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logging {
    private static final Logger logger = LogManager.getLogger(Logging.class);

    public static void logInfo(String msg){
        logger.info(msg);
    }
    public static void logDebug(String msg){
        logger.debug(msg);
    }
    public static void logWarn(String msg){
        logger.warn(msg);
    }
    public static void logError(String msg){
        logger.error(msg);
    }
    public static void logFatal(String msg){
        logger.fatal(msg);
    }
}

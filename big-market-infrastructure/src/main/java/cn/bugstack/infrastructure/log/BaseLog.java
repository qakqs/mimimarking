package cn.bugstack.infrastructure.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseLog {

    private static final Logger logger = LoggerFactory.getLogger(BaseLog.class);


    public static void debug(String message) {
        logger.debug(message);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message, Throwable e) {
        logger.error(message, e);
    }

}

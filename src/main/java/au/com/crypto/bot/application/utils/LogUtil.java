package au.com.crypto.bot.application.utils;

import org.slf4j.Logger;
import serilogj.Log;

@Deprecated
public class LogUtil {

    public static void printLog(Logger logger, String level, String clazz, String message) {
        printLog(logger,level,clazz,message,null);
    }

   public enum STATUS{

        WARNING,INFO, DEBUG, ERROR;

    }
    public static void printLog(Logger logger, String level, String clazz, String message,  Object obj) {

        String appName = "SignalCatcher";
        if (level.equalsIgnoreCase("ERROR")) {
            if (obj !=null) {
                logger.error(message + obj);
                Log.error("{@Application} " + clazz + " " + message + " {@obj} ", appName, obj);
            } else {
                logger.error(message);
                Log.error("{@Application} " + clazz + " " + message, appName);
            }

        } else if (level.equalsIgnoreCase("DEBUG")) {
            if (obj !=null) {
                logger.debug(message + obj);
                Log.debug("{@Application} " + clazz + " " + message+ " {@obj} ", appName, obj);
            } else {
                logger.debug(message);
                Log.debug("{@Application} " + clazz + " " + message, appName);
            }
        } else if (level.equalsIgnoreCase("INFO")) {
            if (obj !=null) {
                logger.info(message + obj);
                Log.information("{@Application} " + clazz + " " + message+ " {@obj} ", appName, obj);
            } else {
                logger.info(message);
                Log.information("{@Application} " +clazz + " " + message, appName);
            }
        } else if (level.equalsIgnoreCase("WARNING")) {
            if (obj !=null) {
                logger.warn(message + obj);
                Log.warning("{@Application} " + clazz + " " + message+ " {@obj} ", appName, obj);
            } else {
                logger.warn(message);
                Log.warning("{@Application} " + clazz + " " + message, appName);
            }
        }

    }
}

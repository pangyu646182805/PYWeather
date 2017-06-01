package com.neuroandroid.pyweather.utils;

/**
 * Created by NeuroAndroid on 2017/3/7.
 */

public class L {
    private static final Logger sDefaultLevelLogger = new Logger();

    protected static boolean sGlobalToggle = true;

    private L() {
    }

    public static Logger showPath() {
        Logger logger = cloneDefaultLogger();
        if (logger != null) {
            logger.setMethodOffset(-1);
            logger.showPath();
            return logger;
        } else {
            return sDefaultLevelLogger;
        }
    }

    public static Logger tag(Object tag) {
        Logger logger = cloneDefaultLogger();
        if (logger != null) {
            logger.setMethodOffset(-1);
            logger.setTag(tag);
            return logger;
        } else {
            return sDefaultLevelLogger;
        }
    }

    private static Logger cloneDefaultLogger() {
        try {
            Logger logger = (Logger) sDefaultLevelLogger.clone();
            logger.setMethodOffset(-1);
            logger.showPath();
            return logger;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Logger createLogger() {
        Logger logger = new Logger();
        logger.setMethodOffset(-1);
        return logger;
    }

    public static Logger getDefaultLevelLogger() {
        return sDefaultLevelLogger;
    }

    public static void i(String message) {
        sDefaultLevelLogger.i(message);
    }

    public static void w(String message) {
        sDefaultLevelLogger.w(message);
    }

    public static void d(String message) {
        sDefaultLevelLogger.d(message);
    }

    public static void e(String message) {
        sDefaultLevelLogger.e(message);
    }

    public static void v(String message) {
        sDefaultLevelLogger.v(message);
    }

    public static void wtf(String message) {
        sDefaultLevelLogger.wtf(message);
    }

    public static void i(Throwable tr, String message) {
        sDefaultLevelLogger.i(tr, message);
    }

    public static void w(Throwable tr, String message) {
        sDefaultLevelLogger.w(tr, message);
    }

    public static void d(Throwable tr, String message) {
        sDefaultLevelLogger.d(tr, message);
    }

    public static void e(Throwable tr, String message) {
        sDefaultLevelLogger.e(tr, message);
    }

    public static void v(Throwable tr, String message) {
        sDefaultLevelLogger.v(tr, message);
    }

    public static void wtf(Throwable tr, String message) {
        sDefaultLevelLogger.wtf(message, tr);
    }

    public static void i(String prepare, Object... args) {
        sDefaultLevelLogger.i(prepare, args);
    }

    public static void e(String prepare, Object... args) {
        sDefaultLevelLogger.e(prepare, args);
    }

    public static void w(String prepare, Object... args) {
        sDefaultLevelLogger.w(prepare, args);
    }

    public static void d(String prepare, Object... args) {
        sDefaultLevelLogger.d(prepare, args);
    }

    public static void v(String prepare, Object... args) {
        sDefaultLevelLogger.v(prepare, args);
    }

    public static void wtf(String prepare, Object... args) {
        sDefaultLevelLogger.wtf(prepare, args);
    }

    public static void json(String json) {
        sDefaultLevelLogger.json(json);
    }

    public static void setGlobalToggle(boolean globalToggle) {
        sGlobalToggle = globalToggle;
    }
}

package at.asdf00.avaplus.util;

import at.asdf00.avaplus.ModConfig;
import org.apache.logging.log4j.Logger;

public class ModLogger {
    private static Logger l;
    public static void setLogger(Logger logger) {
        l = logger;
    }

    public static void info(String msg, boolean debug) {
        if (!debug || ModConfig.ENABLE_DEBUG_OUTPUT)
            l.info(msg);
    }
    public static void warn(String msg, boolean debug) {
        if (!debug || ModConfig.ENABLE_DEBUG_OUTPUT)
            l.warn(msg);
    }
    public static void error(String msg, boolean debug) {
        if (!debug || ModConfig.ENABLE_DEBUG_OUTPUT)
            l.error(msg);
    }
}

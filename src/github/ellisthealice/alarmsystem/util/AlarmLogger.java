/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 *
 * @author EllisTheAlice
 */
public class AlarmLogger {

    private final Logger logger;

    private AlarmLogger(Class c) {
        logger = Logger.getLogger(c.getName());
        logger.setLevel(Props.LOG_LEVEL);
        logger.setUseParentHandlers(Props.LOG_TO_CONSOLE);
        try {
            SimpleFormatter formatter = new SimpleFormatter();
            StreamHandler handler;
            if (Props.LOG_STRATEGY == LogStrategy.FILE) {
                handler = new FileHandler(Props.LOG_PATH + File.separator + Props.LOG_FILEPATTERN, Props.LOG_MAX_FILESIZE, Props.LOG_FILECOUNT);
            } else {
                handler = new ConsoleHandler();
            }
            handler.setFormatter(formatter);
            logger.addHandler(handler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(AlarmLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static AlarmLogger getLogger(Class c) {
        return new AlarmLogger(c);
    }

    public void logError(String message) {
        logger.log(Level.SEVERE, message);
    }

    public void logInfo(String message) {
        logger.info(message);
    }

}

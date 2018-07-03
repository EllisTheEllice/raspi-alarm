/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.util;

import java.util.logging.Level;

/**
 *
 * @author sengbers
 */
public class Props {

    public static final boolean DEBUG = true;
    public static final int MAX_THREADS = 5;
    public static final String BUCKETNAME = "ir-sensor";
    public static final String REGION = "eu-west-1";
    public static final String SAVEPATH = "C:\\Users\\sengbers\\Pictures\\s3test";

    public static final String LOG_PATH = "C:\\Users\\sengbers\\Pictures\\s3test\\log";
    public static final String LOG_FILEPATTERN = "alarmsystem";
    public static final LogStrategy LOG_STRATEGY = LogStrategy.FILE;
    public static Level LOG_LEVEL = Level.INFO;
    public static final int LOG_MAX_FILESIZE = 1024 * 1024; //1MB
    public static final int LOG_FILECOUNT = 10;

    private Props() {

    }
}

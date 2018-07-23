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

    //general
    public static final boolean DRY_RUN = true;
    public static final boolean LOG_TO_CONSOLE = true;
    public static final int MAX_UPLOAD_THREADS = 5;
    public static final String SAVEPATH = "/home/pi/Pictures";
    public static final boolean CLEANUP = false;

    //aws general
    public static final String REGION = "eu-west-1";

    //s3
    public static final String BUCKETNAME = "ir-sensor";

    //sns
    public static final String SNS_TOPIC_ARN = "arn:aws:sns:us-east-1:575860075599:IntrusionDetection";
    public static final String SNS_INTRUDER_SUBJECT = "intruder detected!";
    public static final String SNS_INTRUDER_MESSAGE = "We have detected an intruder! Time: %s";

    //Logging
    public static final String LOG_PATH = "/var/log/alarmsystem";
    public static final String LOG_FILEPATTERN = "alarmsystem";
    public static final LogStrategy LOG_STRATEGY = LogStrategy.FILE;
    public static Level LOG_LEVEL = Level.INFO;
    public static final int LOG_MAX_FILESIZE = 1024 * 1024; //1MB
    public static final int LOG_FILECOUNT = 10;

    //radio
    public static final String SWITCH_A_CODE = "";
    public static final String SWITCH_B_CODE = "";
    public static final String SWITCH_C_CODE = "";

    //camera
    public static final int CAMERA_HEIGHT = 500;
    public static final int CAMERA_WIDTH = 500;
    public static final int CAMERA_BRIGHTNESS = 75;
    public static final int CAMERA_RECORDING_LENGTH = 60000;
    public static final int CAMERA_IMG_INTERVAL = 250;

//    public Props() {
//        Properties prop = new Properties();
//        String filename = "..\\..\\..\\..\\ressources\\example.properties";
//        File file = new File(filename);
//
//        try (FileInputStream fis = new FileInputStream(file)) {
//            //load a properties file from class path, inside static method
//            prop.load(fis);
//        } catch (IOException ex) {
//            Logger.getLogger(Props.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}

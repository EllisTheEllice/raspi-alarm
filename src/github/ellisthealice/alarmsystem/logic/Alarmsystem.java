/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.logic;

/**
 *
 * @author EllisTheAlice
 */
import github.ellisthealice.alarmsystem.util.RegexFileFilter;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import com.pi4j.io.gpio.GpioController;
import github.ellisthealice.alarmsystem.aws.s3.ImageUploader;
import github.ellisthealice.alarmsystem.util.AlarmLogger;
import github.ellisthealice.alarmsystem.util.Props;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Alarmsystem {

    // This is the controller.
    private GpioController gpio;
    private final static AlarmLogger LOGGER = AlarmLogger.getLogger(Alarmsystem.class);

    public static void main(String[] args) throws FailedToRunRaspistillException, IOException, InterruptedException, ExecutionException {
        //JvPi jvpi = new Alarmsystem();
        //MotionDetector detector=new MotionDetector(new MotionDetectionListener());
        //String path="/home/pi/Pictures";
        String path = Props.SAVEPATH;
        LinkedList<Future<String>> futures = new LinkedList<>();
        //PiCam cam = new PiCam(path, 500, 500, 75, 1);
        ImageUploader uploader = new ImageUploader();
        //for (int i = 0; i < 50; i++) {
        //    System.out.println("Taking " + i);
        //cam.timelapse(true, "myimg-" + i + "-%04d.jpg", 2000, 200);

        File dir = new File(path);
        if (!dir.isDirectory()) {
            throw new IllegalStateException("wtf mate?");
        }
        //for (File file : dir.listFiles(new RegexFileFilter("myimg-" + i + "*"))) {
        for (File file : dir.listFiles(new RegexFileFilter("a*"))) {
            LOGGER.logInfo("uploading...");
            Future<String> f = uploader.upload(file);
            futures.add(f);
        }

        for (Future<String> future : futures) {
            LOGGER.logInfo(future.get());
        }
        uploader.stop();

        // }
        // cam.stop();
        //cam.takeVideo("myvideo.h264", 10);
        /*while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
    }

}

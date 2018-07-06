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
import com.amazonaws.services.sns.model.PublishResult;
import github.ellisthealice.alarmsystem.util.RegexFileFilter;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import com.pi4j.io.gpio.GpioController;
import github.ellisthealice.alarmsystem.aws.s3.ImageUploader;
import github.ellisthealice.alarmsystem.aws.sns.MessageBroker;
import github.ellisthealice.alarmsystem.camera.PiCam;
import github.ellisthealice.alarmsystem.ir.MotionDetectionListener;
import github.ellisthealice.alarmsystem.ir.MotionDetector;
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

    public static void main(String[] args) throws InterruptedException, FailedToRunRaspistillException {
        LOGGER.logInfo("Hi");
        System.out.println("hi");
        MotionDetector detector = new MotionDetector(new MotionDetectionListener());
        System.out.println("ho");

        while (true) {
            Thread.sleep(100);
        }
    }

}

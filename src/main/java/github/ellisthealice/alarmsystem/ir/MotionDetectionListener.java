/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.ir;

import com.amazonaws.services.s3.AmazonS3;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import github.ellisthealice.alarmsystem.aws.sns.MessageBroker;
import github.ellisthealice.alarmsystem.aws.AWSClientFactory;
import github.ellisthealice.alarmsystem.camera.PiCam;
import github.ellisthealice.alarmsystem.logic.AsyncUploadworker;
import github.ellisthealice.alarmsystem.util.Props;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EllisTheAlice
 */
public class MotionDetectionListener implements MotionDetectionInterface {

    private final MessageBroker broker = new MessageBroker();
    private final PiCam cam;
    private final String IMG_PATTERN = "alarmsystem-%04d.jpg";
    private volatile List<String> processedImages = new ArrayList<>();
    private ScheduledExecutorService executor;
    private final AsyncUploadworker worker;

    public MotionDetectionListener() throws FailedToRunRaspistillException {
        cam = new PiCam(Props.SAVEPATH, Props.CAMERA_HEIGHT, Props.CAMERA_WIDTH, Props.CAMERA_BRIGHTNESS, 1);
        AmazonS3 s3 = AWSClientFactory.getS3Client();
        worker = new AsyncUploadworker(processedImages, s3);
    }

    @Override
    public void motionDetected() {
        try {
            executor = Executors.newScheduledThreadPool(Props.MAX_UPLOAD_THREADS);
            broker.sendIntruderWarning();
            executor.scheduleAtFixedRate(worker, Props.CAMERA_IMG_INTERVAL + 30, 500, TimeUnit.MILLISECONDS);
            cam.timelapse(true, IMG_PATTERN, Props.CAMERA_RECORDING_LENGTH, Props.CAMERA_IMG_INTERVAL);
            Thread.sleep(5000); //give some time to process the remaining images
            executor.shutdown();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(MotionDetectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

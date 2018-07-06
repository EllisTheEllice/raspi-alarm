/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.ir;

import com.amazonaws.services.sns.model.PublishResult;
import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import github.ellisthealice.alarmsystem.aws.s3.ImageUploader;
import github.ellisthealice.alarmsystem.aws.sns.MessageBroker;
import github.ellisthealice.alarmsystem.camera.PiCam;
import github.ellisthealice.alarmsystem.util.AlarmLogger;
import github.ellisthealice.alarmsystem.util.Props;
import github.ellisthealice.alarmsystem.util.RegexFileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author EllisTheAlice
 */
public class MotionDetectionListener implements MotionDetectionInterface {

    private final ImageUploader uploader = new ImageUploader();
    private final MessageBroker broker = new MessageBroker();
    private final PiCam cam;
    private static final AlarmLogger LOGGER = AlarmLogger.getLogger(MotionDetectionListener.class);
    private final String IMG_PATTERN = "";

    public MotionDetectionListener() throws FailedToRunRaspistillException {
        cam = new PiCam(Props.SAVEPATH, 500, 500, 75, 1);
    }

    @Override
    public void motionDetected() {
        try {
            LinkedList<Future<String>> futures = new LinkedList<>();
            Future<PublishResult> publishResult = broker.sendIntruderWarning();

            BufferedImage img = cam.takeBufferedStill();
            uploader.upload(img, "firstshot");

            for (int i = 0; i < Props.CAMERA_REPEATS; i++) {
                cam.timelapse(true, "myimg-" + i + "-%04d.jpg", Props.CAMERA_RECORDING_LENGTH, Props.CAMERA_IMG_INTERVAL);

                File dir = new File(Props.SAVEPATH);
                for (File file : dir.listFiles(new RegexFileFilter("myimg-" + i + "-*"))) {
                    LOGGER.logInfo("uploading...");
                    Future<String> f = uploader.upload(file);
                    futures.add(f);
                }
            }

            for (Future<String> future : futures) {
                LOGGER.logInfo(future.get());
            }
            //uploader.stop();

            LOGGER.logInfo(publishResult.get().getMessageId());

        } catch (IOException | InterruptedException | ExecutionException ex) {
            LOGGER.logError(ex.getMessage());
        }
    }

}

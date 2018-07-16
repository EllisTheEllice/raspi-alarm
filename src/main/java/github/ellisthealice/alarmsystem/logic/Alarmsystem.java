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
import com.amazonaws.kinesisvideo.common.exception.KinesisVideoException;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import github.ellisthealice.alarmsystem.ir.MotionDetectionListener;
import github.ellisthealice.alarmsystem.ir.MotionDetector;
import github.ellisthealice.alarmsystem.util.AlarmLogger;

public class Alarmsystem {

    private final static AlarmLogger LOGGER = AlarmLogger.getLogger(Alarmsystem.class);
    private static MotionDetector detector;

    public static void main(String[] args) throws InterruptedException, FailedToRunRaspistillException, KinesisVideoException {
        LOGGER.logInfo("Starting alarmsystem...");
        detector = new MotionDetector(new MotionDetectionListener());
        detector.startDetection();
        LOGGER.logInfo("Alarmsystem started");

        //VideoStreamer streamer=new VideoStreamer();
        while (true) {
            Thread.sleep(100);
        }
    }

}

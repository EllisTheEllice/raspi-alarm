/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.camera;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author EllisTheAlice
 */
public class PiCam extends RPiCamera {

    private final int height, width, brightness, timeout;
    private final String saveDir;

    public PiCam(String saveDir, int height, int width, int brightness, int timeout) throws FailedToRunRaspistillException {
        super(saveDir);
        setWidth(width).setHeight(height).setBrightness(brightness).setExposure(Exposure.AUTO).setTimeout(timeout).turnOffPreview();
        setQuality(75);
        this.saveDir = saveDir;
        this.height = height;
        this.width = width;
        this.brightness = brightness;
        this.timeout = timeout;
    }

    public File takeVideo(String videoname, int time) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(String.format("raspivid -w %d -h %d -n -t %d -o %s", width, height, time, saveDir + "/" + videoname));
        p.waitFor();
        return new File(videoname);
    }

    public BufferedInputStream takeVideoStream(int time) throws IOException {
        Process p = Runtime.getRuntime().exec(String.format("raspivid -w %d -h %d -n -t %d -o -", width, height, time));
        BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
        return bis;
    }

    public File timelapse(boolean wait, String pictureName, int length, int interval) throws IOException, InterruptedException {
        setTimeout(length);
        File f = super.timelapse(wait, pictureName, interval);
        setTimeout(timeout);
        return f;
    }

}

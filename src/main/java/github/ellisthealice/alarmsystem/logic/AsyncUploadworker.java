/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.logic;

import com.amazonaws.services.s3.AmazonS3;
import github.ellisthealice.alarmsystem.aws.s3.ImageUploader;
import github.ellisthealice.alarmsystem.util.AlarmLogger;
import github.ellisthealice.alarmsystem.util.Props;
import github.ellisthealice.alarmsystem.util.RegexFileFilter;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author EllisTheAlice
 */
public class AsyncUploadworker implements Runnable {

    private final List<String> PROCESSED_IMAGES;
    private final AlarmLogger LOGGER = AlarmLogger.getLogger(getClass());
    private final ImageUploader uploader;
    
    public AsyncUploadworker(List<String> processedImages, AmazonS3 s3) {
        this.PROCESSED_IMAGES = processedImages;
        uploader=new ImageUploader(s3);
    }

    @Override
    public void run() {
        LOGGER.logInfo("Thread " + Thread.currentThread().getName() + " is looking for images...");
        File dir = new File(Props.SAVEPATH);
        for (File file : dir.listFiles(new RegexFileFilter("^alarmsystem.*\\.jpg$"))) {
            LOGGER.logInfo("Thread " + Thread.currentThread().getName() + " found some images. Checking if they have been processed");
            if (!PROCESSED_IMAGES.contains(file.getName())) {
                LOGGER.logInfo("Thread " + Thread.currentThread().getName() + " now processes image " + file.getName());
                PROCESSED_IMAGES.add(file.getName());
                LOGGER.logInfo("uploading " + file.getName() + " from Thread " + Thread.currentThread().getName() + "...");
                uploader.upload(file);
            }
        }
        LOGGER.logInfo("Thread " + Thread.currentThread().getName() + " is done");
    }

}

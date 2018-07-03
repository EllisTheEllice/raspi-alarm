/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.aws.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import github.ellisthealice.alarmsystem.logic.Alarmsystem;
import github.ellisthealice.alarmsystem.util.AlarmLogger;
import github.ellisthealice.alarmsystem.util.Props;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EllisTheAlice
 */
public class ImageUploader {

    private final AmazonS3 s3;
    private final ExecutorService EXECUTOR;
    private final UploadThread<String> uploadThread;
    private final static AlarmLogger LOGGER = AlarmLogger.getLogger(ImageUploader.class);

    public ImageUploader() {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Props.REGION)
                .build();

        EXECUTOR = Executors.newFixedThreadPool(Props.MAX_THREADS);
        uploadThread = new UploadThread();
    }

    public Future<String> upload(File f) {
        return doUpload(f.getName(), f.getName());
    }

    public Future<String> upload(BufferedImage img, String name) {
        return doUpload(name, img.toString());
    }

    private Future<String> doUpload(String name, String content) {
        uploadThread.setName(name);
        uploadThread.setContent(content);
        return EXECUTOR.submit(uploadThread);
    }

    public void stop() {
        EXECUTOR.shutdown();
    }

    private class UploadThread<T> implements Callable<String> {

        private String name, content;
        private final AlarmLogger LOGGER = AlarmLogger.getLogger(UploadThread.class);

        @Override
        public String call() {
            //PutObjectResult result = s3.putObject(Props.BUCKETNAME, name, img.toString());
            //return result.getETag();
            LOGGER.logInfo("Do something with " + name + " and content " + content);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ImageUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "myetag-" + Thread.currentThread();
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}

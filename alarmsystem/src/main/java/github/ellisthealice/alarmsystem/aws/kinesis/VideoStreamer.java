/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.aws.kinesis;

import com.amazonaws.kinesisvideo.client.KinesisVideoClient;
import com.amazonaws.kinesisvideo.client.mediasource.MediaSource;
import com.amazonaws.kinesisvideo.common.exception.KinesisVideoException;
import com.amazonaws.kinesisvideo.demoapp.auth.AuthHelper;
import com.amazonaws.kinesisvideo.java.client.KinesisVideoJavaClientFactory;
import com.amazonaws.kinesisvideo.java.mediasource.file.ImageFileMediaSource;
import com.amazonaws.kinesisvideo.java.mediasource.file.ImageFileMediaSourceConfiguration;
import com.amazonaws.regions.Regions;

/**
 *
 * @author EllisTheAlice
 */
public class VideoStreamer {

    private static final String STREAM_NAME = "alarmsystem";
    private static final int FPS_25 = 25;
    private static final String IMAGE_DIR = "src/main/resources/data/h264/";
    private static final String IMAGE_FILENAME_FORMAT = "frame-%03d.h264";
    private static final int START_FILE_INDEX = 1;
    private static final int END_FILE_INDEX = 444;

    final KinesisVideoClient kinesisVideoClient;

    public VideoStreamer() throws KinesisVideoException {
        kinesisVideoClient = KinesisVideoJavaClientFactory
                .createKinesisVideoClient(
                        Regions.EU_WEST_1,
                        AuthHelper.getSystemPropertiesCredentialsProvider());

        MediaSource bytesMediaSource = createImageFileMediaSource();
        kinesisVideoClient.registerMediaSource(STREAM_NAME, bytesMediaSource);
        bytesMediaSource.start();
    }

    /**
     * Create a MediaSource based on local sample H.264 frames.
     *
     * @return a MediaSource backed by local H264 frame files
     */
    private MediaSource createImageFileMediaSource() {
        final ImageFileMediaSourceConfiguration configuration
                = new ImageFileMediaSourceConfiguration.Builder()
                .fps(FPS_25)
                .dir(IMAGE_DIR)
                .filenameFormat(IMAGE_FILENAME_FORMAT)
                .startFileIndex(START_FILE_INDEX)
                .endFileIndex(END_FILE_INDEX)
                .build();
        final ImageFileMediaSource mediaSource = new ImageFileMediaSource();
        mediaSource.configure(configuration);

        return mediaSource;
        
        
        
        
    }
}

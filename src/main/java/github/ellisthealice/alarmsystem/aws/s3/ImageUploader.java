/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.StorageClass;
import github.ellisthealice.alarmsystem.util.AlarmLogger;
import github.ellisthealice.alarmsystem.util.Props;
import java.io.File;

/**
 *
 * @author EllisTheAlice
 */
public class ImageUploader {

    private final AmazonS3 s3;
    private final static AlarmLogger LOGGER = AlarmLogger.getLogger(ImageUploader.class);

    public ImageUploader(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String upload(File f) {
        if (Props.DRY_RUN) {
            if (Props.CLEANUP) {
                f.delete();
            }
            return "mytestetag";
        }

        //return EXECUTOR.submit(uploadThread);
        PutObjectRequest request = new PutObjectRequest(Props.BUCKETNAME, f.getName(), f);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        request.setMetadata(metadata);
        request.withStorageClass(StorageClass.ReducedRedundancy);

        PutObjectResult result = s3.putObject(request);

        if (Props.CLEANUP) {
            f.delete();
        }

        return result.getETag();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.aws.sns;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.PublishResult;
import github.ellisthealice.alarmsystem.util.Props;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 *
 * @author EllisTheAlice
 */
public class MessageBroker {

    private final AmazonSNSAsync sns;

    public MessageBroker() {
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

        sns = AmazonSNSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Props.REGION).build();
    }

    public Future<PublishResult> sendIntruderWarning() {
        return sendNotification(Props.SNS_INTRUDER_SUBJECT, String.format(Props.SNS_INTRUDER_MESSAGE, getDateString()));
    }

    private Future<PublishResult> sendNotification(String subject, String message) {
        if (Props.DEBUG) {
            return CompletableFuture.completedFuture(new PublishResult().withMessageId("test"));
        } else {
            return sns.publishAsync(Props.SNS_TOPIC_ARN, message, subject);
        }
    }

    private String getDateString() {
        DateFormat formatter = new SimpleDateFormat("E d.MM.yyyy H:m:s");
        return formatter.format(new Date());
    }
}

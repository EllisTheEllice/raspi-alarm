/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.aws.sns;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.PublishResult;
import github.ellisthealice.alarmsystem.aws.AWSClientFactory;
import github.ellisthealice.alarmsystem.util.AlarmLogger;
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
    private final AlarmLogger LOGGER = AlarmLogger.getLogger(MessageBroker.class);

    public MessageBroker() {
        this.sns = AWSClientFactory.getSNSClient();
    }

    public Future<PublishResult> sendIntruderWarning() {
        LOGGER.logInfo("Informing about intruder");
        return sendNotification(Props.SNS_INTRUDER_SUBJECT, String.format(Props.SNS_INTRUDER_MESSAGE, getDateString()));
    }

    private Future<PublishResult> sendNotification(String subject, String message) {
        if (Props.DRY_RUN) {
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

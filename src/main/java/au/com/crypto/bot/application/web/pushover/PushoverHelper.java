package au.com.crypto.bot.application.web.pushover;

import org.springframework.util.StringUtils;
import serilogj.Log;
import software.amazon.awssdk.regions.servicemetadata.Ec2ServiceMetadata;

import java.util.Map;

public class PushoverHelper {

    public static void sendPushOverMessage(String message) {
        try {
            PushoverClient client = new PushoverRestClient();
            if (!StringUtils.isEmpty(System.getenv("PushOver__AppKey")) && !StringUtils.isEmpty(System.getenv("PushOver__UserKey")))
                //For MVP - just a message, we can customize the message by adding https://support.pushover.net/i44-example-code-and-pushover-libraries#java
                client.pushMessage(PushoverMessage.builderWithApiToken(System.getenv("PushOver__AppKey"))
                        .setUserId(System.getenv("PushOver__UserKey"))
                        .setMessage(message)
                        .build());
        }catch (Exception e) {
            Log.error(e, "Error raising alert with pushover");
        }
    }

}
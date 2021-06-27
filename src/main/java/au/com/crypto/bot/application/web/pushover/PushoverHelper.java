package au.com.crypto.bot.application.web.pushover;

import io.advantageous.boon.core.Sys;
import org.springframework.util.StringUtils;
import serilogj.Log;

import java.util.Map;


public class PushoverHelper {

    public static void sendPushOverMessage(String message) {
        try {
            PushoverClient client = new PushoverRestClient();
            Map<String, String> map = System.getenv();
            for (Map.Entry <String, String> entry: map.entrySet()) {
                System.out.println("Variable Name:- " + entry.getKey() + " Value:- " + entry.getValue());
            }
            if (!StringUtils.isEmpty(System.getenv("Pushover__AppKey")) && !StringUtils.isEmpty(System.getenv("Pushover__UserKey"))) {
                //For MVP - just a message, we can customize the message by adding https://support.pushover.net/i44-example-code-and-pushover-libraries#java
                System.out.println("sending message "+ System.getenv("Pushover__AppKey") + "   " +System.getenv("Pushover__UserKey"));
                client.pushMessage(PushoverMessage.builderWithApiToken(System.getenv("PushOver__AppKey"))
                        .setUserId(System.getenv("PushOver__UserKey"))
                        .setMessage(message)
                        .build());
                System.out.println("sent message "+ System.getenv("Pushover__AppKey") + "   " +System.getenv("Pushover__UserKey"));

            } else
            {
                System.out.println("No env vars "+ System.getenv("Pushover__AppKey") + "   " +System.getenv("Pushover__UserKey"));
            }
        }catch (Exception e) {
            Log.error(e, "Error raising alert with pushover");
        }
    }

}

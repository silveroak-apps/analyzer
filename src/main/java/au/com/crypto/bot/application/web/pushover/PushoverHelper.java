package au.com.crypto.bot.application.web.pushover;

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
            if (!StringUtils.isEmpty(System.getenv("Pushover__AppKey")) && !StringUtils.isEmpty(System.getenv("Pushover__UserKey")))
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

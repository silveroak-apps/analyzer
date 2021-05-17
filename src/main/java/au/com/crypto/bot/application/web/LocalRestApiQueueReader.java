package au.com.crypto.bot.application.web;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.utils.LogUtil;
import au.com.crypto.bot.application.utils.PropertyUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import serilogj.Log;
import software.amazon.awssdk.services.sqs.model.Message;

import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1")
public class LocalRestApiQueueReader extends QueueReader {

    Map<String, String> props = PropertyUtil.getProperties();
    @Autowired
    private LocalRestApiQueueReader localRestApiQueueReader;


    /**
     * For health check
     *
     * @return the list
     */
    @GetMapping("/healthz")
    public String getTest() {
        return "OK";
    }

    /**
     * Will return json message up on success
     *
     * @param payload
     * @param response
     * @return
     * @throws URISyntaxException
     */
    @PostMapping(value = "/sendEvent", consumes = "application/json", produces = "application/json")
    public String processEvent(@RequestBody String payload, HttpServletResponse response)
            throws URISyntaxException {

        processMessage(ApplicationControllers.getInstance(), payload);
        return payload;
    }

    //Process requested event
    private String processMessage(ApplicationControllers ac, String payload) {

      try {
            JSONObject messageJson = new JSONObject(payload);
            int contracts = -1;
            if (messageJson.has("contracts")) {
                contracts = messageJson.getInt("contracts");
            }
            pushEvent(ac.getMarketEventController(),
                    messageJson.getString("name"),
                    messageJson.getDouble("price"),
                    messageJson.getString("symbol"),
                    messageJson.getString("market"),
                    messageJson.getInt("timeFrame"),
                    messageJson.getString("exchange"),
                    contracts,
                    messageJson.getString("category"),
                    new Date().getTime(),
                    messageJson.toString(), props);
            Log.information("{Application} Successfully processed event -> {EventMessage}", "LocalEventReader", payload);
        } catch (Exception e) {
            Log.error(e, "Error in processing message from queue {Payload} -> ", payload);
        }
        return "{\"message\": \"success\"}";
    }
}


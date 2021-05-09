package au.com.crypto.bot.application.web;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.AnalyzerApplication;
import au.com.crypto.bot.application.trade.EventCollector;
import au.com.crypto.bot.application.utils.LogUtil;
import au.com.crypto.bot.application.utils.PropertyUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serilogj.Log;
import serilogj.context.LogContext;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//Thread class to poll the SQS
public class PollAWSSQSService extends QueueReader {

    private ApplicationControllers ac;
    private static final Logger logger = LoggerFactory.getLogger(AnalyzerApplication.class);
    static String QUEUE_NAME = "signal_webhook_test";
    Map<String, String> props;


    private final ExecutorService executor
            = Executors.newSingleThreadExecutor();

    public void getMessages() {
        if (isQueueExists()) {
            executor.submit(() -> {
                while (true) {
                    getSQSMessages();
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        LogUtil.printLog(logger, LogUtil.STATUS.ERROR.name(), PollAWSSQSService.class.getSimpleName(), "Exception in executing sleep " + e);
                        Log.error(e, "Exception in executing sleep");
                    }
                }

            });
        }else {
            Log.warning("Specified queue isn't exist in AWS, system will use local queue");
        }

    }

    private boolean isQueueExists() {
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.AP_SOUTHEAST_2)
                .build();
        try {

            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(QUEUE_NAME)
                    .build();

            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
        } catch (QueueNameExistsException | QueueDoesNotExistException e) {
            Log.error(e, "Error in getting messages from queue, queue may not be exist");
            return false;
        }
        return true;
    }

    public PollAWSSQSService(ApplicationControllers ac, String queueName) {
        LogContext.pushProperty("Application", getClass().getSimpleName());
        props = PropertyUtil.getProperties();
        QUEUE_NAME = queueName;
        Log.information("New thread: started for {QueueName}", queueName);
        this.ac = ac;
        EventCollector.loadMarketEventsFromDB(ac);
    }

    public void getSQSMessages() {
        try {

            SqsClient sqsClient = SqsClient.builder()
                    .region(Region.AP_SOUTHEAST_2)
                    .build();
            try {

                GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                        .queueName(QUEUE_NAME)
                        .build();

                String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

                // Receive messages from the queue
                ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                        .maxNumberOfMessages(1)
                        .waitTimeSeconds(2)
                        .queueUrl(queueUrl)
                        .attributeNamesWithStrings("All")
                        .build();
                List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
                LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), PollAWSSQSService.class.getSimpleName(), "Number of messages in queue =  @{QueueLength}", messages.size());
                // Print out the messages
                for (Message m : messages) {
                    LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), PollAWSSQSService.class.getSimpleName(), "Message from AWS queue" + m.body());
                    processMessage(m);
                    LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), PollAWSSQSService.class.getSimpleName(), "Processing finished and deleting message -- " + m.messageId());
                    deleteMessageFromQueue(sqsClient, queueUrl, m);
                }
            } catch (QueueNameExistsException e) {
                Log.error(e, "Error in getting messages from queue, queue may not be exist");
                throw e;
            }
        } catch (Exception e) {
            LogUtil.printLog(logger, LogUtil.STATUS.ERROR.name(), PollAWSSQSService.class.getSimpleName(), "Exception in executing method" + e);
            Log.error(e, "Error in getting messages from AWS queue");
            e.printStackTrace();
        }
    }

    /**
     * "Symbol": "ETHUSDT",
     * "Trigger": "BUY",
     * "Market": "USDT"
     *
     * @param m
     */
    private void processMessage(Message m) {

        //long epoch = Long.parseLong(m.attributes().get(MessageSystemAttributeName.SENT_TIMESTAMP));
        String attrValue = extractAttribute(m, "SentTimestamp");
        long epoch = attrValue != null ? Long.parseLong(attrValue) : 0;
        //TODO: push event to dead letter queue
        try {
            JSONObject messageJson = new JSONObject(m.body());
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
                    epoch,
                    messageJson.toString());
        } catch (Exception e) {
            Log.error(e, "Error in processing message from queue -> ", m.messageId());
        }
    }


    private static String extractAttribute(Message message, String attributeName) {
        if (message.hasAttributes()) {
            Map<MessageSystemAttributeName, String> attributes = message.attributes();
            MessageSystemAttributeName msan = MessageSystemAttributeName.fromValue(attributeName);
            return attributes.get(msan);
        }
        return null;
    }

    public void deleteMessageFromQueue(SqsClient sqsClient, String queueUrl, Message message) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();
        sqsClient.deleteMessage(deleteMessageRequest);
    }

    public void run() {

    }

    /**
     * this is only for testing
     */
    public PollAWSSQSService() {

    }

}


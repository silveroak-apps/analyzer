package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.analyzer.entities.MarketEventController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serilogj.Log;

import java.util.List;

public class EventCollector {

    private static final Logger logger = LoggerFactory.getLogger(EventCollector.class);

    //TODO: need to filter old message
    public static void loadMarketEventsFromDB(ApplicationControllers ac) {
        MarketEventController mc = ac.getMarketEventController();
        Events events = Events.getInstance();
        List<MarketEvent> listOfMarketEvents = mc.findAllEventsLimitTwoHundred();
        events.addMarketEventsToQueue(listOfMarketEvents);
        Log.information("{Application} Successfully loaded all market events from DB, No of Events: "
                + listOfMarketEvents.size(), EventCollector.class.getSimpleName());
    }
}

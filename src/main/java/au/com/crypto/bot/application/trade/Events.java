package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.analyzer.entities.MarketEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class contains a collection of all the market events
 */
public class Events {

    private static Events signalCollector;
    //Synchronized market event queue
    CopyOnWriteArrayList<MarketEvent> marketEvents = new CopyOnWriteArrayList<MarketEvent>();

    public static Events getInstance()
    {
        if (signalCollector == null)
        {
            // if instance is null, initialize
            signalCollector = new Events();
        }
        return signalCollector;
    }

    private Events() {}

    public void addMarketEvent(MarketEvent me) {
        marketEvents.add(me);
    }
    public void addMarketEvents(List<MarketEvent> mes) {
        marketEvents.addAll(mes);
    }

    public CopyOnWriteArrayList<MarketEvent> getMarketEvents() {
        return marketEvents;
    }
}

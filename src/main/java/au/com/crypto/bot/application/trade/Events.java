package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.analyzer.entities.MarketEvent;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * This class contains a collection of all the market events
 */
public class Events {

    private static Events signalCollector;
    //Synchronized market event queue
    Queue<MarketEvent> queue = QueueUtils.synchronizedQueue(new CircularFifoQueue<MarketEvent>(250));
    
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

    public void addMarketEventsToQueue(List<MarketEvent> mes) {
        queue.addAll(mes);
    }

    public void addMarketEventToQueue(MarketEvent me) {
        queue.add(me);
    }

    public CopyOnWriteArrayList<MarketEvent> getMarketEvents() {
        return new CopyOnWriteArrayList<>(queue);
    }
}

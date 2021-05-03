package au.com.crypto.bot.application.web;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.analyzer.entities.MarketEventController;
import au.com.crypto.bot.application.trade.Events;
import serilogj.Log;

import java.math.BigDecimal;
import java.util.Date;

public class QueueReader {

    private ApplicationControllers ac;
    String SOURCE = "tradingView";


    protected void pushEvent(MarketEventController mec, String name, double price, String symbol, String market, long timeframe, String exchange, int contracts, String category, long epoch, String event) {
        MarketEvent marketEvent = new MarketEvent();
        marketEvent.setMessage(event);
        marketEvent.setMarket(market);
        marketEvent.setPrice(BigDecimal.valueOf(price));
        marketEvent.setSymbol(symbol);
        marketEvent.setTimeframe(timeframe);
        marketEvent.setEventTime(new Date(epoch));
        marketEvent.setSource(SOURCE);
        marketEvent.setCategory(category);
        marketEvent.setName(name);
        marketEvent.setContracts(contracts);
        Events.getInstance().addMarketEvent(marketEvent);
        String id = mec.save(marketEvent);
        Log.information("{@Application}  -> " + "Successfully saved event from {@MarketSource}" +
                        " for Symbol {@Symbol}, {@Timeframe}, {@Name} -> {@id}",
                "Analyzer", SOURCE,  symbol, timeframe, name, id);

    }
}

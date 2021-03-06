package au.com.crypto.bot.application.web;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analysis.ClosePositionAnalyzer;
import au.com.crypto.bot.application.analysis.OpenPositionAnalyzer;
import au.com.crypto.bot.application.analysis.SpotBuyAnalyzer;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.analyzer.entities.MarketEventController;
import au.com.crypto.bot.application.trade.Events;
import au.com.crypto.bot.application.trade.TraderImpl;
import au.com.crypto.bot.application.trade.Trader;
import serilogj.Log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class QueueReader {

    protected void pushEvent(ApplicationControllers ac, MarketEventController mec, String name, double price, String symbol, String market,
                             long timeframe, String exchange, double contracts, String category,
                             long epoch, String event, Map<String, String> props, String source) {
        MarketEvent marketEvent = new MarketEvent();
        marketEvent.setMessage(event);
        marketEvent.setMarket(market);
        marketEvent.setPrice(BigDecimal.valueOf(price));
        marketEvent.setSymbol(symbol);
        marketEvent.setTimeframe(timeframe);
        marketEvent.setEventTime(new Date(epoch));
        marketEvent.setSource(source);
        marketEvent.setCategory(category);
        marketEvent.setName(name);
        marketEvent.setContracts(contracts);
        marketEvent.setExchangeName(exchange);
        marketEvent.setExchangeId(ac.getExchange().findExchangeIdByName(exchange));
        Events.getInstance().addMarketEventToQueue(marketEvent);
        String id = mec.save(marketEvent);
        Log.information("{Application} - {Function} - {MarketEventId} " + "Successfully saved event from {MarketSource}" +
                        " for Symbol {Symbol}, {Name}, {Category} {Timeframe}",
                "Analyzer", "SaveEvent", id,source,  symbol, name, category, timeframe);
        analyzeStrategies(ac, props, marketEvent);

    }

    protected void analyzeStrategies(ApplicationControllers ac, Map<String, String> props, MarketEvent me) {
        //Analyze strategies
        String exchangeType = "";
        Trader trader = new TraderImpl();
        if (props.get("exchangeType") != null)
            exchangeType = props.get("exchangeType");
        if (exchangeType.equalsIgnoreCase(CONSTANTS._futures)) {
            OpenPositionAnalyzer.getInstance(ac, trader).run(me);
            ClosePositionAnalyzer.getInstance(ac, trader).run(me);
        } else if (exchangeType.equalsIgnoreCase(CONSTANTS._spot)) {
            SpotBuyAnalyzer.getInstance(ac, trader).run(me);
        } else if (exchangeType.equalsIgnoreCase("ALL")) {
            OpenPositionAnalyzer.getInstance(ac, trader).run(me);
            ClosePositionAnalyzer.getInstance(ac, trader).run(me);
            SpotBuyAnalyzer.getInstance(ac, trader).run(me);
        }
    }
}

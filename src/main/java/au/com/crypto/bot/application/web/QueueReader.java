package au.com.crypto.bot.application.web;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analysis.ClosePositionAnalyzer;
import au.com.crypto.bot.application.analysis.OpenPositionAnalyzer;
import au.com.crypto.bot.application.analysis.SpotBuyAnalyzer;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.analyzer.entities.MarketEventController;
import au.com.crypto.bot.application.trade.Events;
import au.com.crypto.bot.application.trade.FuturesTrader;
import au.com.crypto.bot.application.trade.Trader;
import serilogj.Log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class QueueReader {

    String SOURCE = "tradingView";


    protected void pushEvent(ApplicationControllers ac, MarketEventController mec, String name, double price, String symbol, String market,
                             long timeframe, String exchange, int contracts, String category,
                             long epoch, String event, Map<String, String> props) {
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
        analyzeStrategies(ac, props);

    }

    protected void analyzeStrategies(ApplicationControllers ac, Map<String, String> props) {
        //Analyze strategies
        String exchangeType = "";
        Trader trader = new FuturesTrader();
        if (props.get("exchangeType") != null)
            exchangeType = props.get("exchangeType");
        if (exchangeType.equalsIgnoreCase(CONSTANTS._futures)) {
            new OpenPositionAnalyzer(ac, trader).run();
            new ClosePositionAnalyzer(ac, trader).run();
        } else if (exchangeType.equalsIgnoreCase(CONSTANTS._spot)) {
            new SpotBuyAnalyzer(ac, trader).run();
        } else if (exchangeType.equalsIgnoreCase("ALL")) {
            new OpenPositionAnalyzer(ac, trader).run();
            new ClosePositionAnalyzer(ac, trader).run();
            new SpotBuyAnalyzer(ac, trader).run();
        }
    }
}

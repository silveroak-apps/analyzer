package au.com.crypto.bot.application.analysis;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.trade.Strategies;
import au.com.crypto.bot.application.trade.Trader;
import au.com.crypto.bot.application.utils.PropertyUtil;
import com.google.gson.Gson;
import serilogj.Log;
import serilogj.context.LogContext;

import java.util.*;

/**
 * For spot we use external sell analyzer
 */
public class SpotBuyAnalyzer extends StrategyAnalyzer {
    private final ApplicationControllers ac;
    Map<String, String> props;
    Trader trader;
    Gson gson = new Gson();
    private static SpotBuyAnalyzer spotBuyAnalyzer = null;
    private Set<String> processedEvents = new HashSet<String>();
    private SpotBuyAnalyzer(ApplicationControllers ac, Trader trader) {
        super(trader);
        props = PropertyUtil.getProperties();
        this.ac = ac;
        this.trader = trader;
    }

    public static SpotBuyAnalyzer getInstance(ApplicationControllers ac, Trader trader) {
        if (spotBuyAnalyzer == null)
        {
            spotBuyAnalyzer = new SpotBuyAnalyzer(ac, trader);
        }
        return spotBuyAnalyzer;
    }
    @Override
    public synchronized void run() {
        try {
            List<Map<String, Object>> positiveValues = analyzeStrategy(CONSTANTS._open_n_close);

            if (!positiveValues.isEmpty()) {
                for (Map<String, Object> match : positiveValues) {
                    MarketEvent marketEvent = (MarketEvent) match.get(CONSTANTS._marketEvent);
                    Strategies.ConditionsGroup conditionsGroup = (Strategies.ConditionsGroup) match.get(CONSTANTS._strategy);
                    Strategies.Strategy sp = (Strategies.Strategy) match.get(CONSTANTS._strategy_pair);
                    String key = marketEvent.getId()+"_"+ conditionsGroup.getConditionsName()+"_"+ sp.getStrategyId();

                    LogContext.pushProperty("MarketEventName", marketEvent.getName());
                    LogContext.pushProperty("StrategyPairName", sp.getStrategyName());
                    LogContext.pushProperty("StrategyName", conditionsGroup.getConditionsName());
                    String symbol = sp.getSymbol();
                    Log.information("Found a match for {Symbol} - {PositionType}- {MarketEvent} and {Strategy}}",
                            symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                    if (!processedEvents.contains(key)) {
                        trader.raiseSignal(ac, 0L, marketEvent.getPrice().doubleValue(), symbol,
                                CONSTANTS._open, sp.getPositionType(), conditionsGroup, sp.getStrategyName(),
                                props, marketEvent.getMarket(), marketEvent.getContracts(), marketEvent.getExchangeId(), marketEvent.getId(),
                                marketEvent.getExchangeName());
                        processedEvents.add(key);
                    }

                }
            }
        } catch (Exception e) {
            Log.error(e, "{@Application} Error occurred in @{class}: ", "Analyzer",
                    OpenPositionAnalyzer.class.getSimpleName(), e.getMessage());
        }
    }
}

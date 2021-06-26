package au.com.crypto.bot.application.analysis;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.FuturesSignal;
import au.com.crypto.bot.application.analyzer.entities.FuturesSignalController;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.trade.Events;
import au.com.crypto.bot.application.trade.Strategies;
import au.com.crypto.bot.application.trade.Trader;
import au.com.crypto.bot.application.utils.PropertyUtil;
import com.google.gson.Gson;
import serilogj.Log;
import serilogj.context.LogContext;

import java.util.*;

public class OpenPositionAnalyzer extends StrategyAnalyzer {
    private static ApplicationControllers ac = null;
    Map<String, String> props;
    Trader trader = null;
    Gson gson = new Gson();

    private Set<String> processedEvents = new HashSet<String>();
    private static OpenPositionAnalyzer openPositionAnalyzer = null;

    private OpenPositionAnalyzer(ApplicationControllers ac, Trader trader) {
        super(trader);
        props = PropertyUtil.getProperties();
        this.ac = ac;
        this.trader = trader;
    }

    public static OpenPositionAnalyzer getInstance(ApplicationControllers ac, Trader trader) {
        if (openPositionAnalyzer == null)
        {
            openPositionAnalyzer = new OpenPositionAnalyzer(ac, trader);
        }
        return openPositionAnalyzer;
    }
    @Override
    public synchronized void run(MarketEvent marketEvent) {
        try {

            List<Map<String, Object>> positiveSignalMatches = analyzeStrategy(CONSTANTS._open, marketEvent);
            if (!positiveSignalMatches.isEmpty()) {
                for (Map<String, Object> match : positiveSignalMatches) {
                    try {
                        Strategies.ConditionsGroup conditionsGroup = (Strategies.ConditionsGroup) match.get(CONSTANTS._strategy);
                        Strategies.Strategy sp = (Strategies.Strategy) match.get(CONSTANTS._strategy_pair);
                        String key = marketEvent.getId()+"_"+ conditionsGroup.getConditionsName()+"_"+ sp.getStrategyId();
                            String symbol = sp.getSymbol();
                            Log.information("{Application} - {Function} - {MarketEventId} - Found a match for " +
                                            "{Symbol} - {PositionType} MarketEvent - {MarketEvent} and {Strategy} " +
                                            "isMarketEventProcessed - {isMarketEventProcessed} - {StrategyKey}- Ready to place an order",
                                    "Analyzer", "OpenPositionAnalyzer", marketEvent.getId(), symbol,
                                    sp.getPositionType(), marketEvent, marketEvent.getId(), conditionsGroup,
                                    processedEvents.contains(key), key);

                            var openSignals = getOpenSignalBySymbol(symbol,
                                    sp.getPositionType(), marketEvent.getExchangeId());
                            if (sp.getExchangeId() == marketEvent.getExchangeId() && sp.getSymbol().equalsIgnoreCase(marketEvent.getSymbol())) {
                                if (!processedEvents.contains(key)) {
                                    if (openSignals.isEmpty()) {
                                        trader.raiseSignal(ac, 0L, marketEvent.getPrice().doubleValue(), symbol,
                                                CONSTANTS._open, sp.getPositionType(), conditionsGroup, sp.getStrategyName(),
                                                props, marketEvent.getMarket(), marketEvent.getContracts(), marketEvent.getExchangeId(), marketEvent.getId(), marketEvent.getExchangeName());
                                        Log.information("{Application} - {Function} - {MarketEventId} - No Open Signals on this symbol {Symbol} for market event {MarketEvent} Id: {MarketEventId} and raised a new signal",
                                                "Analyzer", "OpenPositionAnalyzer", marketEvent.getId(), symbol, marketEvent, marketEvent.getId());
                                        processedEvents.add(key);
                                    } else {
                                        Log.information("{Application} - {Function} - {MarketEventId} Found an active / created / unknown signal - {Strategy}" +
                                                        "Not placing any new command" +
                                                        "--- Signal Status {SignalStatus}" +
                                                        "--- Position Status {PositionStatus}" +
                                                        "--- Symbol {Symbol}" +
                                                        "--- Position Type {PositionType}" +
                                                        "--- Signal Id {SignalId}" +
                                                        "--- Updated time {UpdatedTime}" +
                                                        "--- Created time {CreatedTime}" +
                                                        "--- Market event id {MarketEventId}",
                                                "Analyzer", "OpenPositionAnalyzer", marketEvent.getId()
                                                , conditionsGroup.getConditionsName()
                                                , openSignals.get(0).getSignalStatus()
                                                , openSignals.get(0).getPositionStatus()
                                                , openSignals.get(0).getSymbol()
                                                , openSignals.get(0).getPositionType()
                                                , openSignals.get(0).getSignalId()
                                                , openSignals.get(0).getUpdatedDateTime()
                                                , openSignals.get(0).getCreatedDateTime()
                                                , marketEvent.getId()
                                        );
                                    }
                                } else {
                                    Log.information("{Application} - {Function} - {MarketEventId} - Event already processed {Symbol} for the {Event} and id {Id}",
                                            "Analyzer", "OpenPositionAnalyzer", marketEvent.getId(), symbol, marketEvent.getName(), marketEvent.getId());
                                }
                            } else {
                                Log.information("{Application} - {Function} - {MarketEventId} - Not placing any signal command as the event doesn't belong to " +
                                                "Exchange {Exchange} and Symbol {Symbol} in strategy - {@Strategy}",
                                        "Analyzer", "OpenPositionAnalyzer", marketEvent.getId(), sp.getExchangeId(), symbol, sp);
                            }
                    } catch (Exception e) {
                        Log.error(e, "{Application} - {Function} - {MarketEventId} Error occurred: in placing a signal ", "Analyzer", "OpenPositionAnalyzer", marketEvent.getId(), e.getMessage());
                    }
                }
            } else {
                Log.information("{Application} - {Function} - {MarketEventId} No positive matches found for active strategies and market events ", "Analyzer", "OpenPositionAnalyzer", marketEvent.getId());
            }
        } catch (Exception e) {
            Log.error(e, "{Application} - {Function} - {MarketEventId} Error occurred in @{class}: ", "Analyzer", "OpenPositionAnalyzer", marketEvent.getId(),
                    OpenPositionAnalyzer.class.getSimpleName(), e.getMessage());
        }
    }

    private static List<FuturesSignal> getOpenSignalBySymbol(String symbol, String positionType, long exchangeId) {
        FuturesSignalController futuresSignalController = ac.getFuturesSignalController();
        return futuresSignalController.findActiveSignals(symbol, exchangeId, positionType);
    }

    /**
     * This is for testing only
     */
    // public OpenPositionAnalyzer(List<Strategies.Strategy> strategies) {
//        super(strategies);
//    }
}

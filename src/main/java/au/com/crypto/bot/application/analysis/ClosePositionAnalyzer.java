package au.com.crypto.bot.application.analysis;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.FuturesSignal;
import au.com.crypto.bot.application.analyzer.entities.FuturesSignalController;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.trade.Strategies;
import au.com.crypto.bot.application.trade.Trader;
import au.com.crypto.bot.application.utils.PropertyUtil;
import com.google.gson.Gson;
import serilogj.Log;
import serilogj.context.LogContext;

import java.util.*;

public class ClosePositionAnalyzer extends StrategyAnalyzer {
    private ApplicationControllers ac;
    Map<String, String> props;
    Trader trader = null;
    private static ClosePositionAnalyzer closePositionAnalyzer = null;
    Gson gson = new Gson();
    private Set<String> processedEvents = new HashSet<String>();

    private ClosePositionAnalyzer(ApplicationControllers ac, Trader trader) {
        super(trader);
        props = PropertyUtil.getProperties();
        this.ac = ac;
        this.trader = trader;
    }

    public static ClosePositionAnalyzer getInstance(ApplicationControllers ac, Trader trader) {
        if (closePositionAnalyzer == null)
        {
            closePositionAnalyzer = new ClosePositionAnalyzer(ac, trader);
        }
        return closePositionAnalyzer;
    }

    @Override
    public synchronized void run() {
        try {
            List<Map<String, Object>> positiveValues = analyzeStrategy(CONSTANTS._close);
            if (!positiveValues.isEmpty()) {
                Log.information("{Class} - Found {Size} matches for the event", "ClosePositionAnalyzer", positiveValues.size());
                for (Map<String, Object> match : positiveValues) {
                    try {
                        MarketEvent marketEvent = (MarketEvent) match.get(CONSTANTS._marketEvent);
                        Strategies.ConditionsGroup conditionsGroup = (Strategies.ConditionsGroup) match.get(CONSTANTS._strategy);
                        Strategies.Strategy sp = (Strategies.Strategy) match.get(CONSTANTS._strategy_pair);
                        String key = marketEvent.getId()+"_"+ conditionsGroup.getConditionsName()+"_"+ sp.getStrategyId();
                        LogContext.pushProperty("MarketEventName", marketEvent.getName());
                        LogContext.pushProperty("StrategyPairName", sp.getStrategyName());
                        LogContext.pushProperty("StrategyName", conditionsGroup.getConditionsName());
                        LogContext.pushProperty("Application Name", getClass().getSimpleName());
                        String symbol = sp.getSymbol();
                        Log.information("{Class} - Found a match for {Symbol} - {PositionType}- {MarketEvent}, market event id - {MarketEventId} {Strategy} " +
                                        "- isMarketEvent Processed {isMarketEvent} and {StrategyKey}",
                                "ClosePositionAnalyzer", symbol, sp.getPositionType(), marketEvent, marketEvent.getId(), gson.toJson(conditionsGroup),
                                processedEvents.contains(key), key);

                        var openSignals = getOpenSignalBySymbolWithPositionStatus(symbol,
                                sp.getPositionType(), marketEvent.getExchangeId());
                        Log.information("{Class} - Checking for active signals - ActiveSignalsSize - {Size}",
                                "ClosePositionAnalyzer", openSignals.size());
                        if (!processedEvents.contains(key)) {
                            if (!openSignals.isEmpty()) {
                                //Assuming system will have one signal per symbol
                                FuturesSignal fs = openSignals.get(0);
                                trader.raiseSignal(ac, fs.getSignalId(), marketEvent.getPrice().doubleValue(), symbol,
                                        CONSTANTS._close, sp.getPositionType(), conditionsGroup, sp.getStrategyName(),
                                        props, marketEvent.getMarket(), marketEvent.getContracts(), marketEvent.getExchangeId(), marketEvent.getId());
                                Log.information("{Class} - {Application} Found an existing signal raising a new command close Strategy - {Strategy}" +
                                                "--- Signal Status {SignalStatus}" +
                                                "--- Position Status {PositionStatus}" +
                                                "--- Symbol {Symbol}" +
                                                "--- Position Type {PositionType}" +
                                                "--- Position Size {PositionSize}" +
                                                "--- Signal Id {SignalId}" +
                                                "--- Updated time {UpdatedTime}" +
                                                "--- Created time {CreatedTime}",
                                        "ClosePositionAnalyzer", "Analyzer", conditionsGroup.getConditionsName()
                                        , openSignals.get(0).getSignalStatus()
                                        , openSignals.get(0).getPositionStatus()
                                        , openSignals.get(0).getSymbol()
                                        , openSignals.get(0).getPositionType()
                                        , openSignals.get(0).getPositionSize()
                                        , openSignals.get(0).getSignalId()
                                        , openSignals.get(0).getUpdatedDateTime()
                                        , openSignals.get(0).getCreatedDateTime()
                                );
                            } else {
                                Log.information("{Class}  - Not placing any command for {Symbol} - {PositionType}- {MarketEvent} and {Strategy} as there are no active positions in the system ",
                                        "ClosePositionAnalyzer", symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                            }
                            processedEvents.add(key);
                        } else {
                            Log.information("{Class}  - Not placing any command for {Symbol} - {PositionType}- {MarketEvent} and {Strategy} as the market event is already processed ",
                                    "ClosePositionAnalyzer", symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                        }

                    } catch (Exception e) {
                        Log.error(e, "{Class}  - {Application} Error occurred in {class}: in placing a signal ", "Analyzer",
                                "ClosePositionAnalyzer", ClosePositionAnalyzer.class.getSimpleName(), e.getMessage());
                    }
                }
            } else {
                Log.information("{Class} - {Application} No positive matches found for active strategies and market events ", "ClosePositionAnalyzer", this.getClass().getSimpleName());
            }
            FuturesSignalController fsController = ac.getFuturesSignalController();
            //TODO: need to do trailing stop loss
//            List<FuturesSignal> futuresSignals = fsController.findAllActiveSignalsByStrategy(CONSTANTS._binance_exchange_futures);
//            if (futuresSignals != null && !futuresSignals.isEmpty()) {
//
//            }
        } catch (Exception e) {
            Log.error(e, "{Class} - {@Application} Error occurred in {class}: ", "ClosePositionAnalyzer", "Analyzer",
                    ClosePositionAnalyzer.class.getSimpleName(), e.getMessage());
        }
    }

    private List<FuturesSignal> getOpenSignalBySymbolWithPositionStatus(String symbol, String positionType, long exchangeId) {
        FuturesSignalController futuresSignalController = ac.getFuturesSignalController();
        return futuresSignalController.findActiveSignalsWithPosition(symbol, exchangeId, positionType);
    }
}

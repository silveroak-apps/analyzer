package au.com.crypto.bot.application.analysis;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.Signal;
import au.com.crypto.bot.application.analyzer.entities.SignalController;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.trade.Strategies;
import au.com.crypto.bot.application.trade.Trader;
import au.com.crypto.bot.application.utils.PropertyUtil;
import com.google.gson.Gson;
import serilogj.Log;

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
    public synchronized void run(MarketEvent marketEvent) {
        try {
            List<Map<String, Object>> strategyMatches = analyzeStrategy(CONSTANTS._close, marketEvent);
            if (!strategyMatches.isEmpty()) {
                Log.information("{Application} - {Function} - {MarketEventId}" +
                        "Found {Size} matches for the event", "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(),
                        strategyMatches.size());
                for (Map<String, Object> match : strategyMatches) {
                    try {
                        Strategies.ConditionsGroup conditionsGroup = (Strategies.ConditionsGroup) match.get(CONSTANTS._strategy);
                        Strategies.Strategy sp = (Strategies.Strategy) match.get(CONSTANTS._strategy_pair);
                        String key = marketEvent.getId()+"_"+ conditionsGroup.getConditionsName()+"_"+ sp.getStrategyId();
                        String symbol = sp.getSymbol();
                        Log.information("{Application} - {Function} - {MarketEventId} - Found a match for {Symbol}" +
                                        " - {PositionType}- {MarketEvent}, market event id - {Strategy} " +
                                        "- isMarketEvent Processed {isMarketEvent} and {StrategyKey}",
                                "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(),
                                symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup),
                                processedEvents.contains(key), key);

                        var openSignals = getOpenSignalBySymbolWithPositionStatus(symbol,
                                sp.getPositionType(), marketEvent.getExchangeId());
                        Log.information("{Application} - {Function} - {MarketEventId} - {Size} active Signals found ",
                                "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(), openSignals.size());
                        if (!openSignals.isEmpty()
                                && !isAnyActiveCommandForSymbol(symbol, marketEvent.getExchangeId(), sp.getPositionType())) {
                            if (!processedEvents.contains(key)) {
                                if (sp.getExchangeId() == marketEvent.getExchangeId() && sp.getSymbol().equalsIgnoreCase(marketEvent.getSymbol())) {
                                    //Assuming system will have one signal per symbol in one direction
                                    Signal fs = openSignals.get(0);
                                    trader.raiseSignal(ac, fs.getSignalId(), marketEvent.getPrice().doubleValue(), symbol,
                                            CONSTANTS._close, sp.getPositionType(), conditionsGroup, sp.getStrategyName(),
                                            props, marketEvent.getMarket(), marketEvent.getContracts(), marketEvent.getExchangeId(), marketEvent.getId(), marketEvent.getExchangeName(), sp);
                                    Log.information("{Application} - {Function} - {MarketEventId} Found an existing signal to close, raising a new command to close on Strategy - {Strategy}" +
                                                    "--- Signal Status {SignalStatus}" +
                                                    "--- Position Status {PositionStatus}" +
                                                    "--- Symbol {Symbol}" +
                                                    "--- Position Type {PositionType}" +
                                                    "--- Signal Id {SignalId}" +
                                                    "--- Updated time {UpdatedTime}" +
                                                    "--- Created time {CreatedTime}",
                                            "Analyzer", "ClosePositionAnalyzer", marketEvent.getId()
                                            , conditionsGroup.getConditionsName()
                                            , openSignals.get(0).getSignalStatus()
                                            , openSignals.get(0).getPositionStatus()
                                            , openSignals.get(0).getSymbol()
                                            , openSignals.get(0).getPositionType()
                                            , openSignals.get(0).getSignalId()
                                            , openSignals.get(0).getUpdatedDateTime()
                                            , openSignals.get(0).getCreatedDateTime()
                                    );
                                } else {
                                    Log.information("{Application} - {Function} - {MarketEventId} - Not placing any signal command as this market event doesn't belong to " +
                                                    "Exchange {Exchange} and Symbol {Symbol} in strategy in strategy - {Strategy}",
                                            "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(), marketEvent.getExchangeId(), symbol, sp);

                                }
                                processedEvents.add(key);
                            } else {
                                Log.information("{Application} - {Function} - {MarketEventId} - Not placing any command for {Symbol} - {PositionType}- {MarketEvent} and {Strategy} as the market event is already processed ",
                                        "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(), symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                            }
                        } else {
                            Log.information("{Application} - {Function} - {MarketEventId} - Not placing any command for {Symbol} - {PositionType}- {MarketEvent} and {Strategy} as there are no active positions in the system",
                                    "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(), symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                        }

                    } catch (Exception e) {
                        Log.error(e, "{Application} - {Function} - {MarketEventId} Error occurred in {class}: in placing a signal ", "Analyzer",
                                "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(), ClosePositionAnalyzer.class.getSimpleName(), e.getMessage());
                    }
                }
            } else {
                Log.information("{Application} - {Function} - {MarketEventId} No positive matches found for active strategies and market events ",
                        "Analyzer", "ClosePositionAnalyzer", marketEvent.getId());
            }
            SignalController fsController = ac.getFuturesSignalController();
            //TODO: need to do trailing stop loss
//            List<FuturesSignal> futuresSignals = fsController.findAllActiveSignalsByStrategy(CONSTANTS._binance_exchange_futures);
//            if (futuresSignals != null && !futuresSignals.isEmpty()) {
//
//            }
        } catch (Exception e) {
            Log.error(e, "{Application} - {Function} - {MarketEventId} Error occurred in {class}: ",
                    "Analyzer", "ClosePositionAnalyzer", marketEvent.getId(), ClosePositionAnalyzer.class.getSimpleName(), e.getMessage());
        }
    }

    private List<Signal> getOpenSignalBySymbolWithPositionStatus(String symbol, String positionType, long exchangeId) {
        SignalController futuresSignalController = ac.getFuturesSignalController();
        return futuresSignalController.findActiveSignalsWithPosition(symbol, exchangeId, positionType);
    }

    private boolean isAnyActiveCommandForSymbol(String symbol, long exchangeId, String positionType) {
        SignalController futuresSignalController = ac.getFuturesSignalController();
        return futuresSignalController.isAnyActiveCommandForSymbol(symbol, exchangeId, positionType);
    }
}

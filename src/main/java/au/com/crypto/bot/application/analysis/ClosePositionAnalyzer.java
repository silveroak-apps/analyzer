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
import org.hibernate.boot.SchemaAutoTooling;
import serilogj.Log;
import serilogj.context.LogContext;

import java.util.*;

public class ClosePositionAnalyzer extends StrategyAnalyzer {
    private ApplicationControllers ac;
    Map<String, String> props;
    Trader trader = null;
    Gson gson = new Gson();
    private Set<Long> processedEvents = new HashSet();

    public ClosePositionAnalyzer(ApplicationControllers ac, Trader trader) {
        super(trader);
        props = PropertyUtil.getProperties();
        this.ac = ac;
        this.trader = trader;
    }


    @Override
    public void run() {
        try {
            List<Map<String, Object>> positiveValues = analyzeStrategy(CONSTANTS._close);
            if (!positiveValues.isEmpty()) {
                for (Map<String, Object> match : positiveValues) {
                    try {
                        MarketEvent marketEvent = (MarketEvent) match.get(CONSTANTS._marketEvent);
                        Strategies.ConditionsGroup conditionsGroup = (Strategies.ConditionsGroup) match.get(CONSTANTS._strategy);
                        Strategies.Strategy sp = (Strategies.Strategy) match.get(CONSTANTS._strategy_pair);
                        LogContext.pushProperty("MarketEventName", marketEvent.getName());
                        LogContext.pushProperty("StrategyPairName", sp.getStrategyName());
                        LogContext.pushProperty("StrategyName", conditionsGroup.getConditionsName());
                        LogContext.pushProperty("Application Name", getClass().getSimpleName());
                        String symbol = sp.getSymbol();
                        Log.information("{Class} - Found a match for {Symbol} - {PositionType}- {MarketEvent}, market event id - {MarketEventId} {Strategy} " +
                                        "- isMarketEvent Processed {isMarketEvent}",
                                "ClosePositionAnalyzer", symbol, sp.getPositionType(), marketEvent, marketEvent.getId(), gson.toJson(conditionsGroup),
                                processedEvents.contains(marketEvent.getId()));

                        var openSignals = getOpenSignalBySymbolWithPositionStatus(symbol,
                                sp.getPositionType());
                        Log.information("{Class} - Checking for active signals - ActiveSignalsSize - {Size}",
                                "ClosePositionAnalyzer", openSignals.size());
                        if (!processedEvents.contains(marketEvent.getId())) {
                            if (!openSignals.isEmpty()) {
                                //Assuming system will have one signal per symbol
                                FuturesSignal fs = openSignals.get(0);
                                trader.raiseSignal(ac, fs.getSignalId(), marketEvent.getPrice().doubleValue(), symbol,
                                        CONSTANTS._close, sp.getPositionType(), conditionsGroup, sp.getStrategyName(),
                                        props, marketEvent.getMarket(), marketEvent.getContracts());
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
                                processedEvents.add(marketEvent.getId());
                            } else {
                                Log.information("{Class}  - Not placing any command for {Symbol} - {PositionType}- {MarketEvent} and {Strategy} as there are no active positions in the system ",
                                        "ClosePositionAnalyzer", symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                            }
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
            List<FuturesSignal> futuresSignals = fsController.findAllActiveSignalsByStrategy(CONSTANTS._binance_exchange_futures);
            if (futuresSignals != null && !futuresSignals.isEmpty()) {
                //TODO: need to do trailing stop loss
            }
        } catch (Exception e) {
            Log.error(e, "{Class} - {@Application} Error occurred in {class}: ", "ClosePositionAnalyzer", "Analyzer",
                    OpenPositionAnalyzer.class.getSimpleName(), e.getMessage());
        }
    }

    private List<FuturesSignal> getOpenSignalBySymbolWithPositionStatus(String symbol, String positionType) {
        FuturesSignalController futuresSignalController = ac.getFuturesSignalController();
        return futuresSignalController.findActiveSignalsWithPosition(symbol, CONSTANTS._binance_exchange_futures, positionType);
    }
}

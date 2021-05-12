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
import io.advantageous.boon.core.Sys;
import org.springframework.util.CollectionUtils;
import serilogj.Log;
import serilogj.context.LogContext;

import java.util.*;

public class OpenPositionAnalyzer extends StrategyAnalyzer implements Runnable {
    private ApplicationControllers ac = null;
    Thread openPositionAnalyzer;
    Map<String, String> props;
    Trader trader = null;
    Gson gson = new Gson();

    private Map<String, Date> lastSignal = new HashMap<>();
    private Set<Long> processedEvents = new HashSet();

    public OpenPositionAnalyzer(ApplicationControllers ac, Trader trader) {
        super(trader);
        openPositionAnalyzer = new Thread(this);
        props = PropertyUtil.getProperties();
        this.ac = ac;
        this.trader = trader;
        openPositionAnalyzer.start();
    }

    @Override
    public void run() {

        try {
            while (true) {
                List<Map<String, Object>> positiveSignalMatches = analyzeStrategy(CONSTANTS._open);
                if (!positiveSignalMatches.isEmpty()) {
                    for (Map<String, Object> match : positiveSignalMatches) {
                        try {
                            MarketEvent marketEvent = (MarketEvent) match.get(CONSTANTS._marketEvent);
                            Strategies.ConditionsGroup conditionsGroup = (Strategies.ConditionsGroup) match.get(CONSTANTS._strategy);
                            Strategies.Strategy sp = (Strategies.Strategy) match.get(CONSTANTS._strategy_pair);
                            try (var me  = LogContext.pushProperty("MarketEventName", marketEvent.getName())) {
                            try (var spn = LogContext.pushProperty("StrategyPairName", sp.getStrategyName())) {  
                            try (var sn  = LogContext.pushProperty("StrategyName", conditionsGroup.getConditionsName())) {
                            try (var an  = LogContext.pushProperty("Application", getClass().getSimpleName())) {
                                String symbol = sp.getSymbol();
                                Log.information("Found a match for {Symbol} - {PositionType}- MarketEvent  {MarketEvent} - {MarketEventId} and {Strategy}",
                                        symbol, sp.getPositionType(), marketEvent, marketEvent.getId(), conditionsGroup);

                                var openSignals = getOpenSignalBySymbol(symbol,
                                        sp.getPositionType());

                                //Adding processed symbol to map
                                lastSignal.put(symbol, new Date());
                                Log.information("Processing event - checking for is event already processed - {EventProcessed} for eventId {EventId}",
                                        processedEvents.contains(marketEvent.getId()), marketEvent.getId());

                                if (openSignals.isEmpty() && !processedEvents.contains(marketEvent.getId())) {
                                    trader.raiseSignal(ac, 0L, marketEvent.getPrice().doubleValue(), symbol,
                                            CONSTANTS._open, sp.getPositionType(), conditionsGroup, sp.getStrategyName(),
                                            props, marketEvent.getMarket(), marketEvent.getContracts());
                                    Log.information("No Open Signals on this symbol {Symbol} for market event {MarketEvent} (Id: {MarketEventId}) and raised a new signal",
                                            symbol, marketEvent, marketEvent.getId());

                                    // Adding processed event to avoid duplicated entries
                                    Log.information("Adding market event {EventId} to processed events.", marketEvent.getId());
                                    processedEvents.add(marketEvent.getId());
                                } else if (!openSignals.isEmpty()) {
                                    Log.information("{Application} Found an active / created / unknown signal - {Strategy}" +
                                                    "--- Signal Status {SignalStatus}" +
                                                    "--- Position Status {PositionStatus}" +
                                                    "--- Symbol {Symbol}" +
                                                    "--- Position Type {PositionType}" +
                                                    "--- Position Size {PositionSize}" +
                                                    "--- Signal Id {SignalId}" +
                                                    "--- Updated time {UpdatedTime}" +
                                                    "--- Created time {CreatedTime}",
                                            getClass().getSimpleName(), conditionsGroup.getConditionsName()
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
                                    Log.information("Event already processed {Symbol} for the {Event} and id {Id}", symbol, marketEvent.getName(), marketEvent.getId());
                                }
                            }}}} // try-with-resources: LogContext
                        } catch (Exception e) {
                            Log.error(e, "{Application} Error occurred in {class}: in placing a signal ", "Analyzer",
                                    OpenPositionAnalyzer.class.getSimpleName(), e.getMessage());
                        }
                    }
                } else {
                    Log.information("{Application} No positive matches found for active strategies and market events ", this.getClass().getSimpleName());
                }
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            Log.error(e, "{@Application} Error occurred in @{class}: ", "Analyzer",
                    OpenPositionAnalyzer.class.getSimpleName(), e.getMessage());
        }
    }

    private List<FuturesSignal> getOpenSignalBySymbol(String symbol, String positionType) {
        FuturesSignalController futuresSignalController = ac.getFuturesSignalController();
        return futuresSignalController.findActiveSignals(symbol, CONSTANTS._binance_exchange_futures, positionType);
    }

    /**
     * This is for testing only
     */
    // public OpenPositionAnalyzer(List<Strategies.Strategy> strategies) {
//        super(strategies);
//    }
}

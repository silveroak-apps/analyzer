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

public class ClosePositionAnalyzer extends StrategyAnalyzer implements Runnable {
    private ApplicationControllers ac;
    Thread closePositionAnalyzer;
    Map<String, String> props;
    Trader trader = null;
    Gson gson = new Gson();
    private Set<Long> processedEvents = new HashSet();

    public ClosePositionAnalyzer(ApplicationControllers ac, Trader trader) {
        super(trader);
        closePositionAnalyzer = new Thread(this);
        props = PropertyUtil.getProperties();
        this.ac = ac;
        this.trader = trader;
        closePositionAnalyzer.start();
    }


    @Override
    public void run() {

        try {
            while (true) {
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
                            Log.information("ClosePositionAnalyzer - Found a match for {Symbol} - {PositionType}- {MarketEvent} and {Strategy}",
                                    symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                            var openSignals = getOpenSignalBySymbolWithPositionStatus(symbol,
                                    sp.getPositionType());
                            if (!openSignals.isEmpty() && !processedEvents.contains(marketEvent.getId()))  {
                                //Assuming system will have one signal per symbol
                                FuturesSignal fs = openSignals.get(0);
                                trader.raiseSignal(ac, fs.getSignalId(), marketEvent.getPrice().doubleValue(), symbol,
                                        CONSTANTS._close, sp.getPositionType(), conditionsGroup, sp.getStrategyName(),
                                        props, marketEvent.getMarket(), marketEvent.getContracts());
                                Log.information("ClosePositionAnalyzer - {Application} Found an existing signal raising a new command close Strategy - {Strategy}" +
                                                "--- Signal Status {SignalStatus}" +
                                                "--- Position Status {PositionStatus}" +
                                                "--- Symbol {Symbol}" +
                                                "--- Position Type {PositionType}" +
                                                "--- Position Size {PositionSize}" +
                                                "--- Signal Id {SignalId}" +
                                                "--- Updated time {UpdatedTime}" +
                                                "--- Created time {CreatedTime}",
                                        "Analyzer", conditionsGroup.getConditionsName()
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
                                Log.information("ClosePositionAnalyzer - Not placing any command for {Symbol} - {PositionType}- {MarketEvent} and {Strategy} as the market event is already processed ",
                                        symbol, sp.getPositionType(), marketEvent, gson.toJson(conditionsGroup));
                            }
                        } catch (Exception e) {
                            Log.error(e, "ClosePositionAnalyzer - {Application} Error occurred in {class}: in placing a signal ", "Analyzer",
                                    OpenPositionAnalyzer.class.getSimpleName(), e.getMessage());
                        }
                    }
                } else {
                    Log.information("ClosePositionAnalyzer - {Application} No positive matches found for active strategies and market events ", this.getClass().getSimpleName());
                }
                FuturesSignalController fsController = ac.getFuturesSignalController();
                List<FuturesSignal> futuresSignals = fsController.findAllActiveSignalsByStrategy(CONSTANTS._binance_exchange_futures);
                if (futuresSignals != null && !futuresSignals.isEmpty()) {
                    //TODO: need to do trailing stop loss
                }

                Thread.sleep(2000);
            }
        } catch (Exception e) {
            Log.error(e, "ClosePositionAnalyzer - {@Application} Error occurred in {class}: ", "Analyzer",
                    OpenPositionAnalyzer.class.getSimpleName(), e.getMessage());
        }
    }

    private List<FuturesSignal> getOpenSignalBySymbolWithPositionStatus(String symbol, String positionType) {
        FuturesSignalController futuresSignalController = ac.getFuturesSignalController();
        return futuresSignalController.findActiveSignalsWithPosition(symbol, CONSTANTS._binance_exchange_futures, positionType);
    }
}

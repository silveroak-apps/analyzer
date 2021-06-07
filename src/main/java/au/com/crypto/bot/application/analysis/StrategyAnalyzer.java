package au.com.crypto.bot.application.analysis;

import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.trade.*;
import au.com.crypto.bot.application.utils.LoadStrategies;
import serilogj.Log;
import serilogj.context.LogContext;

import java.util.*;
import java.util.stream.Collectors;

public abstract class StrategyAnalyzer {

    Trader trader;

    StrategyAnalyzer(Trader trader) {
        Log.information("Base Class loaded {Application}: ", getClass().getSimpleName());
        this.trader = trader;
    }

    public List<Map<String, Object>> analyzeStrategy(String strategyType)  {
        Strategies strategies = LoadStrategies.getInstance().getStrategies();
        List<Map<String, Object>> strategyMatchList = new ArrayList<>();
        for (Strategies.Strategy sp : strategies.getStrategies()) {
            LogContext.pushProperty("Strategy", sp.getStrategyName());
            LogContext.pushProperty("Application", "Analyzer");
            //For Open Strategies - Open and Close is for spot
            if (strategyType.equalsIgnoreCase(CONSTANTS._open) || strategyType.equalsIgnoreCase(CONSTANTS._open_n_close)) {
                sp.getOpenConditionGroups().iterator()
                        .forEachRemaining(s -> {
                                    Map<String, Object> strategyMatch = new HashMap<>();
                                    checkStrategy(s, strategyMatch);
                                    if (!strategyMatch.isEmpty()) {
                                        strategyMatchList.add(strategyMatch);
                                        strategyMatch.put(CONSTANTS._strategy_pair, sp);
                                    }
                                }
                        );
            }

            //For Close Strategies - Open and Close is for spot
            if (strategyType.equalsIgnoreCase(CONSTANTS._close) || strategyType.equalsIgnoreCase(CONSTANTS._open_n_close))
                sp.getCloseConditionGroups().iterator()
                        .forEachRemaining(s -> {
                                    Map<String, Object> strategyMatch = new HashMap<>();
                                    checkStrategy(s, strategyMatch);
                                    if (!strategyMatch.isEmpty()) {
                                        strategyMatchList.add(strategyMatch);
                                        strategyMatch.put(CONSTANTS._strategy_pair, sp);
                                    }
                                }
                        );
        }
        return strategyMatchList;
    }

    private void checkStrategy(Strategies.ConditionsGroup s, Map<String, Object> strategyMatch) {

        List<Strategies.ConditionsGroup.Condition> conditions = s.getConditions();
        List<MarketEvent> filteredEvents = new ArrayList<>();
        conditions.forEach(c -> checkCondition(c, filteredEvents));
        if (filteredEvents.size() >= conditions.size()) {
            checkSequence(filteredEvents, conditions, s, strategyMatch);
        }

    }

    /**
     * Conditions -> sequence 1 is latest one to arrived
     * <p>
     * positive case
     * Ex: 1 - first event that occurred - Event A (oldest one to check)
     * 2 - second event that occurred
     * 3 - last event that occurred
     * <p>
     * filteredEvents -> sort by desc eventDate
     * Ex: index 1 - 3:45 - Event G
     * 2 - 3:40
     * 3 - 3:00 (oldest one at bottom)
     * <p>
     * ***************  Valid use cases  **********************
     * <p>
     * ->    Latest
     * --------------------------------------------------------------------
     * Event A   Event B Event C     Event D         Event E        Event F
     * X           X     Y            X              Y               Z
     * Order X Y Z is valid
     * <p>
     * ->    Latest
     * --------------------------------------------------------------------
     * Event A   Event B Event C     Event D         Event E        Event F
     * X           X     Y            Y              Y               Z
     * Order X Y Z is valid
     * <p>
     * <p>
     * ->    Latest
     * --------------------------------------------------------------------
     * Event A   Event B Event C     Event D         Event E        Event F
     * X           X     Y            Y              X               Z
     * Order X Y Z is valid
     * <p>
     * **************  Invalid Use cases ***********************
     * <p>
     * ->    Latest
     * --------------------------------------------------------------------
     * Event A   Event B Event C     Event D         Event E        Event F
     * Y           X     Z            Z              X               Z
     * Order X Y Z is valid
     *
     * @param filteredEvents
     * @param conditions
     */
    private void checkSequence(List<MarketEvent> filteredEvents, List<Strategies.ConditionsGroup.Condition> conditions, Strategies.ConditionsGroup conditionsGroup, Map<String, Object> strategyMatch) {
        //Limited
        Collections.sort(conditions);
        Collections.sort(filteredEvents);
        Map<String, MarketEvent> filteredMarketEvents = new HashMap<>();
        //To validate position
        int placement = 1;
        for (Strategies.ConditionsGroup.Condition c : conditions) {
            for (int j = 0, validConditionsSize = filteredEvents.size(); j < validConditionsSize; j++) {
                MarketEvent v = filteredEvents.get(j);
                if (v.getName().equalsIgnoreCase(c.getName())) {
                    if (j + 1 >= placement) {
                        if (!filteredMarketEvents.containsKey(v.getName()))
                            filteredMarketEvents.put(v.getName(), v);
                        placement = j + 1;
                    }
                }
            }
        }
        if (filteredMarketEvents.size() == conditions.size()) {
            MarketEvent marketEvent = filteredEvents.get(filteredEvents.size() - 1);
            strategyMatch.put(CONSTANTS._strategy, conditionsGroup);
            strategyMatch.put(CONSTANTS._marketEvent, marketEvent);
            Log.information("A match found for {Strategy}: {MarketEvent}", conditionsGroup.getConditionsName(), marketEvent);
        }
    }

    /**
     * Event       - 4:28 - 1000
     * Now         - 4:30 - 1120
     * Strategy    - 15m  - 900
     * <p>
     * valid = Event time + Strategy > dateNow
     *
     * @param c
     * @param filteredEvents
     */
    private void checkCondition(Strategies.ConditionsGroup.Condition c, List<MarketEvent> filteredEvents) {
        List<MarketEvent> events = Events.getInstance().getMarketEvents();
        Log.information("{CheckConditions} Market events found {Count} ", "CheckConditions", events.size());
        List<MarketEvent> eventsToProcess = events.stream()
                .filter(e -> e.getTimeframe() == c.getTimeFrame())
                .filter(e -> e.getCategory().equalsIgnoreCase(c.getCategory()))
                .filter(e -> e.getSymbol().equalsIgnoreCase(c.getSymbol()))
                .collect(Collectors.toList());
        long dateNow = (new Date()).getTime();
        Log.information("{CheckConditions} Eligible Market events found {Count} ", "CheckConditions", eventsToProcess.size());
        Log.information("{CheckConditions} Checking for condition match {Condition} ","CheckConditions", c.getName());

        //Filtering with Symbol, timeframe and sorting with latest on top
        Collections.sort(eventsToProcess, Collections.reverseOrder());

        List<MarketEvent> positiveMarketEvents = new ArrayList<>();
        if (eventsToProcess.size() <= 0) {
            return;
        } else {
            MarketEvent event = eventsToProcess.get(0);
            Log.information("{CheckConditions} TimeNow - {TimeNow}, {EventTimeEpoch}, {ConditionTimeEpoch} ","CheckConditions", dateNow,
                    event.getEventTimeInEpoch(), c.getEpochForTimeFrame());
            if (event.getName().equalsIgnoreCase(c.getName())) {
                if (event.getEventTimeInEpoch() + c.getEpochForTimeFrame() > dateNow) {
                    Log.information("{CheckConditions} A positive match found on a condition ","CheckConditions", c.getName());
                    positiveMarketEvents.add(event);
                }
            }
        }
        filteredEvents.addAll(positiveMarketEvents);
    }

    public synchronized void run() {

    }

    /**
     * This is for testing only
     */
//    public StrategyAnalyzer(List<Strategies.Strategy> strategies) {
//        //props = PropertyUtil.getProperties();
//        this.strategies = strategies;
//    }

}

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

    public List<Map<String, Object>> analyzeStrategy(String strategyType, MarketEvent marketEvent) {
        Strategies strategies = LoadStrategies.getInstance().getStrategies();
        List<Strategies.Strategy> strategyList = strategies.getStrategies().stream().
                filter(s -> s.getExchangeId() == marketEvent.getExchangeId()).collect(Collectors.toList());
        List<Map<String, Object>> strategyMatchList = new ArrayList<>();
        for (Strategies.Strategy sp : strategyList) {
            LogContext.pushProperty("Strategy", sp.getStrategyName());
            LogContext.pushProperty("Application", "Analyzer");
            //For Open Strategies - Open and Close is for spot
            if (strategyType.equalsIgnoreCase(CONSTANTS._open) || strategyType.equalsIgnoreCase(CONSTANTS._open_n_close)) {
                sp.getOpenConditionGroups().iterator()
                        .forEachRemaining(s -> {
                                    Map<String, Object> strategyMatch = new HashMap<>();
                                    checkStrategy(s, strategyMatch, marketEvent);
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
                                    checkStrategy(s, strategyMatch, marketEvent);
                                    if (!strategyMatch.isEmpty()) {
                                        strategyMatchList.add(strategyMatch);
                                        strategyMatch.put(CONSTANTS._strategy_pair, sp);
                                    }
                                }
                        );
        }
        return strategyMatchList;
    }

    private void checkStrategy(Strategies.ConditionsGroup s, Map<String, Object> strategyMatch, MarketEvent marketEvent) {

        List<Strategies.ConditionsGroup.Condition> conditions = s.getConditions();
        List<MarketEvent> filteredEvents = new ArrayList<>();
        conditions.forEach(c -> checkCondition(c, filteredEvents, marketEvent));
        if (filteredEvents.size() >= conditions.size()) {
            checkSequence(filteredEvents, conditions, s, strategyMatch, marketEvent);
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
     *  @param filteredEvents
     * @param conditions
     * @param marketEvent
     */
    private void checkSequence(List<MarketEvent> filteredEvents, List<Strategies.ConditionsGroup.Condition> conditions, Strategies.ConditionsGroup conditionsGroup, Map<String, Object> strategyMatch, MarketEvent marketEvent) {
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
        // This means all conditions specified in strategy met
        if (filteredMarketEvents.size() == conditions.size()) {
            strategyMatch.put(CONSTANTS._strategy, conditionsGroup);
        } else {
            Log.information("{Application} - {Function} - {MarketEventId} Some events matched with strategy conditions, " +
                            "but unfortunately the sequence didn't match " +
                            "{StrategyConditions} - {FilteredMarketEvents}",
                    "Analyzer", "CheckConditions", marketEvent.getId(),
                    conditions.stream().map(Strategies.ConditionsGroup.Condition::getName)
                            .collect(Collectors.joining(", ")),
                    new ArrayList<MarketEvent>(filteredMarketEvents.values()).stream().map(MarketEvent::getName)
                            .collect(Collectors.joining(", ")));
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
    private void checkCondition(Strategies.ConditionsGroup.Condition c, List<MarketEvent> filteredEvents, MarketEvent marketEvent) {
        List<MarketEvent> events = Events.getInstance().getMarketEvents();

        List<MarketEvent> eventsToProcess = events.stream()
                .filter(e -> e.getTimeframe() == c.getTimeFrame())
                .filter(e -> e.getCategory().equalsIgnoreCase(c.getCategory()))
                .filter(e -> e.getSymbol().equalsIgnoreCase(c.getSymbol()))
                .filter(e -> e.getExchangeId().equals(marketEvent.getExchangeId()))
                .collect(Collectors.toList());
        long dateNow = (new Date()).getTime();

        //Filtering with Symbol, timeframe and sorting with latest on top
        Collections.sort(eventsToProcess, Collections.reverseOrder());

        List<MarketEvent> positiveMarketEvents = new ArrayList<>();
        if (eventsToProcess.size() <= 0) {
            Log.information("{Application} - {Function} - {MarketEventId} - {Exchange} No Eligible Market events found",
                    "Analyzer", "CheckConditions", marketEvent.getId(), marketEvent.getExchangeName());
            return;
        } else {
            //Assuming there will be two events per pair (always get the latest / category)
            MarketEvent event = eventsToProcess.get(0);
            try {
                Log.information("{Application} - {Function} - {MarketEventId} " +
                                "Trying Name match - {Condition1}, Event time match {Condition2}", "Analyzer", "CheckConditions", marketEvent.getId(),
                        event.getName().trim() +
                                (event.getName().trim().equalsIgnoreCase(c.getName().trim()) ? " equals to " : " not equals to ")
                                + c.getName().trim(),
                        event.getEventTimeInEpoch() + c.getEpochForTimeFrame() > dateNow ?
                                "With in the time limit (" + (dateNow - event.getEventTimeInEpoch() + c.getEpochForTimeFrame() / 1000) + " sec left)" :
                                "Event expired (" + (event.getEventTimeInEpoch() + c.getEpochForTimeFrame() - dateNow / 1000) + " passed )");
                if (event.getName().trim().equalsIgnoreCase(c.getName().trim()) && event.getEventTimeInEpoch() + c.getEpochForTimeFrame() > dateNow) {
                    positiveMarketEvents.add(event);
                    Log.information("{Application} - {Function} - {MarketEventId} A positive match found on a condition ", "Analyzer",
                            "CheckConditions", marketEvent.getId());
                }
            } catch (Exception e) {
                Log.error(e, "{Application} - {Function} - {MarketEventId} Error in checking condition ", "Analyzer", "CheckConditions", marketEvent.getId());
            }

        }
        filteredEvents.addAll(positiveMarketEvents);
    }

    public synchronized void run(MarketEvent me) {

    }

    /**
     * This is for testing only
     */
//    public StrategyAnalyzer(List<Strategies.Strategy> strategies) {
//        //props = PropertyUtil.getProperties();
//        this.strategies = strategies;
//    }

}

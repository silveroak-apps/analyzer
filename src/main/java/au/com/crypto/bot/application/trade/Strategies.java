package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.analyzer.entities.StrategyConditions;

import java.util.*;
import java.util.stream.Collectors;

public class Strategies {
    private List<Strategy> strategies;

    public List<Strategy> getStrategies() {
        if (strategies == null ) {
            return Collections.emptyList();
        }
        return strategies;
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * {
     * "strategies":[
     * {
     * "name":"strategyOne",
     * "source":"TradingView",
     * "conditions":[
     * {
     * "name":"3bar_6h_alert",
     * "last_observed":"6h",
     * "sequence": 1
     * },
     * {
     * "name":"2wave_cross_2m_alert",
     * "last_observed":"5m",
     * "sequence": 2
     * },
     * {
     * "name":"moneyflow_15m_alert",
     * "last_observed":"1h",
     * "sequence": 3
     * }
     * ]
     * },
     * {
     * "name":"strategyTwo",
     * "source":"TradingView",
     * "conditions":[
     * {
     * "name":"3bar_6h_alert",
     * "last_observed":"6h",
     * "sequence": 1
     * },
     * {
     * "name":"2wave_cross_2m_alert",
     * "last_observed":"5m",
     * "sequence": 2
     * },
     * {
     * "name":"moneyflow_15m_alert",
     * "last_observed":"1h",
     * "sequence": 3
     * }
     * ]
     * }
     * ]
     * }
     */

    public static class Strategy {

        private long strategyId;
        private String strategyName;
        private String positionType;
        private String exchangeType;
        private long exchangeId;
        private String symbol;
        private String source;
        private long version;
        private List<ConditionsGroup> openConditionGroups;
        private List<ConditionsGroup> closeConditionGroups;

        public Strategy() {

        }
        public Strategy(au.com.crypto.bot.application.analyzer.entities.Strategy strategy) {
            this.setSymbol(strategy.getSymbol());
            this.setPositionType(strategy.getPositionType());
            this.setStrategyName(strategy.getName());
            this.setExchangeType(strategy.getExchangeType());
            this.setVersion(strategy.getVersion());
            this.setStrategyId(strategy.getId());
            this.setExchangeId(strategy.getExchangeId());
        }

        public long getExchangeId() {
            return exchangeId;
        }

        public void setExchangeId(long exchangeId) {
            this.exchangeId = exchangeId;
        }

        public long getStrategyId() {
            return strategyId;
        }

        public void setStrategyId(long strategyId) {
            this.strategyId = strategyId;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public List<ConditionsGroup> getOpenConditionGroups() {
            if (openConditionGroups == null ) {
                setOpenConditionGroups(new ArrayList<>());
            }
            return openConditionGroups;
        }

        public void setOpenConditionGroups(List<ConditionsGroup> openConditionGroups) {
            this.openConditionGroups = openConditionGroups;
        }

        public String getStrategyName() {
            return strategyName;
        }

        public void setStrategyName(String strategyName) {
            this.strategyName = strategyName;
        }

        public List<ConditionsGroup> getCloseConditionGroups() {
            if ( closeConditionGroups == null ) {
                setCloseConditionGroups(new ArrayList<>());
            }
            return closeConditionGroups;
        }

        public void setCloseConditionGroups(List<ConditionsGroup> closeConditionGroups) {
            this.closeConditionGroups = closeConditionGroups;
        }

        public String getPositionType() {
            return positionType;
        }

        public void setPositionType(String positionType) {
            this.positionType = positionType;
        }

        public String getExchangeType() {
            return exchangeType;
        }

        public void setExchangeType(String exchangeType) {
            this.exchangeType = exchangeType;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Strategy copy() {
            Strategy newStrategyPair = new Strategy();
            newStrategyPair.setStrategyName(strategyName);
            newStrategyPair.setPositionType(positionType);
            newStrategyPair.setExchangeType(exchangeType);
            newStrategyPair.setSymbol(symbol);
            newStrategyPair.setSource(source);
            newStrategyPair.setExchangeId(exchangeId);
            newStrategyPair.setCloseConditionGroups(new ArrayList<>(getCloseConditionGroups()!=null ? getCloseConditionGroups() : Collections.emptyList()));
            newStrategyPair.setOpenConditionGroups(new ArrayList<>(getOpenConditionGroups()!=null ? getOpenConditionGroups() : Collections.emptyList()));
            return newStrategyPair;
        }
    }

    public static class ConditionsGroup implements Cloneable {

        public String conditionsName;

        public List<Condition> getConditions() {
            if (conditions == null) {
                setConditions(new ArrayList<>());
            }
            return conditions;
        }

        public void setConditions(List<Condition> conditions) {
            this.conditions = conditions;
        }

        private List<Condition> conditions;

        public String getConditionsName() {
            return conditionsName = conditions.stream().map(Condition::getName)
                    .collect(Collectors.joining("___"));
        }

        public static class Condition implements Comparable<Condition> {
            private long conditionId;
            private String name;
            private long epochForTimeFrame = 0L;
            private long timeFrame;
            private long lastObserved;
            private long sequence;
            private String symbol;
            private String category;
            private long version;

            public Condition(StrategyConditions c, String symbol) {
                this.setName(c.getName());
                this.setCategory(c.getCategory());
                this.setSequence(c.getSequence());
                this.setVersion(c.getVersion());
                this.setLastObserved(c.getLastObserved());
                this.setTimeFrame(c.getTimeFrame());
                this.setSymbol(symbol);
                this.setConditionId(c.getId());
            }

            public long getConditionId() {
                return conditionId;
            }

            public void setConditionId(long conditionId) {
                this.conditionId = conditionId;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public long getVersion() {
                return version;
            }

            public void setVersion(long version) {
                this.version = version;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getLastObserved() {
                return lastObserved;
            }

            public void setLastObserved(long lastObserved) {
                this.lastObserved = lastObserved;
            }

            public long getSequence() {
                return sequence;
            }

            public void setSequence(long sequence) {
                this.sequence = sequence;
            }

            public long getTimeFrame() {
                return timeFrame;
            }

            public void setTimeFrame(long timeFrame) {
                this.timeFrame = timeFrame;
            }

            private long timeframeToEpoch() {
//                if (lastObserved.endsWith("s")) {
//                    String timeframeLocal = this.lastObserved;
//                    timeframeLocal = timeframeLocal.replaceAll("s", "");
//                    return Integer.parseInt(timeframeLocal) * 1000;
//                }
//                if (lastObserved.endsWith("m")) {
                    long timeframeLocal = this.lastObserved;
                    return timeframeLocal * 60000;
//                }
//                if (lastObserved.endsWith("h")) {
//                    String timeframeLocal = this.lastObserved;
//                    timeframeLocal = timeframeLocal.replaceAll("h", "");
//                    return Integer.parseInt(timeframeLocal) * 3600000;
//                }
//                if (lastObserved.endsWith("d")) {
//                    String timeframeLocal = this.lastObserved;
//                    timeframeLocal = timeframeLocal.replaceAll("d", "");
//                    return Integer.parseInt(timeframeLocal) * 86400000;
//                }
            }

            public long getEpochForTimeFrame() {
                if (epochForTimeFrame == 0L) {
                    setEpochForTimeFrame(timeframeToEpoch());
                }
                return epochForTimeFrame;
            }

            public void setEpochForTimeFrame(long epochForTimeFrame) {
                this.epochForTimeFrame = epochForTimeFrame;
            }

            @Override
            public int compareTo(Condition o) {
                return Long.compare(this.getSequence(), o.getSequence());
            }
        }
    }

    protected Strategies copy() {
        Strategies newStrategies = new Strategies();
        var spList = new ArrayList<Strategy>();
        for (Strategy sp : getStrategies()) {
            spList.add(sp.copy());
        }
        newStrategies.setStrategies(spList);
        return newStrategies;
    }

//    public Strategies getStrategiesByTradeTypeAndExchangeType(String trade_type, String exchange_type) {
//        Strategies strategies = this.copy();
//        strategies.getStrategies().removeIf(strategyPair -> !strategyPair.getExchangeType().equalsIgnoreCase(exchange_type));
//        if (trade_type.equalsIgnoreCase(CONSTANTS._open)) {
//            strategies.getStrategies()
//                    .iterator()
//                    .forEachRemaining(sp -> {
//                                sp.setCloseConditions(Collections.emptyList());
//                                sp.setOpenConditions(new ArrayList<>() {
//                                    {
//                                        add(sp.getOpenStrategy());
//                                    }
//                                });
//                            }
//                    );
//
//        } else if (trade_type.equalsIgnoreCase(CONSTANTS._buy)) {
//            strategies.getStrategies()
//                    .iterator()
//                    .forEachRemaining(sp -> sp.setOpenConditions(Collections.emptyList()));
//        }
//        return strategies;
//    }
}

package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.ApplicationControllers;

import java.util.Map;


public interface Trader {
    public void raiseSignal(ApplicationControllers ac, long existingSignalId, double price, String symbol,
                            String tradeType,
                            String positionType, Strategies.ConditionsGroup conditionsGroup, String strategyPairName,
                            Map<String, String> props, String market, double contracts, long exchangeId, long marketEventId,
                            String exchangeName, Strategies.Strategy strategy);
}

package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.*;
import au.com.crypto.bot.application.utils.JSONHelper;
import au.com.crypto.bot.application.utils.LoadConfigs;
import au.com.crypto.bot.application.utils.Utils;
import com.google.gson.Gson;
import serilogj.Log;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Futures trade
 */
public class FuturesTrader extends TraderImpl {

    Gson gson = new Gson();
    int leverage = 10;
    //int quantity = 1; //Asset quantity

    public FuturesTrader() {
    }

    @Override
    @Transactional
    public void raiseSignal(ApplicationControllers ac, long existingSignalId, double price, String symbol,
                            String tradeType,
                            String positionType, Strategies.ConditionsGroup conditionsGroup, String strategyPairName,
                            Map<String, String> props, String market, double contracts, long exchangeId, long marketEventId, String exchangeName, Strategies.Strategy strategy) {
        long signalId = existingSignalId;
        if (existingSignalId == 0L) {
            signalId = saveFutureSignal(ac, symbol, positionType, strategyPairName, exchangeId, marketEventId, strategy);
            Log.information(" {Application} - {Function} - {MarketEventId} - {Exchange} Successfully saved a signal {Symbol} for action type {ActionType} with signal id {SignalId}",
                    "Analyzer", "SignalAndCommand", marketEventId, exchangeName, symbol, tradeType, signalId);
        }
        saveFutureSignalCommand(ac, signalId, conditionsGroup.getConditionsName(), symbol, price, tradeType, conditionsGroup, market, contracts, props, marketEventId, exchangeName);
    }

    private void saveFutureSignalCommand(ApplicationControllers ac, long signalId, String strategyPairName,
                                         String symbol, double price, String tradeType,
                                         Strategies.ConditionsGroup conditionsGroup, String market, double eventContracts, Map<String, String> props, long marketEventId, String exchangeName) {
        SignalCommandController fscController = ac.getFuturesSignalCommandController();
        SignalCommand fsCommand = new SignalCommand();
        fsCommand.setSignalId(signalId);
        fsCommand.setStrategyName(strategyPairName);
        fsCommand.setStrategyHash(JSONHelper.jsonToHash(gson.toJson(conditionsGroup)));
        fsCommand.setStrategyData(gson.toJson(conditionsGroup));
        fsCommand.setStatus(CONSTANTS._created);
        fsCommand.setRequestDateTime(new Date());
        fsCommand.setLeverage(leverage);
        fsCommand.setPrice(new BigDecimal(price));
        fsCommand.setMarketEventId(marketEventId);
        fsCommand.setSignalAction(tradeType); //OPEN, CLOSE, INCREASE ..
        //Assuming there will be only one match pair
        fsCommand.setStrategyConditionId(conditionsGroup.getConditions().get(0).getConditionId());
        double contracts = 0;
        if (tradeType.equalsIgnoreCase(CONSTANTS._close)) {
            contracts = getActiveContracts(ac, signalId);
        } else if (eventContracts <= 0) {
            contracts = getDefaultContractsFromDB(ac, symbol, exchangeName, marketEventId, signalId);
        } else {
            contracts = getContractsByMarket(market, eventContracts, price, marketEventId);
        }

        if (contracts != 0) {
            fsCommand.setQuantity(BigDecimal.valueOf(contracts));
            String signalCommandId = fscController.save(fsCommand);
            Log.information("{Application} - {Function} - {MarketEventId} - {SignalCommandId} Successfully placed a command for @{Symbol} with signal id {SignalId} " +
                    "with Contracts From Active Position {Contracts}" +
                    "with Position Type {PositionType}" +
                    "with Exchange {Exchange}" +
                    "with Strategy name {Strategy}" +
                    "with marketEventId {MarketEventId}" +
                    "with Leverage {Leverage}", "Analyzer", "SignalAndCommand", marketEventId,
                    signalCommandId, symbol, signalId, fsCommand.getQuantity(),
                    tradeType, exchangeName, strategyPairName, marketEventId, leverage);
            try {
                Utils.triggerTrader(props.get("traderUrl"));
            } catch (Exception e) {
                Log.error(e, "{Application} - {Function} - {MarketEventId} - {SignalId} - {Exchange} Error Raising http signal to trader", "Analyzer", "SignalAndCommand", marketEventId, signalId, exchangeName);
            }
        } else {
            Log.warning("{Application} - {Function} - {MarketEventId} - {SignalId} - {Exchange} WRONG SIGNAL OR NO CONTRACTS - " +
                    "Symbol: {Symbol} - Contracts: {Contracts} ",
                    "Analyzer", "SignalAndCommand", marketEventId,signalId, exchangeName, symbol,
                    getDefaultContractsFromDB(ac, symbol, exchangeName, marketEventId,signalId));
        }

    }


    private double getContractsByMarket(String market, double contracts, double price, long marketEventId) {
        double contractsPerType = market.equalsIgnoreCase("USDT") ? contracts : (contracts * price) / 10;
        int scale = 4;
        return BigDecimal.valueOf(contractsPerType).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double getActiveContracts(ApplicationControllers ac, long signalId) {
        return ac.getFuturesSignalController().getActiveContractsFromDB(signalId);
    }

    private long saveFutureSignal(ApplicationControllers ac, String symbol, String positionType, String strategyPairName, long exchangeId, long marketEventId, Strategies.Strategy strategy) {
        Signal fs = new Signal();
        fs.setExchangeId(exchangeId);
        fs.setCreatedDateTime(new Date());
        fs.setStrategyPairName(strategyPairName);
        fs.setPositionType(positionType);
        fs.setSymbol(symbol);
        fs.setStrategyPairId(strategy.getStrategyId());
        SignalController fsc = ac.getFuturesSignalController();
        long signalId = fsc.save(fs);
        Log.information("{Application} - {Function} - {MarketEventId} Successfully saved a future signal {Symbol} signal id {SignalId}", "Analyzer", "SignalAndCommand", marketEventId, symbol, signalId);
        return signalId;
    }

    private Double getDefaultContractsFromDB(ApplicationControllers ac, String symbol, String exchangeName, long marketEventId, long signalId) {
        double returnValue = 0d;
        try {
            Map<String, String> contracts = LoadConfigs.getConfig(ac, exchangeName);
            if (contracts.containsKey(symbol))
                returnValue = Double.parseDouble(contracts.get(symbol));
        } catch (Exception e) {
            Log.error(e, "{Application} - {Function} - {MarketEventId} - {SignalId} Error in parsing contract config value, check config table, expecting a double", "Analyzer", "SignalAndCommand", marketEventId, signalId);
        }
        return returnValue;
    }

    private Double getContractMultiplierFromDB(ApplicationControllers ac, String symbol) {
        double returnValue = 1d;
        try {
            Map<String, String> contracts = LoadConfigs.getConfig(ac, CONSTANTS._contractsMultiplier);
            if (contracts.containsKey(symbol))
                returnValue = Double.parseDouble(contracts.get(symbol));
        } catch (Exception e) {
            Log.error(e, "{Application} - {Function} - {MarketEventId} Error in parsing contract config value, check config table, expecting a double");
        }
        return returnValue;
    }
}

package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.*;
import au.com.crypto.bot.application.utils.JSONHelper;
import au.com.crypto.bot.application.utils.LoadConfigs;
import com.google.gson.Gson;
import serilogj.Log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
    public void raiseSignal(ApplicationControllers ac, long existingSignalId, double price, String symbol,
                            String tradeType,
                            String positionType, Strategies.ConditionsGroup conditionsGroup, String strategyPairName,
                            Map<String, String> props, String market, int contracts) {
        long signalId = existingSignalId;
        if (existingSignalId == 0L) {
            signalId = saveFutureSignal(ac, symbol, positionType, strategyPairName);
            Log.information("{@Application} Successfully posted a signal command @{Symbol} with signal id @{SignalId}", "Analyzer", symbol, signalId);
        }
        if (tradeType.equalsIgnoreCase(CONSTANTS._open)) {
            if (isPositionOpen(ac, signalId)) {
                signalId = saveFutureSignal(ac, symbol, positionType, strategyPairName);
                saveFutureSignalCommand(ac, signalId, conditionsGroup.getConditionsName(), symbol, price, tradeType, conditionsGroup, contracts);
                Log.information("{@Application} Successfully added command @{Symbol} with signal id @{SignalId}", "Analyzer", symbol, signalId);
            } else {
                Log.information("{@Application} Command already placed for @{Symbol} with signal id @{SignalId} for action type {Action}" +
                        "", "Analyzer", symbol, signalId, tradeType);
            }
        }

        if (tradeType.equalsIgnoreCase(CONSTANTS._close)) {
            saveFutureSignalCommand(ac, signalId, conditionsGroup.getConditionsName(), symbol, price, tradeType, conditionsGroup, contracts);
            Log.information("{@Application} Successfully added command @{Symbol} with signal id @{SignalId}", "Analyzer", symbol, signalId);
        }

    }

    //Signal Status not ACTIVE OR signal status ACTIVE AND not OPEN
    private boolean isPositionOpen(ApplicationControllers ac, long signalId) {
        FuturesSignalCommandController fscController = ac.getFuturesSignalCommandController();
        List<FuturesSignal> fscCommand = fscController.findFuturePositionsWithSignal(signalId);
        return !fscCommand.isEmpty();
    }

    //trade type can be OPEN, CLOSE, INCREASE ..
    private boolean isSignalCommandAlreadyPlaced(ApplicationControllers ac, String jsonHash, long signalId, String tradeType) {
        FuturesSignalCommandController fscController = ac.getFuturesSignalCommandController();
        List<FuturesSignalCommand> fscCommand = fscController.finSignalsBySignalNHashNTradeAction(jsonHash, signalId, tradeType);
        return !fscCommand.isEmpty();
    }


    private void saveFutureSignalCommand(ApplicationControllers ac, long signalId, String strategyPairName,
                                         String symbol, double price, String tradeType,
                                         Strategies.ConditionsGroup conditionsGroup, int eventContracts) {
        FuturesSignalCommandController fscController = ac.getFuturesSignalCommandController();
        FuturesSignalCommand fsCommand = new FuturesSignalCommand();
        fsCommand.setSignalId(signalId);
        fsCommand.setStrategyName(strategyPairName);
        fsCommand.setStrategyHash(JSONHelper.jsonToHash(gson.toJson(conditionsGroup)));
        fsCommand.setStrategyData(gson.toJson(conditionsGroup));
        fsCommand.setStatus(CONSTANTS._created);
        fsCommand.setRequestDateTime(new Date());
        fsCommand.setLeverage(leverage);
        fsCommand.setPrice(new BigDecimal(price));

        fsCommand.setSignalAction(tradeType); //OPEN, CLOSE, INCREASE ..
        if (eventContracts <= 0) {
            double contracts = getDefaultContractsFromDB(ac, symbol);
            if (contracts != 0) {
                double multiplier = getContractMultiplierFromDB(ac, symbol);
                fsCommand.setQuantity(BigDecimal.valueOf(contracts * multiplier));
                fscController.save(fsCommand);
                Log.information("{@Application} Successfully saved for @{Symbol} with signal id @{SignalId} " +
                        "with Contracts {Contracts}" +
                        "with Position Type {PositionType}" +
                        "with Strategy name {Strategy}" +
                        "with contract multiplier {Multiplier}" +
                        "with Leverage {Leverage}", "Analyzer", symbol, signalId, contracts, tradeType, strategyPairName,multiplier, leverage);
            } else {
                Log.error("WRONG SYMBOL OR NO CONTRACTS - Symbol: {Symbol} - Balance: {Contracts} ", symbol, getDefaultContractsFromDB(ac, symbol));
            }
        } else {
            double multiplier = getContractMultiplierFromDB(ac, symbol);
            fsCommand.setQuantity(BigDecimal.valueOf(eventContracts * multiplier));
            fscController.save(fsCommand);
            Log.information("{@Application} Successfully saved for @{Symbol} with signal id @{SignalId} " +
                    "with Contracts {Contracts}" +
                    "with Position Type {PositionType}" +
                    "with Strategy name {Strategy}" +
                    "with contract multiplier {Multiplier}" +
                    "with Leverage {Leverage}", "Analyzer", symbol, signalId, eventContracts, tradeType, strategyPairName, multiplier, leverage);
        }

    }

    private long saveFutureSignal(ApplicationControllers ac, String symbol, String positionType, String strategyPairName) {
        FuturesSignal fs = new FuturesSignal();
        fs.setExchangeId(CONSTANTS._binance_exchange_futures);
        fs.setCreatedDateTime(new Date());
        fs.setStrategyPairName(strategyPairName);
        fs.setPositionType(positionType);
        fs.setSymbol(symbol);
        FuturesSignalController fsc = ac.getFuturesSignalController();
        long signalId = fsc.save(fs);
        Log.information("{@Application} Successfully posted a future signal @{Symbol} signal id @{SignalId}", "Analyzer", symbol, signalId);
        return signalId;
    }

    private Double getDefaultContractsFromDB(ApplicationControllers ac, String symbol) {
        double returnValue = 0d;
        try {
            Map<String, String> contracts = LoadConfigs.getConfig(ac, CONSTANTS._contracts);
            if (contracts.containsKey(symbol))
                returnValue = Double.parseDouble(contracts.get(symbol));
        } catch (Exception e) {
            Log.error(e, "Error in parsing contract config value, check config table, expecting a double");
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
            Log.error(e, "Error in parsing contract config value, check config table, expecting a double");
        }
        return returnValue;
    }
}

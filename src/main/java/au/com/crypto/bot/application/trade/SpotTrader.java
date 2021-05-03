package au.com.crypto.bot.application.trade;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.utils.LogUtil;
import au.com.crypto.bot.application.utils.Utils;
import au.com.crypto.bot.application.analyzer.entities.PositiveSignal;
import au.com.crypto.bot.application.analyzer.entities.PositiveSignalController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serilogj.Log;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class SpotTrader extends TraderImpl {

    private static final Logger logger = LoggerFactory.getLogger(SpotTrader.class);

    //Will ignore the singal id
    @Override
    public void raiseSignal(ApplicationControllers ac, long existingSignalId, double price, String symbol,
                            String tradeType,
                            String positionType, Strategies.ConditionsGroup conditionsGroup, String strategyPairName,
                            Map<String, String> props, String market, int contracts) {
        buyOrder(ac.getPositiveSignalController(), price, symbol, market, tradeType, props, conditionsGroup.getConditionsName());
    }

    private void buyOrder(PositiveSignalController positiveSignalController, double price, String symbol, String market, String trigger, Map<String, String> props, String strategy) {

        PositiveSignal pos = null;
        PositiveSignal existingSignal = isSignalExist(positiveSignalController, symbol, strategy);
        if (existingSignal != null) {
            pos = existingSignal;
            if (trigger.equals(CONSTANTS._sell)) {
                String signalId = placeSignal(pos, symbol, pos.getBuyPrice().doubleValue(), price, market, trigger, positiveSignalController,
                        existingSignal.getSignalDate(), new Date(), props, strategy);
                Log.information("Successfully updated {@SignalId} for strategy {@Strategy} for the symbol {@Symbol}", signalId);
                LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), SpotTrader.class.getSimpleName(), "Successfully updated {@SignalId} for strategy " +
                        "tradingView for the symbol " + symbol, signalId);
            } else {
                LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), SpotTrader.class.getSimpleName(), "Position already exist, no change in position for " +
                        "{@SignalId} for " + symbol, pos.getSignal_id());
            }
        } else {
            pos = new PositiveSignal();
            if (!trigger.equals(CONSTANTS._sell)) {
                String signalId = placeSignal(pos, symbol, price, 0, market, trigger, positiveSignalController, new Date(), null, props, strategy);
                Log.information("Successfully posted signal {@SignalId} for strategy {@Strategy} for the symbol {@Symbol}", signalId,
                        strategy, symbol);
                LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), SpotTrader.class.getSimpleName(), "Successfully posted a signal " +
                        "{@SignalId} for strategy tradingView for the symbol " + symbol, signalId);
            } else {
                LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), SpotTrader.class.getSimpleName(), "Ignored Short signal for strategy tradingView " +
                        "for the symbol " + symbol);
            }
        }

    }

    private String placeSignal(PositiveSignal pos, String symbol, double buyPrice, double sellPrice, String market, String trigger,
                               PositiveSignalController positiveSignalController, Date signalDate, Date sellSignalDatetime, Map<String, String> props,
                               String strategy) {
        LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), SpotTrader.class.getSimpleName(), "Caught a fish in strategy from trading view");
        pos.setSymbol(symbol); // BTCUSDT
        pos.setBuyPrice(BigDecimal.valueOf(buyPrice));
        pos.setActualBuyPrice(BigDecimal.valueOf(buyPrice));
        pos.setSellPrice(BigDecimal.valueOf(sellPrice));
        pos.setActualSellPrice(BigDecimal.valueOf(sellPrice));
        pos.setMarket(market); //USDT OR BTC OR ETH OR BNB
        pos.setStatus(trigger);
        pos.setSignalDate(signalDate);
        pos.setBuyDate(signalDate);
        pos.setSellSignalDateTime(sellSignalDatetime);
        pos.setSellDate(sellSignalDatetime);
        pos.setStrategy(strategy);
        pos.setExchangeId((long) CONSTANTS._binance_exchange_spot);
        String signalId = positiveSignalController.save(pos);
        Utils.triggerSellBotForBuy(props.get("protocol"), props.get("host"), props.get("path"));
        return signalId;
    }

    private PositiveSignal isSignalExist(PositiveSignalController controller, String symbol, String strategy) {
        return controller.findOpenSignalBySymbol(symbol, strategy);
    }

}

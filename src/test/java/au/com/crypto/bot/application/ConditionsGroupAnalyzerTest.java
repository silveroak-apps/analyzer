package au.com.crypto.bot.application;

import au.com.crypto.bot.application.analyzer.entities.MarketEvent;
import au.com.crypto.bot.application.trade.Events;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class ConditionsGroupAnalyzerTest {

    Events events = Events.getInstance();

    @Before
    public void init() {
        //Failed case
        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
                new Date(new Date().getTime() - 3600000), "BTCUSDT", new BigDecimal(50000.33), 100,
                "USDT", 21600));
        events.addMarketEvent(new MarketEvent("2wave_cross_5m_alert","source", "json_message",
                new Date(new Date().getTime() - 90000), "BTCUSDT", new BigDecimal(50010.33), 100,
                "USDT", 300));
        events.addMarketEvent(new MarketEvent("moneyflow_1m_alert","source", "json_message",
                new Date(new Date().getTime() - 110000), "BTCUSDT", new BigDecimal(50005.33),100,
                "USDT", 60));
        events.addMarketEvent(new MarketEvent("2wave_cross_5m_alert","source", "json_message",
                new Date(new Date().getTime() - 83000), "BTCUSDT", new BigDecimal(50010.33),100,
                "USDT", 300));
        events.addMarketEvent(new MarketEvent("moneyflow_1m_alert","source", "json_message",
                new Date(new Date().getTime()), "BTCUSDT", new BigDecimal(50005.33),100,
                "USDT", 60));
        events.addMarketEvent(new MarketEvent("2wave_cross_1h_alert","source", "json_message",
                new Date(new Date().getTime() - 260000), "BTCUSDT", new BigDecimal(50000.33),110,
                "USDT", 60));
        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
                new Date(new Date().getTime() - 26033000), "BTCUSDT", new BigDecimal(50000.33),110,
                "USDT", 21600));
        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
                new Date(new Date().getTime() - 260332000), "BTCUSDT", new BigDecimal(50000.33),110,
                "USDT", 21600));
        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
                new Date(new Date().getTime() - 26098000), "BTCUSDT", new BigDecimal(50000.33),110,
                "USDT", 21600));

//        //Positive case
//        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
//                new Date(new Date().getTime() - 3600000), "BTCUSDT", new BigDecimal(50000.33),
//                "USDT", "6h"));
//        events.addMarketEvent(new MarketEvent("2wave_cross_5m_alert","source", "json_message",
//                new Date(new Date().getTime() - 12000), "BTCUSDT", new BigDecimal(50010.33),
//                "USDT", "5m"));
//        events.addMarketEvent(new MarketEvent("moneyflow_1m_alert","source", "json_message",
//                new Date(new Date().getTime() - 110000), "BTCUSDT", new BigDecimal(50005.33),
//                "USDT", "1m"));
//        events.addMarketEvent(new MarketEvent("2wave_cross_5m_alert","source", "json_message",
//                new Date(new Date().getTime() - 10000), "BTCUSDT", new BigDecimal(50010.33),
//                "USDT", "5m"));
//        events.addMarketEvent(new MarketEvent("moneyflow_1m_alert","source", "json_message",
//                new Date(new Date().getTime() - 90000), "BTCUSDT", new BigDecimal(50005.33),
//                "USDT", "1m"));
//        events.addMarketEvent(new MarketEvent("2wave_cross_1h_alert","source", "json_message",
//                new Date(new Date().getTime() - 260000), "BTCUSDT", new BigDecimal(50000.33),
//                "USDT", "1m"));
//        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
//                new Date(new Date().getTime() - 26033000), "BTCUSDT", new BigDecimal(50000.33),
//                "USDT", "1m"));
//        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
//                new Date(new Date().getTime() - 260332000), "BTCUSDT", new BigDecimal(50000.33),
//                "USDT", "1m"));
//        events.addMarketEvent(new MarketEvent("3bar_6h_alert","source", "json_message",
//                new Date(new Date().getTime() - 26098000), "BTCUSDT", new BigDecimal(50000.33),
//                "USDT", "1m"));


    }

    @Test
    public void testStrategyAnalyzer() {
//        List<Strategy> st = LoadStrategies.getInstance().getStrategyList();
//        Set<String> tradeType = new HashSet<>();
//        tradeType.add(CONSTANTS._buy);
//        OpenPositionAnalyzer op = new OpenPositionAnalyzer(st);
//        op.analyzeStrategy(tradeType);
    }

}

package au.com.crypto.bot.application;

import au.com.crypto.bot.application.analysis.ClosePositionAnalyzer;
import au.com.crypto.bot.application.analysis.OpenPositionAnalyzer;
import au.com.crypto.bot.application.analysis.SpotBuyAnalyzer;
import au.com.crypto.bot.application.analyzer.entities.*;
import au.com.crypto.bot.application.ticker.entities.Coin24HrMarketController;
import au.com.crypto.bot.application.ticker.entities.CoinStatsController;
import au.com.crypto.bot.application.ticker.entities.CoinStatsHistoryController;
import au.com.crypto.bot.application.ticker.entities.TickerController;
import au.com.crypto.bot.application.trade.FuturesTrader;
import au.com.crypto.bot.application.trade.Trader;
import au.com.crypto.bot.application.utils.LogUtil;
import au.com.crypto.bot.application.utils.PropertyUtil;
import au.com.crypto.bot.application.web.PollAWSSQSService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import serilogj.Log;
import serilogj.LoggerConfiguration;
import serilogj.context.LogContext;
import serilogj.events.LogEventLevel;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static serilogj.sinks.coloredconsole.ColoredConsoleSinkConfigurator.coloredConsole;
import static serilogj.sinks.seq.SeqSinkConfigurator.seq;

@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan
@ImportResource("classpath:applicationContext.xml")
public class AnalyzerApplication {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzerApplication.class);

    static boolean isSimulation = false;
    static boolean isTradingView = true;
    static String exchangeType = "";
    static String queue_name = "signal_webhook_test";

    static String seqHost = "http://localhost:5341";

    public static void main(String[] args) {

        Map<String, String> props = PropertyUtil.getProperties();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        InitiateLogger(props);
        //Assuming the script always run with 2 arguments
        String signal_queue = args[0];
        Date startDate = new Date();
        try {
            LogContext.pushProperty("{Application}", "Analyzer");
            LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), AnalyzerApplication.class.getSimpleName(), "Analyzer bot started at ", startDate);
            if (props.get("isSimulation") != null)
                isSimulation = Boolean.parseBoolean(props.get("isSimulation"));

            ApplicationContext context =
                    new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});

            APIController api = (APIController) context.getBean("APIController");
            ExchangeController exchange = (ExchangeController) context.getBean("exchangeController");
            Data24HrController dailyData = (Data24HrController) context.getBean("data24HrController");
            TickerController tickerController = (TickerController) context.getBean("tickerController");
            CoinController coinController = (CoinController) context.getBean("coinController");
            CoinStatsController coinStatsController = (CoinStatsController) context.getBean("coinStatsController");
            PositiveSignalController positiveSignalController = (PositiveSignalController) context.getBean("positiveSignalController");
            BuyAnalysisController buyAnalysisController = (BuyAnalysisController) context.getBean("buyAnalysisController");
            Coin24HrMarketController coin24HrMarketController = (Coin24HrMarketController) context.getBean("coin24HrMarketController");
            KlineDataController klineDataController = (KlineDataController) context.getBean("klineDataController");
            WatchDogController watchDogController = (WatchDogController) context.getBean("watchDogController");
            CoinStatsHistoryController cshController = (CoinStatsHistoryController) context.getBean("coinStatsHistoryController");
            MarketEventController marketEventController = (MarketEventController) context.getBean("marketEventController");
            FuturesSignalController futuresSignalController = (FuturesSignalController) context.getBean("futuresSignalController");
            FuturesSignalCommandController futuresSignalCommandController = (FuturesSignalCommandController) context.getBean("futuresSignalCommandController");
            StrategyController strategyController = (StrategyController) context.getBean("strategyController");
            StrategyConditionsController strategyConditionsController = (StrategyConditionsController) context.getBean("strategyConditionsController");
            ConfigsController configsController = (ConfigsController) context.getBean("configsController");

            ApplicationControllers ac = ApplicationControllers.getInstance();
            ac.setApiController(api);
            ac.setCoinController(coinController);
            ac.setDailyData(dailyData);
            ac.setExchange(exchange);
            ac.setTickerController(tickerController);
            ac.setCoinStatsController(coinStatsController);
            ac.setPositiveSignalController(positiveSignalController);
            ac.setBuyAnalysisController(buyAnalysisController);
            ac.setCoin24HrMarketController(coin24HrMarketController);
            ac.setKlineDataController(klineDataController);
            ac.setCoinStatsHistoryController(cshController);
            ac.setMarketEventController(marketEventController);
            ac.setFuturesSignalController(futuresSignalController);
            ac.setFuturesSignalCommandController(futuresSignalCommandController);
            ac.setStrategyConditionsController(strategyConditionsController);
            ac.setStrategyController(strategyController);
            ac.setConfigController(configsController);

            if (props.get("isTradingView") != null)
                isTradingView = Boolean.parseBoolean(props.get("isTradingView"));

            if (signal_queue != null)
                queue_name = signal_queue;

            try {
                //Starting aws queue reader
                new PollAWSSQSService(ac, queue_name).getMessages();
            } catch (Exception e) {
                Log.warning(e, "Error in connecting AWS queue, system will use local queue");
            }
            if (props.get("exchangeType") != null)
                exchangeType = props.get("exchangeType");

            Trader trader = new FuturesTrader();
            if (exchangeType.equalsIgnoreCase(CONSTANTS._futures)) {
                new OpenPositionAnalyzer(ac, trader);
                new ClosePositionAnalyzer(ac, trader);
            } else if (exchangeType.equalsIgnoreCase(CONSTANTS._spot)) {
                new SpotBuyAnalyzer(ac, trader);
            } else if (exchangeType.equalsIgnoreCase("ALL")) {
                new OpenPositionAnalyzer(ac, trader);
                new ClosePositionAnalyzer(ac, trader);
                new SpotBuyAnalyzer(ac, trader);
            }
            LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), AnalyzerApplication.class.getSimpleName(), "Application Running in @{ExchangeType} " + exchangeType);
        } catch (Exception e) {
            Log.error(e,"ERROR IN RUNNING ANALYZER BOT");
            System.exit(0);
        }
       SpringApplication.run(AnalyzerApplication.class, args);

    }


    private static void InitiateLogger(Map<String, String> props) {

        if (StringUtils.isNotEmpty(props.get("seq.host.url"))) {
            seqHost = props.get("seq.host.url");
        }
        Log.setLogger(new LoggerConfiguration()
                .writeTo(coloredConsole())
                //.writeTo(rollingFile("/app/analyzer.log"), LogEventLevel.Debug)
                .writeTo(seq(seqHost))
                .setMinimumLevel(LogEventLevel.Verbose)
                .createLogger());
        LogUtil.printLog(logger, LogUtil.STATUS.INFO.name(), AnalyzerApplication.class.getSimpleName(), "Seq host " + seqHost);
    }
}

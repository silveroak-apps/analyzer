package au.com.crypto.bot.application;

import au.com.crypto.bot.application.analyzer.entities.*;
import au.com.crypto.bot.application.ticker.entities.Coin24HrMarketController;
import au.com.crypto.bot.application.ticker.entities.CoinStatsController;
import au.com.crypto.bot.application.ticker.entities.CoinStatsHistoryController;
import au.com.crypto.bot.application.ticker.entities.TickerController;

/**
 * Created by Sailaja on 2/25/2018.
 */
public class ApplicationControllers {


    private APIController apiController;
    private ExchangeController exchange;
    private Data24HrController dailyData;
    private CoinController coinController;
    private TickerController ticker;
    private CoinStatsController coinStatsController;
    private BuyAnalysisController buyAnalysisController;
    private Coin24HrMarketController coin24HrMarketController;
    private KlineDataController klineDataController;
    private CoinStatsHistoryController coinStatsHistoryController;
    private MarketEventController marketEventController ;
    private SignalController futuresSignalController ;
    private SignalCommandController futuresSignalCommandController;
    private StrategyController strategyController;
    private StrategyConditionsController strategyConditionsController;
    private ConfigsController configsController;

    private static ApplicationControllers ourInstance;

    public static ApplicationControllers getInstance() {
        if(ourInstance == null){
            ourInstance = new ApplicationControllers();
            return ourInstance;
        }
        return ourInstance;
    }

    private ApplicationControllers() {
    }

    public APIController getApiController() {
        return apiController;
    }

    public void setApiController(APIController apiController) {
        this.apiController = apiController;
    }

    public ExchangeController getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeController exchange) {
        this.exchange = exchange;
    }

    public Data24HrController getDailyData() {
        return dailyData;
    }

    public void setDailyData(Data24HrController dailyData) {
        this.dailyData = dailyData;
    }

    public CoinController getCoinController() {
        return coinController;
    }

    public void setCoinController(CoinController coinController) {
        this.coinController = coinController;
    }

    public TickerController getTickerController() {
        return ticker;
    }

    public void setTickerController(TickerController ticker) {
        this.ticker = ticker;
    }

    public CoinStatsController getCoinStatsController() {
        return coinStatsController;
    }

    public void setCoinStatsController(CoinStatsController coinStatsController) {
        this.coinStatsController = coinStatsController;
    }

    public static ApplicationControllers getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(ApplicationControllers ourInstance) {
        ApplicationControllers.ourInstance = ourInstance;
    }

    public BuyAnalysisController getBuyAnalysisController() {
        return buyAnalysisController;
    }

    public void setBuyAnalysisController(BuyAnalysisController buyAnalysisController) {
        this.buyAnalysisController = buyAnalysisController;
    }

    public Coin24HrMarketController getCoin24HrMarketController() {
        return coin24HrMarketController;
    }

    public void setCoin24HrMarketController(Coin24HrMarketController coin24HrMarketController) {
        this.coin24HrMarketController = coin24HrMarketController;
    }
    
    public KlineDataController getKlineDataController() {
        return klineDataController;
    }

    public void setKlineDataController(KlineDataController klineDataController) {
        this.klineDataController = klineDataController;
    }

    public CoinStatsHistoryController getCoinStatsHistoryController() {
        return coinStatsHistoryController;
    }

    public void setCoinStatsHistoryController(CoinStatsHistoryController coinStatsHistoryController) {
        this.coinStatsHistoryController = coinStatsHistoryController;
    }

    public MarketEventController getMarketEventController() {
        return marketEventController;
    }

    public void setMarketEventController(MarketEventController marketEventController) {
        this.marketEventController = marketEventController;
    }

    public SignalController getFuturesSignalController() {
        return futuresSignalController;
    }

    public void setFuturesSignalController(SignalController futuresSignalController) {
        this.futuresSignalController = futuresSignalController;
    }

    public SignalCommandController getFuturesSignalCommandController() {
        return futuresSignalCommandController;
    }

    public void setFuturesSignalCommandController(SignalCommandController futuresSignalCommandController) {
        this.futuresSignalCommandController = futuresSignalCommandController;
    }

    public StrategyController getStrategyController() {
        return strategyController;
    }

    public void setStrategyController(StrategyController strategyController) {
        this.strategyController = strategyController;
    }

    public StrategyConditionsController getStrategyConditionsController() {
        return strategyConditionsController;
    }

    public void setStrategyConditionsController(StrategyConditionsController strategyConditionsController) {
        this.strategyConditionsController = strategyConditionsController;
    }

    public ConfigsController getConfigController() {
        return configsController;
    }

    public void setConfigController(ConfigsController configsController) {
        this.configsController = configsController;
    }
}

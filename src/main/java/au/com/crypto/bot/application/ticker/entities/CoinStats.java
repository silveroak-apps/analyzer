package au.com.crypto.bot.application.ticker.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Transactional
@Table(name = "COIN_STATS")
public class CoinStats {

    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column (name = "symbol")
    private String symbol;

    @Column(name = "div_dip")
    private double divDip;

    @Column (name = "market")
    private String market;

    @Column(name = "moving_agv60min")
    private double movingAgv;

    @Column (name = "change45min")
    private double change45Min;

    @Column(name = "standard_deviation")
    private double standardDeviation;

    @Column (name = "current_price")
    private double currentPrice;

    @Column(name = "bolen_avg")
    private double bolenAvg;

    @Column (name = "bolen_high")
    private double bolenHigh;

    @Column (name = "bolen_low")
    private double bolenLow;

    @Column (name = "coin_score")
    private double coinScore;

    @Column (name = "tick_high")
    private double tickHigh;

    @Column (name = "tick_low")
    private double tickLow;

    @Column (name = "time")
    private Date time;

    @Column (name = "last_sold")
    private double lastSold;

    @Column (name = "lowest60Min")
    private double lowest60Min;

    @Column (name = "first_resistance")
    private double firstResistance;

    @Column (name = "second_resistance")
    private double secondResistance;

    @Column (name = "first_support")
    private double firstSupport;

    @Column (name = "second_support")
    private double secondSupport;

    @Column (name = "curr_by_bolenger")
    private double currByBolenger;

    @Column (name = "curr_by_lowest")
    private double currByLowest;

    @Column (name = "quote_volume")
    private double quoteVolume;

    @Column (name = "market_cap")
    private double marketCap;

    @Column (name = "waited_average")
    private double waitedAverage;

    @Column (name = "rsi")
    private double RSI;

    @Column (name = "smallema")
    private double smallEMA;

    @Column (name = "medema")
    private double medEMA;

    @Column (name = "largeema")
    private double largeEMA;

    @Column (name = "macd_signal_nine")
    private Double macdSignalNine;

    @Column (name = "macd_signal_list")
    private String macdSignalList;

    @Column (name = "ema_large_list")
    private String emaLargeList;

    @Column (name = "ema_small_list")
    private String emaSmallList;

    @Column (name = "ema_med_list")
    private String emaMedList;

    @Column (name = "tic_type")
    private String ticType;

    @Column (name = "signal_analyzed")
    private String signalAnalyzed;

    @Column (name = "moving_averageb2")
    private Double movingAverageUntilLastHalfAnHour;

    @Column (name = "moving_averageb3")
    private Double movingAverageUntilLastOneHours;

    @Column (name = "moving_avg_two_hrs")
    private Double movingAverageUntilLastTwoHours;

    @Column (name = "moving_average30min")
    private Double movingAverage30Min;

    @Column (name = "reject_reason")
    private String rejectReason;

    @Column (name = "tick_high_24hr")
    private BigDecimal tickHigh24Hr;

    @Column (name = "tick_low_24hr")
    private BigDecimal tickLow24Hr;


    @Column (name = "last_mins_neg_flow")
    private Integer lastMinNegFlow;

    @Column (name = "price_movement_percent")
    private Double priceMovementPercent;

    @Column(name = "exchange_id")
    private Long exchangeId;

    @Column(name = "ema5")
    private Double ema5;

    @Column(name = "ema10")
    private Double ema10;

    @Column(name = "ema20")
    private Double ema20;

    @Column(name = "ema30")
    private Double ema30;

    @Column(name = "ema50")
    private Double ema50;

    @Column(name = "ema100")
    private Double ema100;

    @Column(name = "ema200")
    private Double ema200;

    @Column(name = "sma5")
    private Double sma5;

    @Column(name = "sma10")
    private Double sma10;

    @Column(name = "sma20")
    private Double sma20;

    @Column(name = "sma30")
    private Double sma30;

    @Column(name = "sma50")
    private Double sma50;

    @Column(name = "sma100")
    private Double sma100;

    @Column(name = "sma200")
    private Double sma200;

    public double getDivDip() {
        return divDip;
    }

    public void setDivDip(double divDip) {
        this.divDip = divDip;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public double getMovingAgv() {
        return movingAgv;
    }

    public void setMovingAgv(double movingAgv) {
        this.movingAgv = movingAgv;
    }

    public double getChange45Min() {
        return change45Min;
    }

    public void setChange45Min(double change45Min) {
        this.change45Min = change45Min;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getBolenAvg() {
        return bolenAvg;
    }

    public void setBolenAvg(double bolenAvg) {
        this.bolenAvg = bolenAvg;
    }

    public double getBolenHigh() {
        return bolenHigh;
    }

    public void setBolenHigh(double bolenHigh) {
        this.bolenHigh = bolenHigh;
    }

    public double getBolenLow() {
        return bolenLow;
    }

    public void setBolenLow(double bolenLow) {
        this.bolenLow = bolenLow;
    }

    public double getCoinScore() {
        return coinScore;
    }

    public void setCoinScore(double coinScore) {
        this.coinScore = coinScore;
    }

    public double getTickHigh() {
        return tickHigh;
    }

    public void setTickHigh(double tickHigh) {
        this.tickHigh = tickHigh;
    }

    public double getTickLow() {
        return tickLow;
    }

    public void setTickLow(double tickLow) {
        this.tickLow = tickLow;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getLastSold() {
        return lastSold;
    }

    public void setLastSold(double lastSold) {
        this.lastSold = lastSold;
    }

    public double getLowest60Min() {
        return lowest60Min;
    }

    public void setLowest60Min(double lowest60Min) {
        this.lowest60Min = lowest60Min;
    }

    public double getFirstResistance() {
        return firstResistance;
    }

    public void setFirstResistance(double firstResistance) {
        this.firstResistance = firstResistance;
    }

    public double getSecondResistance() {
        return secondResistance;
    }

    public void setSecondResistance(double secondResistance) {
        this.secondResistance = secondResistance;
    }

    public double getFirstSupport() {
        return firstSupport;
    }

    public void setFirstSupport(double firstSupport) {
        this.firstSupport = firstSupport;
    }

    public double getSecondSupport() {
        return secondSupport;
    }

    public void setSecondSupport(double secondSupport) {
        this.secondSupport = secondSupport;
    }

    public double getCurrByBolenger() {
        return currByBolenger;
    }

    public void setCurrByBolenger(double currByBolenger) {
        this.currByBolenger = currByBolenger;
    }

    public double getCurrByLowest() {
        return currByLowest;
    }

    public void setCurrByLowest(double currByLowest) {
        this.currByLowest = currByLowest;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(double quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getWaitedAverage() {
        return waitedAverage;
    }

    public void setWaitedAverage(double waitedAverage) {
        this.waitedAverage = waitedAverage;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Double getMovingAverageUntilLastHalfAnHour() {
        return movingAverageUntilLastHalfAnHour;
    }

    public void setMovingAverageUntilLastHalfAnHour(Double movingAverageUntilLastHalfAnHour) {
        this.movingAverageUntilLastHalfAnHour = movingAverageUntilLastHalfAnHour;
    }

    public Double getMovingAverageUntilLastOneHours() {
        return movingAverageUntilLastOneHours;
    }

    public void setMovingAverageUntilLastOneHours(Double movingAverageUntilLastOneHours) {
        this.movingAverageUntilLastOneHours = movingAverageUntilLastOneHours;
    }

    public Double getMovingAverage30Min() {
        return movingAverage30Min;
    }

    public void setMovingAverage30Min(Double movingAverage30Min) {
        this.movingAverage30Min = movingAverage30Min;
    }

    public BigDecimal getTickHigh24Hr() {
        return tickHigh24Hr;
    }

    public void setTickHigh24Hr(BigDecimal tickHigh24Hr) {
        this.tickHigh24Hr = tickHigh24Hr;
    }

    public BigDecimal getTickLow24Hr() {
        return tickLow24Hr;
    }

    public void setTickLow24Hr(BigDecimal tickLow24Hr) {
        this.tickLow24Hr = tickLow24Hr;
    }

    public Double getMovingAverageUntilLastTwoHours() {
        return movingAverageUntilLastTwoHours;
    }

    public void setMovingAverageUntilLastTwoHours(Double movingAverageUntilLastTwoHours) {
        this.movingAverageUntilLastTwoHours = movingAverageUntilLastTwoHours;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public void copy(CoinStats coinStats) {
        this.setDivDip(coinStats.getDivDip());
        this.setTime(coinStats.getTime());
        this.setBolenAvg(coinStats.getBolenAvg());
        this.setBolenHigh(coinStats.getBolenHigh());
        this.setBolenLow(coinStats.getBolenLow());
        this.setChange45Min(coinStats.getChange45Min());
        this.setCoinScore(coinStats.getCoinScore());
        this.setCurrByBolenger(coinStats.getCurrByBolenger());
        this.setCurrByLowest(coinStats.getCurrByLowest());
        this.setCurrentPrice(coinStats.getCurrentPrice());
        this.setFirstResistance(coinStats.getFirstResistance());
        this.setSecondResistance(coinStats.getSecondResistance());
        this.setFirstSupport(coinStats.getFirstSupport());
        this.setSecondSupport(coinStats.getSecondSupport());
        this.setQuoteVolume(coinStats.getQuoteVolume());
        this.setMarketCap(coinStats.getMarketCap());
        this.setWaitedAverage(coinStats.getWaitedAverage());
        this.setRSI(coinStats.getRSI());
        this.setSmallEMA(coinStats.getSmallEMA());
        this.setMedEMA(coinStats.getMedEMA());
        this.setLargeEMA(coinStats.getLargeEMA());
        this.setMacdSignalNine(coinStats.getMacdSignalNine());
        this.setMacdSignalList(coinStats.getMacdSignalList());
        this.setEmaLargeList(coinStats.getEmaLargeList());
        this.setEmaMedList(coinStats.getEmaMedList());
        this.setEmaSmallList(coinStats.getEmaSmallList());
        this.setTicType(coinStats.getTicType());
        this.setSignalAnalyzed(coinStats.getSignalAnalyzed());
        this.setMovingAverageUntilLastHalfAnHour(coinStats.getMovingAverageUntilLastHalfAnHour());
        this.setMovingAverageUntilLastOneHours(coinStats.getMovingAverageUntilLastOneHours());
        this.setRejectReason(coinStats.getRejectReason());
        this.setMovingAverage30Min(coinStats.getMovingAverage30Min());
        this.setMovingAgv(coinStats.getMovingAgv());
        this.setTickHigh24Hr(coinStats.getTickHigh24Hr());
        this.setTickLow24Hr(coinStats.getTickLow24Hr());
        this.setMovingAverageUntilLastTwoHours(coinStats.getMovingAverageUntilLastTwoHours());
        this.setLastMinNegFlow(coinStats.getLastMinNegFlow());
        this.setPriceMovementPercent(coinStats.getPriceMovementPercent());
        this.setEma5(coinStats.getEma5());
        this.setEma10(coinStats.getEma10());
        this.setEma20(coinStats.getEma20());
        this.setEma30(coinStats.getEma30());
        this.setEma50(coinStats.getEma50());
        this.setEma100(coinStats.getEma100());
        this.setEma200(coinStats.getEma200());

        this.setSma5(coinStats.getSma5());
        this.setSma10(coinStats.getSma10());
        this.setSma20(coinStats.getSma20());
        this.setSma30(coinStats.getSma30());
        this.setSma50(coinStats.getSma50());
        this.setSma100(coinStats.getSma100());
        this.setSma200(coinStats.getSma200());
    }

    public void setRSI(double RSI) {
        this.RSI = RSI;
    }

    public double getRSI() {
        return RSI;
    }

    public long getId() {
        return id;
    }

    public double getSmallEMA() {
        return smallEMA;
    }

    public void setSmallEMA(double smallEMA) {
        this.smallEMA = smallEMA;
    }

    public double getMedEMA() {
        return medEMA;
    }

    public void setMedEMA(double medEMA) {
        this.medEMA = medEMA;
    }

    public double getLargeEMA() {
        return largeEMA;
    }

    public void setLargeEMA(double largeEMA) {
        this.largeEMA = largeEMA;
    }

    public Double getMacdSignalNine() {
        return macdSignalNine;
    }

    public void setMacdSignalNine(Double macdSignalNine) {
        this.macdSignalNine = macdSignalNine;
    }

    public String getMacdSignalList() {
        return macdSignalList;
    }

    public void setMacdSignalList(String macdSignalList) {
        this.macdSignalList = macdSignalList;
    }

    public String getEmaLargeList() {
        return emaLargeList;
    }

    public void setEmaLargeList(String emaLargeList) {
        this.emaLargeList = emaLargeList;
    }

    public String getEmaSmallList() {
        return emaSmallList;
    }

    public void setEmaSmallList(String emaSmallList) {
        this.emaSmallList = emaSmallList;
    }

    public String getEmaMedList() {
        return emaMedList;
    }

    public void setEmaMedList(String emaMedList) {
        this.emaMedList = emaMedList;
    }

    public String getTicType() {
        return ticType;
    }

    public void setTicType(String ticType) {
        this.ticType = ticType;
    }

    public String getSignalAnalyzed() {
        return signalAnalyzed;
    }

    public void setSignalAnalyzed(String signalAnalyzed) {
        this.signalAnalyzed = signalAnalyzed;
    }

    public Integer getLastMinNegFlow() {
        return lastMinNegFlow;
    }

    public void setLastMinNegFlow(Integer lastMinNegFlow) {
        this.lastMinNegFlow = lastMinNegFlow;
    }

    public Double getPriceMovementPercent() {
        return priceMovementPercent;
    }

    public void setPriceMovementPercent(Double priceMovementPercent) {
        this.priceMovementPercent = priceMovementPercent;
    }

    public Double getEma5() {
        return ema5;
    }

    public void setEma5(Double ema5) {
        this.ema5 = ema5;
    }

    public Double getEma10() {
        return ema10;
    }

    public void setEma10(Double ema10) {
        this.ema10 = ema10;
    }

    public Double getEma20() {
        return ema20;
    }

    public void setEma20(Double ema20) {
        this.ema20 = ema20;
    }

    public Double getEma30() {
        return ema30;
    }

    public void setEma30(Double ema30) {
        this.ema30 = ema30;
    }

    public Double getEma50() {
        return ema50;
    }

    public void setEma50(Double ema50) {
        this.ema50 = ema50;
    }

    public Double getEma100() {
        return ema100;
    }

    public void setEma100(Double ema100) {
        this.ema100 = ema100;
    }

    public Double getEma200() {
        return ema200;
    }

    public void setEma200(Double ema200) {
        this.ema200 = ema200;
    }

    public Double getSma5() {
        return sma5;
    }

    public void setSma5(Double sma5) {
        this.sma5 = sma5;
    }

    public Double getSma10() {
        return sma10;
    }

    public void setSma10(Double sma10) {
        this.sma10 = sma10;
    }

    public Double getSma20() {
        return sma20;
    }

    public void setSma20(Double sma20) {
        this.sma20 = sma20;
    }

    public Double getSma30() {
        return sma30;
    }

    public void setSma30(Double sma30) {
        this.sma30 = sma30;
    }

    public Double getSma50() {
        return sma50;
    }

    public void setSma50(Double sma50) {
        this.sma50 = sma50;
    }

    public Double getSma100() {
        return sma100;
    }

    public void setSma100(Double sma100) {
        this.sma100 = sma100;
    }

    public Double getSma200() {
        return sma200;
    }

    public void setSma200(Double sma200) {
        this.sma200 = sma200;
    }
}

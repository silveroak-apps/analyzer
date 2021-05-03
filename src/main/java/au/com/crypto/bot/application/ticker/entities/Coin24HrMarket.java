package au.com.crypto.bot.application.ticker.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by Sailaja on 2/23/2018.
 * <p>
 * /**
 * An aggregated trade event for a symbol.
 * "e": "24hrTicker",  // Event type
 * "E": 123456789,     // Event time
 * "s": "BNBBTC",      // Symbol
 * "p": "0.0015",      // Price change
 * "P": "250.00",      // Price change percent
 * "w": "0.0018",      // Weighted average price
 * "x": "0.0009",      // Previous day's close price
 * "c": "0.0025",      // Current day's close price
 * "Q": "10",          // Close trade's quantity
 * "b": "0.0024",      // Best bid price
 * "B": "10",          // Best bid quantity
 * "a": "0.0026",      // Best ask price
 * "A": "100",         // Best ask quantity
 * "o": "0.0010",      // Open price
 * "h": "0.0025",      // High price
 * "l": "0.0010",      // Low price
 * "v": "10000",       // Total traded base asset volume
 * "q": "18",          // Total traded quote asset volume
 * "O": 0,             // Statistics open time
 * "C": 86400000,      // Statistics close time
 * "F": 0,             // First trade ID (not recording)
 * "L": 18150,         // Last trade Id (not recordign)
 * "n": 18151          // Total number of trades
 */

@Entity
@Transactional
@Table(name = "COIN_24HR_MARKET")
public class Coin24HrMarket implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name ="event_type")
    private String eventType;

    @Column(name = "event_time")
    private double eventTime;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "price_change")
    private double priceChange;

    @Column(name = "price_percentage_change")
    private double pricePercentageChange;

    @Column(name = "weighted_average")
    private double weightedAverage;

    @Column(name = "previous_day_close")
    private double previousDayClose;

    @Column(name = "current_day_close")
    private double currentDayClose;

    @Column(name = "close_trade_quantity")
    private double closeTradeQuantity;


    @Column(name = "best_bid_price")
    private double bestBidPrice;

    @Column(name = "best_bid_quantity_price")
    private double bestBidQuantityPrice;

    @Column(name = "best_ask_price")
    private double bestAskPrice;

    @Column(name = "best_ask_price_quantity")
    private double bestAskPriceQuantity;

    @Column(name = "open_price")
    private double openPrice;

    @Column(name = "high_price")
    private double highPrice;

    @Column(name = "low_price")
    private double lowPrice;

    @Column(name = "total_trage_base_asset_volume")
    private double totalTrageBaseAssetVolume;

    @Column(name = "total_trage_quote_asset_volume")
    private double totalTrageQuoteAssetVolume;

    @Column(name = "insert_date",columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;

    @Column (name = "total_trades")
    private double totalTrades;

    @Column(name = "exchange_id")
    private Long exchangeId;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getEventTime() {
        return eventTime;
    }

    public void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPricePercentageChange() {
        return pricePercentageChange;
    }

    public void setPricePercentageChange(double pricePercentageChange) {
        this.pricePercentageChange = pricePercentageChange;
    }

    public double getWeightedAverage() {
        return weightedAverage;
    }

    public void setWeightedAverage(double weightedAverage) {
        this.weightedAverage = weightedAverage;
    }

    public double getPreviousDayClose() {
        return previousDayClose;
    }

    public void setPreviousDayClose(double previousDayClose) {
        this.previousDayClose = previousDayClose;
    }

    public double getCurrentDayClose() {
        return currentDayClose;
    }

    public void setCurrentDayClose(double currentDayClose) {
        this.currentDayClose = currentDayClose;
    }

    public double getCloseTradeQuantity() {
        return closeTradeQuantity;
    }

    public void setCloseTradeQuantity(double closeTradeQuantity) {
        this.closeTradeQuantity = closeTradeQuantity;
    }

    public double getBestBidPrice() {
        return bestBidPrice;
    }

    public void setBestBidPrice(double bestBidPrice) {
        this.bestBidPrice = bestBidPrice;
    }

    public double getBestBidQuantityPrice() {
        return bestBidQuantityPrice;
    }

    public void setBestBidQuantityPrice(double bestBidQuantityPrice) {
        this.bestBidQuantityPrice = bestBidQuantityPrice;
    }

    public double getBestAskPrice() {
        return bestAskPrice;
    }

    public void setBestAskPrice(double bestAskPrice) {
        this.bestAskPrice = bestAskPrice;
    }

    public double getBestAskPriceQuantity() {
        return bestAskPriceQuantity;
    }

    public void setBestAskPriceQuantity(double bestAskPriceQuantity) {
        this.bestAskPriceQuantity = bestAskPriceQuantity;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getTotalTrageBaseAssetVolume() {
        return totalTrageBaseAssetVolume;
    }

    public void setTotalTrageBaseAssetVolume(double totalTrageBaseAssetVolume) {
        this.totalTrageBaseAssetVolume = totalTrageBaseAssetVolume;
    }

    public double getTotalTrageQuoteAssetVolume() {
        return totalTrageQuoteAssetVolume;
    }

    public void setTotalTrageQuoteAssetVolume(double totalTrageQuoteAssetVolume) {
        this.totalTrageQuoteAssetVolume = totalTrageQuoteAssetVolume;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public double getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(double totalTrades) {
        this.totalTrades = totalTrades;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public void copy(Coin24HrMarket value) {
        this.setBestAskPrice(value.getBestAskPrice());
        this.setSymbol(value.getSymbol());
        this.setBestAskPriceQuantity(value.getBestAskPriceQuantity());
        this.setBestBidPrice(value.getBestBidPrice());
        this.setBestBidQuantityPrice(value.getBestBidQuantityPrice());
        this.setCloseTradeQuantity(value.getCloseTradeQuantity());
        this.setCurrentDayClose(value.getCurrentDayClose());
        this.setEventTime(value.getEventTime());
        this.setEventType(value.getEventType());
        this.setHighPrice(value.getHighPrice());
        this.setLowPrice(value.getLowPrice());
        this.setTotalTrageBaseAssetVolume(value.getTotalTrageBaseAssetVolume());
        this.setTotalTrageQuoteAssetVolume(value.getTotalTrageQuoteAssetVolume());
        this.setOpenPrice(value.getOpenPrice());
        this.setPreviousDayClose(value.getPreviousDayClose());
        this.setPriceChange(value.getPriceChange());
        this.setWeightedAverage(value.getWeightedAverage());
        this.setPricePercentageChange(value.getPricePercentageChange());
        this.setTotalTrades(value.getTotalTrades());
        this.setExchangeId(value.getExchangeId());
    }


    public void cast(LinkedHashMap<String, Object> value) {
        setSymbol(String.valueOf(value.get("s")));
        setBestAskPrice(Double.parseDouble(value.get("a").toString()));
        setEventType(String.valueOf(value.get("e").toString()));
        setEventTime(Double.parseDouble(value.get("E").toString()));
        setPriceChange(Double.parseDouble(value.get("p").toString()));
        setPricePercentageChange(Double.parseDouble(value.get("P").toString()));
        setWeightedAverage(Double.parseDouble(value.get("w").toString()));
        setPreviousDayClose(Double.parseDouble(value.get("x").toString()));
        setCurrentDayClose(Double.parseDouble(value.get("c").toString()));
        setCloseTradeQuantity(Double.parseDouble(value.get("Q").toString()));
        setBestBidPrice(Double.parseDouble(value.get("b").toString()));
        setBestBidQuantityPrice(Double.parseDouble(value.get("B").toString()));
        setBestAskPrice(Double.parseDouble(value.get("a").toString()));
        setBestAskPriceQuantity(Double.parseDouble(value.get("A").toString()));
        setOpenPrice(Double.parseDouble(value.get("o").toString()));
        setHighPrice(Double.parseDouble(value.get("h").toString()));
        setLowPrice(Double.parseDouble(value.get("l").toString()));
        setTotalTrageQuoteAssetVolume(Double.parseDouble(value.get("q").toString()));
        setTotalTrageBaseAssetVolume(Double.parseDouble(value.get("v").toString()));
        setTotalTrades(Double.parseDouble(value.get("n").toString()));
        setExchangeId(Long.valueOf(1));
    }

    @Override
    public String toString() {
        return String.format("24Hr market[id=%d, symbol='%s', price='%f', Date='%tI:%tM:%tS %Tp'", id, symbol, priceChange, insertDate, insertDate, insertDate, insertDate);
    }

}

package au.com.crypto.bot.application.ticker.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import au.com.crypto.bot.application.ticker.entities.Coin24HrMarket;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Transactional
@Table(name = "TICKER")
public class Ticker implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "INSERT_DATE", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;

    @Column (name = "TIME_FRAME")
    private String timeFrame;

    @Column(name="exchange_id")
    private Long exchangeId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_time")
    private Double eventTime;

    @Column(name = "price_change")
    private Double priceChange;

    @Column(name = "price_percentage_change")
    private Double pricePercentageChange;

    @Column(name = "weighted_average")
    private Double weightedAverage;

    @Column(name = "previous_day_close")
    private Double previousDayClose;

    @Column(name = "current_day_close")
    private Double currentDayClose;

    @Column(name = "close_trade_quantity")
    private Double closeTradeQuantity;


    @Column(name = "best_bid_price")
    private Double bestBidPrice;

    @Column(name = "best_bid_quantity_price")
    private Double bestBidQuantityPrice;

    @Column(name = "best_ask_price")
    private Double bestAskPrice;

    @Column(name = "best_ask_price_quantity")
    private Double bestAskPriceQuantity;

    @Column(name = "open_price")
    private Double openPrice;

    @Column(name = "high_price")
    private Double highPrice;

    @Column(name = "low_price")
    private Double lowPrice;

    @Column(name = "total_trage_base_asset_volume")
    private Double totalTrageBaseAssetVolume;

    @Column(name = "total_trage_quote_asset_volume")
    private Double totalTrageQuoteAssetVolume;

    @Column(name = "total_trades")
    private Double totalTrades;

    public Ticker() {
    }

    public Ticker(String symbol, double price, Date insertDate, String timeFrame, Long exchangeId) {
        this.symbol = symbol;
        this.price = price;
        this.insertDate = insertDate;
        this.timeFrame = timeFrame;
        this.exchangeId = exchangeId;
    }

    @Override
    public String toString() {
        return String.format("Ticker[id=%d, symbol='%s', price='%f', Date='%tI:%tM:%tS %Tp'", id, symbol, price,insertDate,insertDate,insertDate,insertDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Double getEventTime() {
        return eventTime;
    }

    public void setEventTime(Double eventTime) {
        this.eventTime = eventTime;
    }

    public Double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Double priceChange) {
        this.priceChange = priceChange;
    }

    public Double getPricePercentageChange() {
        return pricePercentageChange;
    }

    public void setPricePercentageChange(Double pricePercentageChange) {
        this.pricePercentageChange = pricePercentageChange;
    }

    public Double getWeightedAverage() {
        return weightedAverage;
    }

    public void setWeightedAverage(Double weightedAverage) {
        this.weightedAverage = weightedAverage;
    }

    public Double getPreviousDayClose() {
        return previousDayClose;
    }

    public void setPreviousDayClose(Double previousDayClose) {
        this.previousDayClose = previousDayClose;
    }

    public Double getCurrentDayClose() {
        return currentDayClose;
    }

    public void setCurrentDayClose(Double currentDayClose) {
        this.currentDayClose = currentDayClose;
    }

    public Double getCloseTradeQuantity() {
        return closeTradeQuantity;
    }

    public void setCloseTradeQuantity(Double closeTradeQuantity) {
        this.closeTradeQuantity = closeTradeQuantity;
    }

    public Double getBestBidPrice() {
        return bestBidPrice;
    }

    public void setBestBidPrice(Double bestBidPrice) {
        this.bestBidPrice = bestBidPrice;
    }

    public Double getBestBidQuantityPrice() {
        return bestBidQuantityPrice;
    }

    public void setBestBidQuantityPrice(Double bestBidQuantityPrice) {
        this.bestBidQuantityPrice = bestBidQuantityPrice;
    }

    public Double getBestAskPrice() {
        return bestAskPrice;
    }

    public void setBestAskPrice(Double bestAskPrice) {
        this.bestAskPrice = bestAskPrice;
    }

    public Double getBestAskPriceQuantity() {
        return bestAskPriceQuantity;
    }

    public void setBestAskPriceQuantity(Double bestAskPriceQuantity) {
        this.bestAskPriceQuantity = bestAskPriceQuantity;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getTotalTrageBaseAssetVolume() {
        return totalTrageBaseAssetVolume;
    }

    public void setTotalTrageBaseAssetVolume(Double totalTrageBaseAssetVolume) {
        this.totalTrageBaseAssetVolume = totalTrageBaseAssetVolume;
    }

    public Double getTotalTrageQuoteAssetVolume() {
        return totalTrageQuoteAssetVolume;
    }

    public void setTotalTrageQuoteAssetVolume(Double totalTrageQuoteAssetVolume) {
        this.totalTrageQuoteAssetVolume = totalTrageQuoteAssetVolume;
    }

    public Double getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(Double totalTrades) {
        this.totalTrades = totalTrades;
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
        this.setPrice(value.getBestAskPrice());
    }

    public Coin24HrMarket getCoin24HrMarket() {
        Coin24HrMarket coin24HrMarket = new Coin24HrMarket();
        coin24HrMarket.setBestAskPrice(this.getBestAskPrice());
        coin24HrMarket.setSymbol(this.getSymbol());
        coin24HrMarket.setBestAskPriceQuantity(this.getBestAskPriceQuantity());
        coin24HrMarket.setBestBidPrice(this.getBestBidPrice());
        coin24HrMarket.setBestBidQuantityPrice(this.getBestBidQuantityPrice());
        coin24HrMarket.setCloseTradeQuantity(this.getCloseTradeQuantity());
        coin24HrMarket.setCurrentDayClose(this.getCurrentDayClose());
        coin24HrMarket.setEventTime(this.getEventTime());
        coin24HrMarket.setEventType(this.getEventType());
        coin24HrMarket.setHighPrice(this.getHighPrice());
        coin24HrMarket.setLowPrice(this.getLowPrice());
        coin24HrMarket.setTotalTrageBaseAssetVolume(this.getTotalTrageBaseAssetVolume());
        coin24HrMarket.setTotalTrageQuoteAssetVolume(this.getTotalTrageQuoteAssetVolume());
        coin24HrMarket.setOpenPrice(this.getOpenPrice());
        coin24HrMarket.setPreviousDayClose(this.getPreviousDayClose());
        coin24HrMarket.setPriceChange(this.getPriceChange());
        coin24HrMarket.setWeightedAverage(this.getWeightedAverage());
        coin24HrMarket.setPricePercentageChange(this.getPricePercentageChange());
        coin24HrMarket.setTotalTrades(this.getTotalTrades());
        coin24HrMarket.setExchangeId(this.getExchangeId());
        return coin24HrMarket;
    }
}

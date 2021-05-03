package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Table(name = "positive_signal")
public class PositiveSignal implements Serializable {

    private static final long serialVersionUID = -3009157732242241506L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long signal_id;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column (name = "buy_price")
    private BigDecimal buyPrice;

    @Column (name = "sell_price")
    private BigDecimal sellPrice;

    @Column (name= "status")
    private String status;

    @Column (name = "actual_sell_price")
    private BigDecimal actualSellPrice;

    @Column (name = "buy_signal_date_time",columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date signalDate;

    @Column (name = "sell_signal_date_time",columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sellSignalDateTime;

    @Column (name = "sell_date_time",columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sellDate;

    @Column (name = "buy_date_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date buyDate;

    @Column (name ="strategy")
    private String strategy;

    @Column (name = "actual_buy_price")
    private BigDecimal actualBuyPrice;

    @Column (name = "market")
    private String market;

    @Column(name = "exchange_id")
    private Long exchangeId;

    @Column (name = "usemargin")
    private boolean useMargin;

    @Column (name = "requested_sell_amount")
    private BigDecimal requestedSellAmount;

    @Column (name = "requested_buy_amount")
    private BigDecimal requestedBuyAmount;

    public PositiveSignal() {
    }

    public PositiveSignal(String symbol,BigDecimal buyPrice, BigDecimal sellPrice,  String status) {

        this.symbol = symbol;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("PositiveSignal [id=%d, symbol='%s', buyPrice='%f', sellPrice = %f,  profitOrLossSoFar = %f, profitPercent = %f, status = '%s', strategy = '%s']", signal_id,symbol, buyPrice, sellPrice, status, strategy);
    }

    public Date getSellSignalDateTime() {
        return sellSignalDateTime;
    }

    public void setSellSignalDateTime(Date sellSignalDateTime) {
        this.sellSignalDateTime = sellSignalDateTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getSignal_id() {
        return signal_id;
    }

    public void setSignal_id(long signal_id) {
        this.signal_id = signal_id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getActualSellPrice() {
        return actualSellPrice;
    }

    public void setActualSellPrice(BigDecimal actualSellPrice) {
        this.actualSellPrice = actualSellPrice;
    }

    public Date getSignalDate() {
        return signalDate;
    }

    public void setSignalDate(Date signalDate) {
        this.signalDate = signalDate;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

//    public long getSignalInfoId() {
//        return signalInfoId;
//    }
//
//    public void setSignalInfoId(long signalInfoId) {
//        this.signalInfoId = signalInfoId;
//    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getStrategy() {
        return strategy;
    }
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public BigDecimal getActualBuyPrice() {
        return actualBuyPrice;
    }

    public void setActualBuyPrice(BigDecimal actualBuyPrice) {
        this.actualBuyPrice = actualBuyPrice;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public boolean isUseMargin() {
        return useMargin;
    }

    public void setUseMargin(boolean useMargin) {
        this.useMargin = useMargin;
    }

    public BigDecimal getRequestedSellAmount() {
        return requestedSellAmount;
    }

    public void setRequestedSellAmount(BigDecimal requestedSellAmount) {
        this.requestedSellAmount = requestedSellAmount;
    }

    public BigDecimal getRequestedBuyAmount() {
        return requestedBuyAmount;
    }

    public void setRequestedBuyAmount(BigDecimal requestedBuyAmount) {
        this.requestedBuyAmount = requestedBuyAmount;
    }

    public void copy(PositiveSignal pos) {
        this.setMarket(pos.getMarket());
        this.setSignal_id(pos.getSignal_id());
        this.setStrategy(pos.getStrategy());
        this.setSignalDate(pos.getSignalDate());
        this.setSellDate(pos.getSellDate());
        this.setStatus(pos.getStatus());
        this.setSymbol(pos.getSymbol());
        this.setActualBuyPrice(pos.getActualBuyPrice());
        this.setActualSellPrice(pos.getActualSellPrice());
        this.setBuyPrice(pos.getBuyPrice());
        this.setSellPrice(pos.getSellPrice());
        this.setBuyDate(pos.getBuyDate());
        this.setSellSignalDateTime(pos.getSellSignalDateTime());
        this.setExchangeId(pos.getExchangeId());
        this.setUseMargin(pos.isUseMargin());
        this.setRequestedBuyAmount(pos.getRequestedBuyAmount());
        this.setRequestedSellAmount(pos.getRequestedSellAmount());
    }
}

package au.com.crypto.bot.application.analyzer.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sailaja on 2/23/2018.
 */
@Entity
@Immutable
public class WebView implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @Column(name = "signal_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "buy_price")
    private Double buyPrice;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "sell_price")
    private Double sellPrice;

    @Column(name = "sold_gain_percent")
    private Double soldGainPercent;

    @Column(name = "current_gain_percent")
    private String currentGainPercent;

    @Column(name = "status")
    private String status;

    @Column(name = "strategy")
    private String strategy;

    @Column(name = "buy_date_time")
    private String buyDate;

    @Column(name = "sell_date_time")
    private String sellDate;

    private WebView() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getSoldGainPercent() {
        return soldGainPercent;
    }

    public void setSoldGainPercent(Double soldGainPercent) {
        this.soldGainPercent = soldGainPercent;
    }

    public String getCurrentGainPercent() {
        return currentGainPercent;
    }

    public void setCurrentGainPercent(String currentGainPercent) {
        this.currentGainPercent = currentGainPercent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

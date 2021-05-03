package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Transactional
@Table(name = "kline_data")
@IdClass(Kline_Data_PK.class)
public class KlineData implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;


    @Id
    @Column(name = "symbol")
    private String symbol;

    @Id
    @Column(name = "opentime")
    private Long opentime;

    @Column(name = "closetime")
    private Long closetime;

    @Column (name = "high")
    private BigDecimal high;

    @Column (name = "low")
    private BigDecimal low;

    @Column (name = "close")
    private BigDecimal close;

    @Column (name = "quoteassetvolume")
    private BigDecimal quoteassetvolume;

    @Column (name = "takerbuybaseassetvolume")
    private BigDecimal takerbuybaseassetvolume;

    @Column (name = "takerbuyquoteassetvolume")
    private BigDecimal takerquoteassetvolume;

    @Column (name = "numberoftrades")
    private Integer numberoftrades;


    public KlineData() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getOpentime() {
        return opentime;
    }

    public void setOpentime(Long opentime) {
        this.opentime = opentime;
    }

    public Long getClosetime() {
        return closetime;
    }

    public void setClosetime(Long closetime) {
        this.closetime = closetime;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getQuoteassetvolume() {
        return quoteassetvolume;
    }

    public void setQuoteassetvolume(BigDecimal quoteassetvolume) {
        this.quoteassetvolume = quoteassetvolume;
    }

    public BigDecimal getTakerbuybaseassetvolume() {
        return takerbuybaseassetvolume;
    }

    public void setTakerbuybaseassetvolume(BigDecimal takerbuybaseassetvolume) {
        this.takerbuybaseassetvolume = takerbuybaseassetvolume;
    }

    public BigDecimal getTakerquoteassetvolume() {
        return takerquoteassetvolume;
    }

    public void setTakerquoteassetvolume(BigDecimal takequoteassetvolume) {
        this.takerquoteassetvolume = takequoteassetvolume;
    }

    public Integer getNumberoftrades() {
        return numberoftrades;
    }

    public void setNumberoftrades(Integer numberoftrades) {
        this.numberoftrades = numberoftrades;
    }
}

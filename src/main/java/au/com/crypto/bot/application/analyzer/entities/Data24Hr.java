package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Table(name = "Data24Hr")
public class Data24Hr implements Serializable {

    private static final long serialVersionUID = -3009157732242241506L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "insertDate")
    private Date insertDate;

    @ManyToOne
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @Column(name = "priceChange")
    private Double priceChange;

    @Column(name = "priceChangePercent")
    private Double priceChangePercent;

    @Column(name = "prevClosePrice")
    private Double prevClosePrice;

    @Column(name = "weightedAvgPrice")
    private Double weightedAvgPrice;

    @Column(name = "lastPrice")
    private Double lastPrice;

    @Column(name = "lastQty")
    private Double lastQty;

    @Column(name = "bidPrice")
    private Double bidPrice;

    @Column(name = "bidQty")
    private Double bidQty;

    @Column(name = "askPrice")
    private Double askPrice;

    @Column(name = "askQty")
    private Double askQty;

    @Column(name = "openPrice")
    private Double openPrice;

    @Column(name = "highPrice")
    private Double highPrice;

    @Column(name = "lowPrice")
    private Double lowPrice;

    @Column(name = "coinVolume")
    private Double volume;

    @Column(name = "quoteVolume")
    private Double quoteVolume;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    public Data24Hr() {
    }

    public Data24Hr(Date insertDate,
                    Coin coin,
                    Double priceChange,
                    Double priceChangePercent,
                    Double weightedAvgPrice,
                    Double prevClosePrice,
                    Double lastPrice,
                    Double lastQty,
                    Double bidPrice,
                    Double bidQty,
                    Double askPrice,
                    Double askQty,
                    Double openPrice,
                    Double highPrice,
                    Double lowPrice,
                    Double  volume,
                    Double quoteVolume, Exchange exchange) {
        this.insertDate = insertDate;
        this.coin = coin;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.weightedAvgPrice = weightedAvgPrice;
        this.prevClosePrice = prevClosePrice;
                this.lastPrice = lastPrice;
                this.lastQty = lastQty;
                this.bidPrice = bidPrice;
                this.bidQty = bidQty;
                this.askPrice = askPrice;
                this.askQty = askQty;
                this.openPrice = openPrice;
                this.highPrice = highPrice;
                this.lowPrice = lowPrice;
                this. volume = volume;
                this.quoteVolume = quoteVolume;
                this.exchange = exchange;
    }

    @Override
    public String toString() {
        return String.format("dailydata[id=%d, coinRef='%s',  askPrice = '%s']", id, coin, askPrice);
    }
}

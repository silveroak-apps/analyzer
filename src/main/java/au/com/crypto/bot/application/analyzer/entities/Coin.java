package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Table(name = "Coin")
public class Coin implements Serializable {

    private static final long serialVersionUID = -3009157732242241506L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long coin_id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MORE_INFO")
    private String moreInfo;

    @Column(name = "SYMBOL")
    private String symbol;
    private Coin() {
    }

    public Coin(String name, String description, String moreInfo, String symbol) {
        this.name = name;
        this.description = description;
        this.moreInfo = moreInfo;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return String.format("Coin[id=%d, name='%s', description='%s', moreInfo = %s,  symbol = %s]", coin_id, name, description,moreInfo,symbol);
    }
}

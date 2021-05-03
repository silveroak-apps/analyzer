package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Table(name = "API")
public class API implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "API_KEY")
    private String apiKey;

    @Column(name = "API_SECRET")
    private String apiSecret;

    @ManyToOne
    @JoinColumn(name = "EXCHANGE_ID")
    private Exchange exchange;

    @Column(name = "TYPE")
    private String type;

    private API() {
    }

    public API(String apiKey, String apiSecret,Exchange exchange, String type) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.exchange = exchange;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("API[id=%d, apiKey='%s', apiSecret='%s', exchangeId = %d]", id, apiKey, apiSecret,exchange);
    }
}

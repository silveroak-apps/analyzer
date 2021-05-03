package au.com.crypto.bot.application.analyzer.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;

/*
	signal_id int8 NOT NULL,
	price numeric(18,10) NOT NULL,
	requested_amount numeric(18,10) NULL,
	signal_action numeric(18,10) NOT NULL,
	order_type varchar(255) NOT NULL,
	status varchar(255) NOT NULL,
	requestor varchar(255) NOT NULL,
	request_date_time timestamp NOT NULL,
	action_date_time timestamp NULL,
	leverage int4 NOT NULL,
	exchange_order_id int8 NULL,
 */

@Entity
@Transactional
@Table(name = "futures_signal")
public class FuturesSignal {

    private static final long serialVersionUID = -3009157732242241606L;
    private static final Logger logger = LoggerFactory.getLogger(FuturesSignal.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "signal_id")
    private long signalId;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "position_type") // LONG, SHORT
    private String positionType;

    @Column(name = "created_date_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDateTime;

    @Column(name = "exchange_id")
    private long exchangeId;

    @Column(name = "strategy_pair_name")
    private String strategyPairName;

    @Transient
    private String positionStatus;

    @Transient
    private double positionSize;


    public String getStrategyPairName() {
        return strategyPairName;
    }

    public void setStrategyPairName(String strategyPairName) {
        this.strategyPairName = strategyPairName;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getSignalId() {
        return signalId;
    }

    public void setSignalId(long signalId) {
        this.signalId = signalId;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public double getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(double positionSize) {
        this.positionSize = positionSize;
    }
}

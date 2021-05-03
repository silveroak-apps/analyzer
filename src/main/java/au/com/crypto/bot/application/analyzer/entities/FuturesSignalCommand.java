package au.com.crypto.bot.application.analyzer.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
@Table(name = "futures_signal_command")
public class FuturesSignalCommand {


    private static final long serialVersionUID = -3009157732242241606L;
    private static final Logger logger = LoggerFactory.getLogger(FuturesSignalCommand.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "signal_id")
    private long signalId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "leverage")
    private Integer leverage;

    @Column(name = "signal_action") //OPEN, CLOSE, INCREASE, DECREASE
    private String signalAction;

    @Column(name = "status")
    private String status;

    @Column(name = "request_date_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDateTime;

    @Column(name = "action_date_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDateTime;

    @Column(name = "exchange_order_id")
    private Long  exchangeOrderId;

    @Column(name = "strategy_hash")
    private String strategyHash;

    @Column(name = "strategy_name")
    private String strategyName;

    @Column(name = "strategy_data")
    private String strategyData;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSignalId() {
        return signalId;
    }

    public void setSignalId(long signalId) {
        this.signalId = signalId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSignalAction() {
        return signalAction;
    }

    public void setSignalAction(String signalAction) {
        this.signalAction = signalAction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public Date getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(Date actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public Long getExchangeOrderId() {
        return exchangeOrderId;
    }

    public void setExchangeOrderId(Long exchangeOrderId) {
        this.exchangeOrderId = exchangeOrderId;
    }

    public String getStrategyHash() {
        return strategyHash;
    }

    public void setStrategyHash(String strategyHash) {
        this.strategyHash = strategyHash;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyData() {
        return strategyData;
    }

    public void setStrategyData(String strategyData) {
        this.strategyData = strategyData;
    }
}

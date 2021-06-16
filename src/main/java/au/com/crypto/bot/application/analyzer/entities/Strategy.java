package au.com.crypto.bot.application.analyzer.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;

/*
id int8 default nextval('strategy_conditions_id_seq'::regclass) not null
		constraint strategy_conditions_pkey
			primary key,
	strategy_id int8 not null,
    name varchar(255) not null,
    time_frame int8 not null,
    last_observed int8 not null,
    category varchar(255) not null,
	condition_group varchar(255) not null, -- OPEN / CLOSE
	created_time timestamp not null,
    sequence int8 not null,
	version int8 not null,
 */

@Entity
@Transactional
@Table(name = "strategy")
public class Strategy {

    private static final long serialVersionUID = -3009157732242241606L;
    private static final Logger logger = LoggerFactory.getLogger(Strategy.class);

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "position_type") // LONG, SHORT
    private String positionType;

    @Column(name = "exchange_type") // LONG, SHORT
    private String exchangeType;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "status")
    private String status;

    @Column(name = "version")
    private long version;

    @Column(name = "exchange_id")
    private long exchangeId;

    @Column(name = "created_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "updated_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(long exchangeId) {
        this.exchangeId = exchangeId;
    }
}

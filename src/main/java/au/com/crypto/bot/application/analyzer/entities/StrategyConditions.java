package au.com.crypto.bot.application.analyzer.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;

/*
create table strategy (
	id int8 default nextval('strategy_id_seq'::regclass) not null
		constraint strategy_pkey
			primary key,
	name varchar(255) not null,
	position_type varchar(255) not null,
	exchange_type varchar(255) not null,
	symbol varchar not null,
	version int8 not null,
	status varchar(54) not null,
	updated_time timestamp not null,
	created_time timestamp not null,
	UNIQUE (name)
);
 */

@Entity
@Transactional
@Table(name = "strategy_conditions")
public class StrategyConditions {

    private static final long serialVersionUID = -3009157732242241606L;
    private static final Logger logger = LoggerFactory.getLogger(StrategyConditions.class);

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "strategy_id")
    private long strategyId;

    @Column(name = "name")
    private String name;

    @Column(name = "time_frame")
    private long timeFrame;

    @Column(name = "last_observed")
    private long lastObserved;

    @Column(name = "category")
    private String category;

    @Column(name = "condition_group")
    private String conditionGroup;

    @Column(name = "sequence")
    private long sequence;

    @Column(name = "condition_sub_group")
    private long conditionSubGroup;

    @Column(name = "version")
    private long version;

    @Column(name = "created_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
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

    public long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(long strategyId) {
        this.strategyId = strategyId;
    }

    public long getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(long timeFrame) {
        this.timeFrame = timeFrame;
    }

    public long getLastObserved() {
        return lastObserved;
    }

    public void setLastObserved(long lastObserved) {
        this.lastObserved = lastObserved;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConditionGroup() {
        return conditionGroup;
    }

    public void setConditionGroup(String conditionGroup) {
        this.conditionGroup = conditionGroup;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public long getConditionSubGroup() {
        return conditionSubGroup;
    }

    public void setConditionSubGroup(long conditionSubGroup) {
        this.conditionSubGroup = conditionSubGroup;
    }
}

package au.com.crypto.bot.application.analyzer.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;

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
@Table(name = "configs")
public class Configs {

    private static final long serialVersionUID = -3009157732242241606L;
    private static final Logger logger = LoggerFactory.getLogger(Configs.class);

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "json_value")
    private String jsonValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }
}

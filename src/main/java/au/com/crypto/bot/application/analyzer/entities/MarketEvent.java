package au.com.crypto.bot.application.analyzer.entities;

import au.com.crypto.bot.application.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Transactional
@Table(name = "market_event")
public class MarketEvent implements Comparable<MarketEvent>{

    private static final long serialVersionUID = -3009157732242241606L;
    private static final Logger logger = LoggerFactory.getLogger(MarketEvent.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "source")
    private String source;

    @Column(name = "name")
    private String name;

    @Column(name = "message")
    private String message;

    @Column(name = "event_time", columnDefinition = "timestamp without time zone")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventTime;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "market")
    private String market;

    @Column(name = "category")
    private String category;

    @Column(name = "time_frame")
    private long timeframe;

    @Column(name = "contracts")
    private Integer contracts;

    @Column(name = "exchange")
    private Long exchangeId;

    @Transient
    private String exchangeName;

    @Transient
    private long eventTimeInEpoch;

    public void copy(MarketEvent marketEvent) {

        this.setEventTime((marketEvent.eventTime));
        this.setSource(marketEvent.getSource());
        this.setMarket(marketEvent.getMarket());
        this.setName(marketEvent.getName());
        this.setPrice(marketEvent.getPrice());
        this.setTimeframe(marketEvent.getTimeframe());
        this.setMessage(marketEvent.getMessage());
        this.setContracts(marketEvent.getContracts());
        this.setExchangeId(marketEvent.getExchangeId());
        this.setExchangeName(marketEvent.getExchangeName());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MarketEvent(String name, String source, String message,
                       Date eventTime, String symbol, BigDecimal price, int contracts,
                       String market, long timeframe) {
        this.setName(name);
        this.setMarket(market);
        this.setSource(source);
        this.setEventTime(eventTime);
        this.setSymbol(symbol);
        this.setMessage(message);
        this.setPrice(price);
        this.setTimeframe(timeframe);
        this.setContracts(contracts);
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public MarketEvent() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        setEventTimeInEpoch(eventTime.getTime());
        this.eventTime = eventTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public long getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(long timeframe) {
        this.timeframe = timeframe;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        Class c = MarketEvent.class;
        Method[] declaredMethods = c.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getName().startsWith("get") && !method.getName().endsWith(("List"))) {
                try {
                    str.append(method.getName().substring(3, method.getName().length() ));
                    str.append(" - ");
                    str.append(method.invoke(this) +", ");
                } catch (IllegalAccessException e) {
                    LogUtil.printLog(logger, LogUtil.STATUS.ERROR.name(), MarketEvent.class.getSimpleName(), "Exception while object ", e);
                } catch (InvocationTargetException e) {
                    LogUtil.printLog(logger, LogUtil.STATUS.ERROR.name(), MarketEvent.class.getSimpleName(), "Exception while object ", e);
                }
            }
        }
        return str.toString();
    }

    public long getEventTimeInEpoch() {
        return eventTime.getTime();
    }

    public void setEventTimeInEpoch(long eventTimeInEpoch) {
        this.eventTimeInEpoch = eventTimeInEpoch;
    }

    @Override
    public int compareTo(MarketEvent me) {
        if (getEventTime() == null || me.getEventTime() == null) {
            return 0;
            
        }
        return getEventTime().compareTo(me.getEventTime());
    }

    public Integer getContracts() {
        if (contracts == null)
            setContracts(-1);
        return contracts;
    }

    public void setContracts(Integer contracts) {
        this.contracts = contracts;
    }
}

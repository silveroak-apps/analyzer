package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class PositiveSignalController {

    @PersistenceContext(unitName = "analysisEntityManagerFactory")
    private EntityManager em;

    @Autowired
    PositiveSignalRepository repository;

    public String save(PositiveSignal signal) {
        repository.save(signal);
        return String.valueOf(signal.getSignal_id());
    }

    public void update(PositiveSignal pos) {
        PositiveSignal signal = findById(pos.getSignal_id());
        signal.copy(pos);
        repository.save(signal);
    }

    public String findAll() {
        String result = "<html>";

        for (PositiveSignal signal : repository.findAll()) {
            result += "<div>" + signal.toString() + "</div>";
        }
        return result + "</html>";
    }


    public PositiveSignal findById(long signal_id) {
        PositiveSignal result = repository.findOne(signal_id);
        return result;
    }

    public List<PositiveSignal> findAllActiveSignals(String strategy) {

        Query q = em.createNativeQuery("SELECT symbol " +
                " FROM public.positive_signal where " +
                "strategy = :strategy and " +
                "status " +
                " not in ('SELL_CANCELED', 'SIGNAL_REJECTED','BUY_CANCELED','BUY_CANCELLED','BUY_REJECTED', 'SIGNAL_LAPSED', 'SELL_FILLED','SELL_REJECTED','BUY_PARTIALLY_FILLED_CODEBUG_ABANDONED','SELL_REJECTED','SIG_INVALID')  ");
        q.setParameter("strategy", strategy);
        List<PositiveSignal> resultList = q.getResultList();
        return resultList;
    }

    public PositiveSignal findPositiveSignalBySymbol(String symbol, String strategy, Date date) {

        StringBuilder query = new StringBuilder("SELECT *" +
                " FROM public.positive_signal where symbol = :symbol  ");

        //For live - live
        //" and strategy = :strategy" +
        if (date != null)
            query.append(" and buy_signal_date_time > :date ");
        query.append(" and status not in ('SELL_CANCELED', 'SIGNAL_REJECTED','BUY_CANCELED','BUY_REJECTED','BUY_CANCELLED', 'SIGNAL_LAPSED', 'SELL_FILLED','SELL_REJECTED','BUY_PARTIALLY_FILLED_CODEBUG_ABANDONED','SELL_REJECTED', 'SIG_INVALID') ");
        Query q = em.createNativeQuery(query.toString(), PositiveSignal.class);
        q.setParameter("symbol", symbol);
        //q.setParameter("strategy", strategy);
        if (date != null)
            q.setParameter("date", date, TemporalType.TIMESTAMP);
        List<PositiveSignal> resultList = Collections.checkedList(q.getResultList(), PositiveSignal.class);
        if (resultList != null && resultList.size() > 0)
            return resultList.get(0);
        return null;
    }

    /**
     * To get the Positive Signal Object for a symbol
     * @param symbol
     * @param strategy
     * @return
     */
    public PositiveSignal findOpenSignalBySymbol(String symbol, String strategy) {

        StringBuilder query = new StringBuilder("SELECT *" +
                " FROM public.positive_signal where symbol = :symbol  ");
        query.append(" and status in ('BUY_FILLED') ");
        query.append(" and strategy = :strategy");
        Query q = em.createNativeQuery(query.toString(), PositiveSignal.class);
        q.setParameter("symbol", symbol);
        q.setParameter("strategy", strategy);
        List<PositiveSignal> resultList = Collections.checkedList(q.getResultList(), PositiveSignal.class);
        if (resultList != null && resultList.size() > 0)
            return resultList.get(0);
        return null;
    }

    public int findPositiveSignalByMarket(String market, String strategy) {


        Query q = em.createNativeQuery("SELECT *" +
                " FROM public.positive_signal where market = :market  " +
                //For live - live
                //" and strategy = :strategy " +
                " and status not in ('SELL_CANCELED', 'SIGNAL_REJECTED','BUY_CANCELED','BUY_REJECTED','BUY_CANCELLED', 'SIGNAL_LAPSED', 'SELL_FILLED','SELL_REJECTED','BUY_PARTIALLY_FILLED_CODEBUG_ABANDONED','SELL_REJECTED','SIG_INVALID') ", PositiveSignal.class);
        q.setParameter("market", market);
        //q.setParameter("strategy", strategy);
        List<PositiveSignal> resultList = Collections.checkedList(q.getResultList(), PositiveSignal.class);
        if (resultList != null && resultList.size() > 0)
            return resultList.size();
        return 0;
    }

    public List<PositiveSignal> getPositiveSignalByDate(Date fromDate, Date toDate) {
        Query q = em.createNativeQuery("SELECT  * " +
                " FROM public.positive_signal where buy_signal_date_time >= :fromDate and buy_signal_date_time < :toDate ");
        q.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP);
        q.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
        List<PositiveSignal> signals = Collections.checkedList(q.getResultList(), PositiveSignal.class);
        return signals;
    }

    public List<PositiveSignal> getAllActiveSignals() {
        Query q = em.createNativeQuery("SELECT  * " +
                "FROM public.positive_signal ", PositiveSignal.class);
        List<PositiveSignal> signals = Collections.checkedList(q.getResultList(), PositiveSignal.class);
        return signals;
    }

    public List<PositiveSignal> getAllUnprocessedSignals() {
        Query q = em.createNativeQuery("SELECT  * " +
                "FROM public.positive_signal where signal_id not in (SELECT signal_id from public.buy_analysis)", PositiveSignal.class);
        List<PositiveSignal> signals = Collections.checkedList(q.getResultList(), PositiveSignal.class);
        return signals;
    }

    public PositiveSignal getRecentSignal() {
        Query q = em.createNativeQuery("SELECT  * " +
                "FROM public.positive_signal order by buy_signal_date_time desc limit 1", PositiveSignal.class);
        List<PositiveSignal> signals = Collections.checkedList(q.getResultList(), PositiveSignal.class);
        if (signals != null && signals.size() > 0)
            return signals.get(0);
        else
            return null;
    }

}
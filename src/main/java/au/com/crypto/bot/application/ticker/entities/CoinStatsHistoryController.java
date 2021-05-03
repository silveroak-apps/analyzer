package au.com.crypto.bot.application.ticker.entities;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class CoinStatsHistoryController {

    @Autowired
    CoinStatsHistoryRepository repository;
    @PersistenceContext(unitName = "tickerEntityManagerFactory")
    private EntityManager em;

    public String save(CoinStatsHistory coinStatsHistory) {
        repository.save(coinStatsHistory);
        return "Done";
    }

    public boolean delete(CoinStatsHistory coinStats) {
        repository.delete(coinStats);
        return true;
    }

    public Iterable<CoinStatsHistory> findAll() {
        return repository.findAll();
    }


    public String findById(long id) {
        String result = "";
        result = repository.findOne(id).toString();
        return result;
    }

    public String fetchDataBySymbol(String symbol) {
        String result = "<html>";

        List<CoinStatsHistory> coinStats = repository.findSignalInfoBySymbol(symbol);
        result += "<div>" + coinStats.toString() + "</div>";


        return result + "</html>";
    }

    public CoinStatsHistory findHistoryByDates(String ticType, String symbol, Date runDate) {

        StringBuilder query = new StringBuilder("SELECT * FROM public.coin_stats_history where tic_type = :ticType and ");

        Date oneMinAdded = DateUtils.addMinutes(runDate, 1);
        query.append(" and time > :runDate");
        query.append( " and time < :oneMinAdded");
        query.append(" and symbol = :symbol");
        query.append(" limit 1");
        Query q = em.createNativeQuery(query.toString(),CoinStatsHistory.class);
        q.setParameter("ticType", ticType);
        q.setParameter("oneMinAdded",oneMinAdded);
        q.setParameter("runDate",runDate);
        q.setParameter("symbol",symbol);
        List<CoinStatsHistory> resultList = Collections.checkedList(q.getResultList(), CoinStatsHistory.class);
        if (resultList != null && resultList.size() > 0)
            return resultList.get(0);
        return null;
    }

    public List<CoinStatsHistory> findHistoryByDates(String ticType, Date runDate, Date endDate, String[] markets) {

        StringBuilder query = new StringBuilder("SELECT * FROM public.coin_stats_history where tic_type = :ticType ");

        for (int i = 0; i < markets.length; i++) {
            if (i == 0 ) {
                query.append(" and (");
            } else {
                query.append(" or ");
            }

            query.append(" ( symbol like '%" + markets[i] + "' or symbol like '" + markets[i] + "_%') ");

        }

        //Date oneMinAdded = DateUtils.addMinutes(runDate, 1);
        query.append(") and time > :runDate");
        query.append( " and time < :endDate");
        query.append(" order by time desc ");
        Query q = em.createNativeQuery(query.toString(),CoinStatsHistory.class);
        q.setParameter("ticType", ticType);
        q.setParameter("endDate", endDate);
        q.setParameter("runDate", runDate);
        List<CoinStatsHistory> resultList = Collections.checkedList(q.getResultList(), CoinStatsHistory.class);
        if (resultList != null && resultList.size() > 0)
            return resultList;
        return Collections.emptyList();
    }

}
package au.com.crypto.bot.application.ticker.entities;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import au.com.crypto.bot.application.analyzer.entities.SignalInfoRepository;

@Component
@Transactional
public class CoinStatsController {

    @Autowired
    SignalInfoRepository repository;
    @PersistenceContext(unitName = "tickerEntityManagerFactory")
    private EntityManager em;

//    public String save(CoinStats coinStats) {
//        CoinStats sigInfos = findBySymbolAndTicType(coinStats.getSymbol(), coinStats.getTicType());
//
//        if (sigInfos == null) {
//            repository.save(coinStats);
//        } else {
//
//            sigInfos.copy(coinStats);
//            repository.save(sigInfos);
//        }
//        return "Done";
//    }

    public boolean delete(CoinStats coinStats) {
        repository.delete(coinStats);
        return true;
    }

    public Iterable<CoinStats> findAll() {
        return repository.findAll();
    }


    public String findById(long id) {
        String result = "";
        result = repository.findOne(id).toString();
        return result;
    }

    public String fetchDataBySymbol(String symbol) {
        String result = "<html>";

        List<CoinStats> coinStats = repository.findSignalInfoBySymbol(symbol);
        result += "<div>" + coinStats.toString() + "</div>";


        return result + "</html>";
    }

    public CoinStats findBySymbolAndTicType(String symbol, String ticType) {
        Query q = em.createNativeQuery("SELECT *" +
                " FROM public.coin_stats where symbol = :symbol and tic_type = :ticType ", CoinStats.class);
        q.setParameter("symbol", symbol);
        q.setParameter("ticType", ticType);
        List<CoinStats> resultList = Collections.checkedList(q.getResultList(), CoinStats.class);
        if (resultList != null && resultList.size() > 0)
            return resultList.get(0);
        return null;
    }

    public List<CoinStats> findByTicType(String ticType, String[] markets) {

        StringBuilder query = new StringBuilder("SELECT * FROM public.coin_stats where (tic_type = :ticType and ");

        for (int i = 0; i < markets.length; i++) {
            query.append(" symbol like '%" + markets[i] + "') ");
            if (i < markets.length - 1) {
                query.append(" or ( tic_type = :ticType and " );
            }
        }
        Date fiveMinOld = DateUtils.addMinutes(new Date(), -5);
        query.append(" and time > " + ":fiveMinOld");
        //query.append(" and signal_analyzed != 'DONE' ");
        Query q = em.createNativeQuery(query.toString(),CoinStats.class);
                q.setParameter("ticType", ticType);
                q.setParameter("fiveMinOld",fiveMinOld);
        List<CoinStats> resultList = Collections.checkedList(q.getResultList(), CoinStats.class);
        if (resultList != null && resultList.size() > 0)
            return resultList;
        return null;
    }

}
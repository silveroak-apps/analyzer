package au.com.crypto.bot.application.ticker.entities;

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
public class TickerController {

    @Autowired
    TickerRepository repository;
    @PersistenceContext(unitName = "tickerEntityManagerFactory")
    private EntityManager em;

    //Unused just for test
    public String process() {
        repository.save(new Ticker("BNBBTC", 2.43434, new Date(), "",1L));
        //entityManager.persist(new Customer("Jack", "Smith"));
        return "Done";
    }

    public String save(Ticker ticker) {
        repository.save(ticker);
        return "Done";
    }

    public boolean delete(Ticker ticker) {
        repository.delete(ticker);
        return true;
    }

    public String findAll() {
        String result = "<html>";

        for (Ticker ticker : repository.findAll()) {
            result += "<div>" + ticker.toString() + "</div>";
        }
        return result + "</html>";
    }


    public String findById(long id) {
        String result = "";
        result = repository.findOne(id).toString();
        return result;
    }

    public String fetchDataExchangeId(String symbol) {
        String result = "<html>";

        Ticker exchange = repository.findTickersBySymbol(symbol);
        result += "<div>" + exchange.toString() + "</div>";


        return result + "</html>";
    }

    public List<String> findAllDistinct(String[] marketsArray) {

        StringBuilder query = new StringBuilder("SELECT distinct symbol FROM public.ticker ");
        if (marketsArray.length>0)
            query.append(" where ");
        for (int i = 0; i < marketsArray.length; i++) {
            query.append(" symbol like '%" + marketsArray[0] + "' ");
            query.append(" or symbol like '" + marketsArray[0] + "_%' ");
            if (i < marketsArray.length-1){
                query.append(" or ");
            }
        }

        Query q = em.createNativeQuery(query.toString());
        List<String> ticker = q.getResultList();
        return ticker;
    }

    public List<Ticker> getTickersByDate(Date fromDate, Date toDate, String symbol) {
        Query q = em.createNativeQuery("SELECT  *" +
                "FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol order by insert_date desc", Ticker.class);
        //TypedQuery<Ticker> q = em.createNativeQuery("select * FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol",Ticker.class);
        q.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP);
        q.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
        q.setParameter("symbol", symbol);

        List<Ticker> tickers = Collections.checkedList(q.getResultList(), Ticker.class);//q.getResultList();
        return tickers;
    }

    public List<Ticker> getAllTickersForAMin(Date fromDate, Date toDate, String[] markets) {

        StringBuilder query = new StringBuilder("SELECT * FROM public.ticker where ");
        for (int i = 0; i < markets.length; i++) {
            if (i == 0 ) {
                query.append(" (");
            } else {
                query.append(" or ");
            }
            query.append(" ( symbol like '%" + markets[i] + "' or symbol like '" + markets[i] + "_%') ");
        }

        query.append(") and insert_date >= :fromDate and insert_date < :toDate order by insert_date desc");

        Query q = em.createNativeQuery(query.toString(), Ticker.class);
        //TypedQuery<Ticker> q = em.createNativeQuery("select * FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol",Ticker.class);
        q.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP);
        q.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
        List<Ticker> tickers = Collections.checkedList(q.getResultList(), Ticker.class);//q.getResultList();
        return tickers;
    }

//	@org.springframework.transaction.annotation.Transactional (transactionManager = "tickerTransactionManager")
//	public boolean deleteTickersByDate(Date fromDate, Date toDate){
//		try {
//			Query q = em.createNativeQuery("Delete " +
//					"FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and time_frame = ''");
//			q.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP);
//			q.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
//			Number result = q.executeUpdate();
//			return ((result.intValue() == 1) ? true : false);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@org.springframework.transaction.annotation.Transactional (transactionManager = "tickerTransactionManager")
//	public boolean deleteTickersByTimeFrame(Date fromDate, Date toDate,String timeFrame){
//		try {
//			Query q = em.createNativeQuery("Delete " +
//					"FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and time_frame like :timeFrame");
//			q.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP);
//			q.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
//			q.setParameter("timeFrame", timeFrame);
//			Number result = q.executeUpdate();
//			return ((result.intValue() == 1) ? true : false);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		return false;
//	}

    public List<Ticker> getTickersByDateNTimeFrame(Date fifteenMinTime, Date toDate, String symbol, String timeFrame) {
        Query q = em.createNativeQuery("SELECT  *" +
                "FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol and time_frame like :timeFrame order by insert_date desc", Ticker.class);
        //TypedQuery<Ticker> q = em.createNativeQuery("select * FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol",Ticker.class);
        q.setParameter("fromDate", fifteenMinTime, TemporalType.TIMESTAMP);
        q.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
        q.setParameter("symbol", symbol);
        q.setParameter("timeFrame", timeFrame);
        List<Ticker> tickers = Collections.checkedList(q.getResultList(), Ticker.class);//q.getResultList();
        return tickers;
    }

    public boolean getTickersBySymbolAndTic(String ticType, Date lastTicTime) {
        Query q = em.createNativeQuery("SELECT  * " +
                "FROM public.ticker where time_frame like :ticType and insert_date > :lastTicTime order by insert_date desc limit 1", Ticker.class);
        //TypedQuery<Ticker> q = em.createNativeQuery("select * FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol",Ticker.class);

        //q.setParameter("symbol",symbol);
        q.setParameter("ticType", "%" + ticType + "%");
        q.setParameter("lastTicTime", lastTicTime, TemporalType.TIMESTAMP);
        List<Ticker> tickers = Collections.checkedList(q.getResultList(), Ticker.class);//q.getResultList();
        if (tickers != null && tickers.size() > 0)
            return true;
        return false;
    }

    public List<Ticker> getTickersByDateNTimeFrameWithOutSymbol(Date fifteenMinTime, Date toDate, String timeFrame, String[] markets) {

        StringBuilder query = new StringBuilder("SELECT  * FROM public.ticker where ( insert_date >= :fromDate and insert_date < :toDate  and time_frame like :timeFrame )");

        for (int i = 0; i < markets.length; i++) {
            if (i == 0 ) {
                query.append(" and (");
            } else {
                query.append(" or ");
            }

            query.append(" ( symbol like '%" + markets[i] + "' or symbol like '" + markets[i] + "_%') ");

        }
        query.append(") order by insert_date desc");
        Query q = em.createNativeQuery(query.toString(), Ticker.class);
        //TypedQuery<Ticker> q = em.createNativeQuery("select * FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol",Ticker.class);
        q.setParameter("fromDate", fifteenMinTime, TemporalType.TIMESTAMP);
        q.setParameter("toDate", toDate, TemporalType.TIMESTAMP);
        q.setParameter("timeFrame", timeFrame);
        List<Ticker> tickers = Collections.checkedList(q.getResultList(), Ticker.class);//q.getResultList();
        return tickers;
    }
}
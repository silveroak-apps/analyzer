package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

@Component
@Transactional
public class KlineDataController {

	@PersistenceContext (unitName = "tickerEntityManagerFactory")
	private EntityManager em;

	@Autowired
	KlineDataRepository repository;

	public String save(KlineData ticker){
		repository.save(ticker);
		return "Done";
	}

	public List<KlineData> getKlineDataByDateAndSymbol(BigInteger datetime, String symbol){
		Query q = em.createNativeQuery("SELECT  *" +
				"FROM public.kline_data where kline_data.opentime <= :datetime and SYMBOL = :symbol order by opentime desc limit 1441",KlineData.class);
		q.setParameter("datetime",datetime);
		q.setParameter("symbol",symbol);

		List<KlineData> klines = Collections.checkedList(q.getResultList(), KlineData.class);//q.getResultList();
		return klines;
	}
	public List<KlineData> getKlineDataForADay(BigInteger datetime){
		BigInteger starttime = datetime.add(new BigInteger("-"+String.valueOf(86400000)));
		Query q = em.createNativeQuery("SELECT  *" +
				"FROM public.kline_data where kline_data.opentime >= :starttime and kline_data.opentime <= :datetime order by opentime desc",KlineData.class);
		q.setParameter("datetime",datetime);
		q.setParameter("starttime",starttime);
		List<KlineData> klines = Collections.checkedList(q.getResultList(), KlineData.class);//q.getResultList();
		return klines;
	}
	public List<String> getDistinctSymbols(){
		Query q = em.createNativeQuery("SELECT  DISTINCT symbol " +
				"FROM public.kline_data order by symbol");
		List<String> list = (List<String>) q.getResultList();
		return list;
	}
	public int getKlineDistint(long datetime, String symbol){
		long enddatetime = datetime + (1000*60);
		Query q = em.createNativeQuery("SELECT count(*) " +
				"FROM public.kline_data where opentime >= :datetime and opentime <= :enddatetime and symbol = :symbol");
		q.setParameter("datetime",datetime);
		q.setParameter("enddatetime",enddatetime);
		q.setParameter("symbol",symbol);
		List<BigInteger> resultList = q.getResultList();
		int result = resultList.get(0).intValue();
		return result;
	}

}
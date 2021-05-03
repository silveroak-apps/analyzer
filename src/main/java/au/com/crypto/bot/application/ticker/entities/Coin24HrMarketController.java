package au.com.crypto.bot.application.ticker.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class Coin24HrMarketController {

	@PersistenceContext(unitName = "tickerEntityManagerFactory")
	private EntityManager em;
	@Autowired
	Coin24HrMarketRepository repository;

	//Unused just for test
//	public String process() {
//		repository.save(new Ticker("BNBBTC", 2.43434,new Date(), ""));
//		//entityManager.persist(new Customer("Jack", "Smith"));
//		return "Done";
//	}

	public String save(Coin24HrMarket coin24HrMarket ){
		Coin24HrMarket coinMarket =repository.findCoin24HrMarketBySymbol(coin24HrMarket.getSymbol());

		if (coinMarket == null) {
			repository.save(coin24HrMarket);
		}else{

			coinMarket.copy(coin24HrMarket);
			coinMarket.setInsertDate(new Date());
			repository.save(coinMarket);
		}
		return "Done";
	}

	public boolean delete(Coin24HrMarket coin24HrMarket){
		repository.delete(coin24HrMarket);
		return true;
	}
//	public List<Coin24HrMarket> findAll(){
//
//		List<Coin24HrMarket> coin24HrMarket = repository.findAll();
//		return result;
//	}


	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public List<Coin24HrMarket > findAllDistinct(String[] marketsArray) {

		StringBuilder query = new StringBuilder("SELECT  * FROM public.coin_24hr_market ");
		if (marketsArray.length>0)
			query.append("where ");
		for (int i = 0; i < marketsArray.length; i++) {
			query.append(" symbol like '%" + marketsArray[i] + "' ");
			query.append(" or symbol like '" + marketsArray[i] + "_%' ");
			if (i < marketsArray.length-1){
				query.append(" or ");
			}
		}
        //Always add defaults
        query.append(" or symbol = 'BTCUSDT' or symbol = 'ETHUSDT' or symbol = 'BNBUSDT'");
		query.append(" or symbol = 'USDT_BTC' or symbol = 'USDT_ETH' or symbol = 'USDT_XMR'");

		Query q = em.createNativeQuery(query.toString(),Coin24HrMarket.class);

		List<Coin24HrMarket> coinMarkets = Collections.checkedList(q.getResultList(), Coin24HrMarket.class);//q.getResultList();
		return coinMarkets;
	}

	public Date findInsertDateBySymbol() {
		Query q = em.createNativeQuery("SELECT  * " +
				"FROM public.coin_24hr_market order by insert_date desc",Coin24HrMarket.class);
		//TypedQuery<Ticker> q = em.createNativeQuery("select * FROM public.ticker where insert_date >= :fromDate and insert_date < :toDate and SYMBOL = :symbol",Ticker.class);

		List<Coin24HrMarket> markets = Collections.checkedList(q.getResultList(), Coin24HrMarket.class);//q.getResultList();
		if(markets!=null && markets.size()>0)
			return markets.get(0).getInsertDate();
		return null;
	}
}
package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class FuturesSignalController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	FuturesSignalRepository repository;

	public long save(FuturesSignal futuresSignal){
		repository.save(futuresSignal);
		return futuresSignal.getSignalId();
	}

	public boolean delete(FuturesSignal futuresSignal){
		repository.delete(futuresSignal);
		return true;
	}

	public Iterable<FuturesSignal> findAll(){
		return repository.findAll();
	}

	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public String fetchDataBySymbol( String Id){
		String result = "<html>";

		List<FuturesSignal> futuresSignal = repository.findFuturesSignalBySignalId(Id);
			result += "<div>" + futuresSignal.toString() + "</div>";
		return result + "</html>";
	}

	/**
	 * to get the find signals by symbol used for open signals
	 */
	public List<FuturesSignal> findActiveSignals(String symbol, long exchangeId, String positionType) {

		String query = """
					   select signal_id, symbol, position_type, 
					   position_status, (executed_buy_qty - executed_sell_qty) as position_size, signal_status
					   from futures_positions
					   where symbol = :symbol 
					   and exchange_id = :exchangeId
					   and position_type = :positionType
					   and signal_status in ( 'ACTIVE', 'CREATED', 'UNKNOWN' )
					   order by signal_id desc limit 1
					   """;

		return findSignalsByPositionAndStatus(query, symbol, exchangeId, positionType);
	}

	/**
	 * Used for close signals
	 *
	 * @param symbol
	 * @param exchangeId
	 * @param positionType
	 * @return
	 */
	public List<FuturesSignal> findActiveSignalsWithPosition(String symbol, long exchangeId, String positionType) {

		String query = """
					   select signal_id, symbol, position_type, 
					   position_status, (executed_buy_qty - executed_sell_qty) as position_size, signal_status 
					   from futures_positions
					   where symbol = :symbol 
					   and exchange_id = :exchangeId
					   and position_type = :positionType
					   and signal_status = 'ACTIVE'
					  """;
		return findSignalsByPositionAndStatus(query, symbol, exchangeId, positionType);
	}

	private List<FuturesSignal> findSignalsByPositionAndStatus(String query, String symbol, long exchangeId, String positionType) {
		Query q = em.createNativeQuery(query);

		q.setParameter("symbol", symbol);
		q.setParameter("exchangeId", exchangeId);
		q.setParameter("positionType", positionType);

		List<Object[]> resultList = q.getResultList();
		List<FuturesSignal> listFS = new ArrayList<>();
		//TODO::Assuming all the
		for (Object[] obj : resultList) {
			FuturesSignal fs = new FuturesSignal();
			fs.setSignalId(((BigInteger)obj[0]).longValue());
			fs.setSymbol((String)obj[1]);
			fs.setPositionType((String)obj[2]);
			fs.setPositionStatus((String)obj[3]);
			fs.setPositionSize(((BigDecimal)obj[4]).doubleValue());
			fs.setSignalStatus((String)obj[5]);
			listFS.add(fs);
		}

		return listFS;
	}

	public double getActiveContractsFromDB(long signalId){
		String query = """
				  SELECT position_size
				  FROM futures_pnl
				  WHERE signal_id = :signalId
				""";
		Query q = em.createNativeQuery(query);
		q.setParameter("signalId", signalId);
		Object result = q.getSingleResult();
		return result == null ? 0: ((BigDecimal) result).doubleValue();
	}

	public List<FuturesSignal> findAllActiveSignalsByStrategy(int exchangeId) {
		Query q = em.createNativeQuery("""
    			select signal_id, symbol, position_type,
				position_status, (executed_buy_qty - executed_sell_qty) as position_size
				from futures_positions
				where exchange_id = :exchangeId
				and signal_status = 'ACTIVE'
				and position_status = 'OPEN'
				""");


		q.setParameter("exchangeId", exchangeId);
		List<FuturesSignal> resultList = q.getResultList();
		return resultList;
	}

	/**
	 * select fs2.signal_id,
	 * 	coalesce (sum(buyOrder.executed_qty) - sum(sellOrder.executed_qty), 0) as position_size,
	 * 	case when sum(buyOrder.executed_qty) - sum(sellOrder.executed_qty) != 0 then 'OPEN' else 'CLOSED' end as signal_status
	 * from futures_signal fs2
	 * left join exchange_order buyOrder on buyOrder.signal_id = fs2.signal_id and buyOrder.order_side  = 'BUY'
	 * left join exchange_order sellOrder on sellOrder.signal_id = fs2.signal_id and sellOrder.order_side = 'SELL'
	 * 	group by fs2.signal_id
	 */

}
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
public class FuturesSignalCommandController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	FuturesSignalCommandRepository repository;

	public String save(FuturesSignalCommand futuresSignalCommand){
		repository.save(futuresSignalCommand);
		return String.valueOf(futuresSignalCommand.getId());
	}

	public boolean delete(FuturesSignalCommand futuresSignalCommand){
		repository.delete(futuresSignalCommand);
		return true;
	}

	public Iterable<FuturesSignalCommand> findAll(){
		return repository.findAll();
	}

	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public String fetchDataBySymbol( String Id){
		String result = "<html>";

		List<FuturesSignalCommand> futuresSignalCommand = repository.findFuturesSignalCommandById(Id);
			result += "<div>" + futuresSignalCommand.toString() + "</div>";
		return result + "</html>";
	}

	//Position type is signal action - CLOSE or OPEN
    public List<FuturesSignalCommand> finSignalsBySignalNHashNTradeAction(String jsonHash, long signalId, String tradeAction) {
		return repository.findAllBySignalIdAndStrategyHashAndSignalAction(signalId, jsonHash, tradeAction );
    }

    public List<FuturesSignal> findFuturePositionsWithSignal(long signalId) {
		String query = """
					   select signal_id, symbol, position_type, 
					   position_status
					   from futures_positions
					   where signal_id = :signalId
					   and signal_status = 'ACTIVE'
					   and position_status <> 'OPEN'
					   order by signal_id desc limit 1
					   """;
		Query q = em.createNativeQuery(query);

		q.setParameter("signalId", signalId);

		List<Object[]> resultList = q.getResultList();
		List<FuturesSignal> listFS = new ArrayList<>();
		//TODO::Assuming all the
		for (Object[] obj : resultList) {
			FuturesSignal fs = new FuturesSignal();
			fs.setSignalId(((BigInteger)obj[0]).longValue());
			fs.setSymbol((String)obj[1]);
			fs.setPositionType((String)obj[2]);
			fs.setPositionStatus((String)obj[3]);
			listFS.add(fs);
		}
		return listFS;
    }
}
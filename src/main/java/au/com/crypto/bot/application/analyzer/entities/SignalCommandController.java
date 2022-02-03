package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class SignalCommandController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	SignalCommandRepository repository;

	public String save(SignalCommand futuresSignalCommand){
		repository.save(futuresSignalCommand);
		return String.valueOf(futuresSignalCommand.getId());
	}

	public boolean delete(SignalCommand futuresSignalCommand){
		repository.delete(futuresSignalCommand);
		return true;
	}

	public Iterable<SignalCommand> findAll(){
		return repository.findAll();
	}

	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public String fetchDataBySymbol( String Id){
		String result = "<html>";

		List<SignalCommand> futuresSignalCommand = repository.findFuturesSignalCommandById(Id);
			result += "<div>" + futuresSignalCommand.toString() + "</div>";
		return result + "</html>";
	}

	//Position type is signal action - CLOSE or OPEN
    public List<SignalCommand> finSignalsBySignalNHashNTradeAction(String jsonHash, long signalId, String tradeAction) {
		return repository.findAllBySignalIdAndStrategyHashAndSignalAction(signalId, jsonHash, tradeAction );
    }

    public List<Signal> findFuturePositionsWithSignal(long signalId) {
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
		List<Signal> listFS = new ArrayList<>();
		//TODO::Assuming all the
		for (Object[] obj : resultList) {
			Signal fs = new Signal();
			fs.setSignalId(((BigInteger)obj[0]).longValue());
			fs.setSymbol((String)obj[1]);
			fs.setPositionType((String)obj[2]);
			fs.setPositionStatus((String)obj[3]);
			listFS.add(fs);
		}
		return listFS;
    }
}
package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class StrategyConditionsController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	StrategyConditionsRepository repository;

	public String save(StrategyConditions strategyConditions){
		repository.save(strategyConditions);
		return String.valueOf(strategyConditions.getId());
	}

	public boolean delete(StrategyConditions strategyConditions){
		repository.delete(strategyConditions);
		return true;
	}

	public Iterable<StrategyConditions> findAll(){
		return repository.findAll();
	}

	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}
	public List<StrategyConditions> findStrategyConditionsByStrategyIdAndVersion(long strategyId, long version){
		return repository.findStrategyConditionsByStrategyIdAndVersion(strategyId, version);
	}
}
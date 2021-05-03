package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class StrategyController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	StrategyRepository repository;

	public String save(Strategy strategy){
		repository.save(strategy);
		return String.valueOf(strategy.getId());
	}

	public boolean delete(Strategy strategy){
		repository.delete(strategy);
		return true;
	}

	public Iterable<Strategy> findAll(){
		return repository.findAll();
	}

	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public String fetchDataBySymbol( String Id){
		String result = "<html>";

		List<Strategy> strategy = repository.findStrategyById(Id);
			result += "<div>" + strategy.toString() + "</div>";
		return result + "</html>";
	}
	List<Strategy> findStrategyById(String id) {
		return repository.findStrategyById(id);
	}

	public List<Strategy> findStrategyByStatus(String status) {
		return repository.findStrategyByStatus(status);
	}
}
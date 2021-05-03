package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class MarketEventController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	MarketEventRepository repository;

	public String save(MarketEvent marketEvent){
		repository.save(marketEvent);
		return String.valueOf(marketEvent.getId());
	}

	public boolean delete(MarketEvent marketEvent){
		repository.delete(marketEvent);
		return true;
	}

	public Iterable<MarketEvent> findAll(){
		return repository.findAll();
	}

	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public List<MarketEvent> findAllOrderByDateDesc() {
		return repository.findAllByOrderByEventTimeDesc();
	}
	public String fetchDataBySymbol( String Id){
		String result = "<html>";

		List<MarketEvent> marketEvent = repository.findSignalInfoById(Id);
			result += "<div>" + marketEvent.toString() + "</div>";
		return result + "</html>";
	}

}
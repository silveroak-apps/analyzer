package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Component
@Transactional
public class BuyAnalysisController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	BuyAnalysisRepository repository;

	public String save(BuyAnalysis buyAnalysis){
		repository.save(buyAnalysis);

		return String.valueOf(buyAnalysis.getId());
	}

	public boolean delete(BuyAnalysis buyAnalysis){
		repository.delete(buyAnalysis);
		return true;
	}
	public Iterable<BuyAnalysis> findAll(){
		return repository.findAll();
	}
	public List<BuyAnalysis> findAllUnmarked() {
		Query q = em.createNativeQuery("select * from buy_analysis " +
				" where signal_id in (select signal_id from positive_signal where strategy is null)", BuyAnalysis.class);
		List<BuyAnalysis> resultList = Collections.checkedList(q.getResultList(),BuyAnalysis.class);
		return resultList;
	}
	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public String fetchDataBySymbol( String Id){
		String result = "<html>";

		List<BuyAnalysis> buyAnalysis = repository.findSignalInfoById(Id);
			result += "<div>" + buyAnalysis.toString() + "</div>";


		return result + "</html>";
	}

}
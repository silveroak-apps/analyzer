package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import au.com.crypto.bot.application.analyzer.entities.PositiveSignal;
import au.com.crypto.bot.application.analyzer.entities.PositiveSignalRepository;
import au.com.crypto.bot.application.analyzer.entities.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class WebController {
	@Autowired
    PositiveSignalRepository repository;
	
	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	EntityManager entityManager;

//	@RequestMapping("/save")
//	public String process() {
//		repository.save(new Customer("Jack", "Smith"));
//		repository.save(new Customer("Adam", "Johnson"));
//		repository.save(new Customer("Kim", "Smith"));
//		repository.save(new Customer("David", "Williams"));
//		repository.save(new Customer("Peter", "Davis"));
//
//		//entityManager.persist(new Customer("Jack", "Smith"));
//
//		return "Done";
//	}

	@RequestMapping("/findall")
    public String findAll(){
        String result = "<html>";
         
        for(PositiveSignal positiveSignal : repository.findAll()){
            result += "<div>" + positiveSignal.toString() + "</div>";
        }
        return result + "</html>";
    }
	@RequestMapping("/findallsignals")
	public List<WebView> findAllSignals(){
		Query q = entityManager.createNativeQuery("SELECT  * " +
				"FROM public.webview ",WebView.class);
		return Collections.checkedList(q.getResultList(), WebView.class);
	}

	@RequestMapping("/findbyid")
	public String findById(@RequestParam("id") long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

}
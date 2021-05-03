package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class ConfigsController {

	@PersistenceContext (unitName = "analysisEntityManagerFactory")
	private EntityManager em;
	@Autowired
	ConfigsRepository repository;

	public List<Configs> findAll(){
		return repository.findAll();
	}

	public List<Configs> findByName(String name){
		return repository.findByName(name);
	}

}
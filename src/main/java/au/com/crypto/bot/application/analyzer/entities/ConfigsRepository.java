package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigsRepository extends CrudRepository<Configs, String>{
  List<Configs> findAll();

  List<Configs> findByName(String name);
}
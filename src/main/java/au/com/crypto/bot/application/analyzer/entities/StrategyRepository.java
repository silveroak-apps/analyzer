package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StrategyRepository extends CrudRepository<Strategy, Long>{
  List<Strategy> findStrategyById(String id);
  List<Strategy> findStrategyByStatus(String status);
}
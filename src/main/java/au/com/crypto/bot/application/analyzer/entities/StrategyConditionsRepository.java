package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StrategyConditionsRepository extends CrudRepository<StrategyConditions, Long>{
  List<StrategyConditions> findStrategyConditionsById(long id);
  List<StrategyConditions> findStrategyConditionsByStrategyId(long strategyId);
  List<StrategyConditions> findStrategyConditionsByStrategyIdAndVersion(long strategyId, long version);
}
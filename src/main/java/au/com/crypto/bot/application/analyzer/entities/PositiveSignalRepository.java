package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

public interface PositiveSignalRepository extends CrudRepository<PositiveSignal, Long>{
  PositiveSignal findPositiveSignalBySymbol(String symbol);

}
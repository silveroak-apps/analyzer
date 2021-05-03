package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FuturesSignalRepository extends CrudRepository<FuturesSignal, Long>{
  List<FuturesSignal> findFuturesSignalBySignalId(String id);
}
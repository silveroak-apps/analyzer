package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SignalRepository extends CrudRepository<Signal, Long>{
  List<Signal> findFuturesSignalBySignalId(String id);
}
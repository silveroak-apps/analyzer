package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FuturesSignalCommandRepository extends CrudRepository<FuturesSignalCommand, Long>{
  List<FuturesSignalCommand> findFuturesSignalCommandById(String id);

  List<FuturesSignalCommand> findAllBySignalIdAndStrategyHashAndSignalAction(long signalId, String jsonHash, String tradeType);
}
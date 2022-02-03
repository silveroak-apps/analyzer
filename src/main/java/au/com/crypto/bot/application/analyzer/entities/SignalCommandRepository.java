package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SignalCommandRepository extends CrudRepository<SignalCommand, Long>{
  List<SignalCommand> findFuturesSignalCommandById(String id);

  List<SignalCommand> findAllBySignalIdAndStrategyHashAndSignalAction(long signalId, String jsonHash, String tradeType);
}
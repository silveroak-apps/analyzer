package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import au.com.crypto.bot.application.ticker.entities.CoinStats;

public interface SignalInfoRepository extends CrudRepository<CoinStats, Long>{
  List<CoinStats> findSignalInfoBySymbol(String symbol);
}
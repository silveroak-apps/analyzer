package au.com.crypto.bot.application.ticker.entities;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CoinStatsHistoryRepository extends CrudRepository<CoinStatsHistory, Long>{
  List<CoinStatsHistory> findSignalInfoBySymbol(String symbol);
}
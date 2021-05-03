package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

public interface CoinRepository extends CrudRepository<Coin, Long>{
  Coin findCoinBySymbol(String symbol);
}
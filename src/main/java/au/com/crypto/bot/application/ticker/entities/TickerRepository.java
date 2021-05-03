package au.com.crypto.bot.application.ticker.entities;

import org.springframework.data.repository.CrudRepository;

public interface TickerRepository extends CrudRepository<Ticker, Long>{
  Ticker findTickersBySymbol(String symbol);
}
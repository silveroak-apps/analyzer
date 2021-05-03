package au.com.crypto.bot.application.ticker.entities;

import org.springframework.data.repository.CrudRepository;

public interface Coin24HrMarketRepository extends CrudRepository<Coin24HrMarket, Long>{
  Coin24HrMarket findCoin24HrMarketBySymbol(String symbol);
}
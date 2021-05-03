package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

public interface ExchangeRepository extends CrudRepository<Exchange, Long>{
  Exchange findExchangeByCode(String code);
}
package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface APIRepository extends CrudRepository<API, Long>{
  List<API> findAPIByExchangeId(long exchangeId);
}
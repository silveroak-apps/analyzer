package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface KlineDataRepository extends CrudRepository<KlineData, Long>{
  KlineData findKlineBySymbolAndOpentime(String symbol, BigInteger opentime);
}
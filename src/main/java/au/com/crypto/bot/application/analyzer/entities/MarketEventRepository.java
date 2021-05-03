package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarketEventRepository extends CrudRepository<MarketEvent, Long>{
  List<MarketEvent> findSignalInfoById(String id);
  List<MarketEvent>  findAllByOrderByEventTimeDesc();
}
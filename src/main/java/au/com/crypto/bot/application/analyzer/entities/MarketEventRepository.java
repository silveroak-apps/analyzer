package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarketEventRepository extends CrudRepository<MarketEvent, Long>{
  List<MarketEvent> findSignalInfoById(String id);
  List<MarketEvent>  findAllByOrderByEventTimeDesc();
  @Query(value = "select * from market_event me order by id desc limit 200", nativeQuery=true)
  List<MarketEvent> findAllEventsLimitTwoHundred();
}
package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

public interface WatchDogRepository extends CrudRepository<WatchDog, Long>{
  WatchDog findWatchDogByResource(String resource);
}
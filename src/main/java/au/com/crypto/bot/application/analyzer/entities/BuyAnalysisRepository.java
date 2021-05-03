package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BuyAnalysisRepository extends CrudRepository<BuyAnalysis, Long>{
  List<BuyAnalysis> findSignalInfoById(String id);
}
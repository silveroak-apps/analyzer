package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface WebViewRepository extends CrudRepository<WebView, Long>{
  List<WebView> findAllByBuyDateBetween(Date fromDate, Date toDate);
}
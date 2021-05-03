package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface Data24HrRepository extends CrudRepository<Data24Hr, Long>{
  Data24Hr findDataByInsertDate(Date insertDate);
}
package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WatchDogController {

	@Autowired
	WatchDogRepository watchDogRepository;

	public void save(WatchDog watchDog){
		WatchDog wd = watchDogRepository.findWatchDogByResource(watchDog.getResource());

		if (wd == null) {
			watchDogRepository.save(watchDog);
		} else {
			wd.setLast_update( new Date());
			watchDogRepository.save(wd);
		}
	}

	public String findAll(){
		String result = "<html>";

		for(WatchDog watchDog : watchDogRepository.findAll()){
			result += "<div>" + watchDog.toString() + "</div>";
		}
		return result + "</html>";
	}
}
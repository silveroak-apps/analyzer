package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class APIController {

	@Autowired
	APIRepository APIRepository;


	@Autowired
	ExchangeRepository exchangeRepository;

	public String process() {
		APIRepository.save(new API("Jack", "Smith",exchangeRepository.findExchangeByCode("BNB"),"READ_ONLY"));
		//entityManager.persist(new Customer("Jack", "Smith"));

		return "Done";
	}


	public String findAll(){
		String result = "<html>";

		for(API api : APIRepository.findAll()){
			result += "<div>" + api.toString() + "</div>";
		}
		return result + "</html>";
	}


	public String findById( long id) {
		String result = "";
		result = APIRepository.findOne(id).toString();
		return result;
	}

	public String fetchDataExchangeId( long exchange_id){
		String result = "<html>";

		for(API api: APIRepository.findAPIByExchangeId(exchange_id)){
			result += "<div>" + api.toString() + "</div>";
		}

		return result + "</html>";
	}
}
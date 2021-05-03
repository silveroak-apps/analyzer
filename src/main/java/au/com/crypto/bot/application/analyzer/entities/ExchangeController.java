package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeController {

	@Autowired
	ExchangeRepository repository;

	public String process() {
		repository.save(new Exchange("BNB","Binance","Some more info"));

		return "Done";
	}


	public String findAll(){
		String result = "<html>";

		for(Exchange exchange : repository.findAll()){
			result += "<div>" + exchange.toString() + "</div>";
		}
		return result + "</html>";
	}


	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public String fetchDataExchangeId( String code){
		String result = "<html>";

		Exchange exchange = repository.findExchangeByCode(code);
			result += "<div>" + exchange.toString() + "</div>";


		return result + "</html>";
	}
}
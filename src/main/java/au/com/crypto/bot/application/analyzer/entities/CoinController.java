package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoinController {

	@Autowired
	CoinRepository repository;



	public String process() {
		repository.save(new Coin("Bitcoin", "Bitcoin","Bitcoin","BTC"));
		//entityManager.persist(new Customer("Jack", "Smith"));

		return "Done";
	}


	public String findAll(){
		String result = "<html>";

		for(Coin api : repository.findAll()){
			result += "<div>" + api.toString() + "</div>";
		}
		return result + "</html>";
	}


	public String findById( long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	public String fetchDataExchangeId( String symbol){
		String result = "<html>";

		Coin coin = repository.findCoinBySymbol(symbol);
			result += "<div>" + coin.toString() + "</div>";


		return result + "</html>";
	}

}
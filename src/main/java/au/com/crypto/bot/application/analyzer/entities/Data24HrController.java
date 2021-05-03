package au.com.crypto.bot.application.analyzer.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Data24HrController {

    @Autowired
    Data24HrRepository repository;

    @Autowired
    ExchangeRepository exchangeRepository;

    @Autowired
    CoinRepository coinRepository;

    public String process() {
        repository.save(new Data24Hr(new Date(), coinRepository.findCoinBySymbol("BTC"),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43),
                new Double(43), exchangeRepository.findExchangeByCode("BNB")));
        //entityManager.persist(new Customer("Jack", "Smith"));

        return "Done";
    }


    public String findAll() {
        String result = "<html>";

        for (Data24Hr data : repository.findAll()) {
            result += "<div>" + data.toString() + "</div>";
        }
        return result + "</html>";
    }


    public String findById(long id) {
        String result = "";
        result = repository.findOne(id).toString();
        return result;
    }

    public String fetchDataExchangeId(Date date) {
        String result = "<html>";

        Data24Hr exchange = repository.findDataByInsertDate(date);
        result += "<div>" + exchange.toString() + "</div>";


        return result + "</html>";
    }
}
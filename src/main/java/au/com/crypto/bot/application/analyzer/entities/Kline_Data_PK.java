package au.com.crypto.bot.application.analyzer.entities;

import java.io.Serializable;

public class Kline_Data_PK implements Serializable {

        protected String symbol;
        protected Long opentime;;

        public Kline_Data_PK() {}

        public Kline_Data_PK(String symbol, Long opentime) {
            this.opentime = opentime;
            this.symbol = symbol;
        }

}

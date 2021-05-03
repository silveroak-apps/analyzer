package au.com.crypto.bot.application.utils;

import au.com.crypto.bot.application.ticker.entities.Coin24HrMarket;
import au.com.crypto.bot.application.ticker.entities.Coin24HrMarketController;
import au.com.crypto.bot.application.analyzer.entities.KlineData;
import au.com.crypto.bot.application.ticker.entities.CoinStats;
import au.com.crypto.bot.application.ticker.entities.CoinStatsHistory;
import au.com.crypto.bot.application.ticker.entities.Ticker;

import org.apache.commons.lang3.time.DateUtils;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static Map<String,Coin24HrMarket> convertIntoMap(List<Coin24HrMarket> all24HrsPriceStats) {
        Map<String,Coin24HrMarket> coinsMarketAsMap = new HashMap<>();
        for (Coin24HrMarket coinMarket:all24HrsPriceStats){
            coinsMarketAsMap.put(coinMarket.getSymbol(),coinMarket);
        }
        return coinsMarketAsMap;
    }

    public static void save(List response, Coin24HrMarketController controller){

        for (LinkedHashMap<String,Object> marketValue:(List<LinkedHashMap<String, Object>>) response){
            // System.out.println(ticker);
            Coin24HrMarket market = new Coin24HrMarket();
            market.cast(marketValue);
            market.setInsertDate(new Date());
            controller.save(market);
        }
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            logger.error("Error in sleeping ticker");
//        }
    }

    public static double MovingAverage(List<Ticker> list){
        double mv = 0;
        try {
            if (list != null && list.size()>10) {
                mv = ((list.subList(0, (int) (list.size() * 0.20)).stream().mapToDouble(o -> o.getPrice()).sum()) / (int) (list.size() * 0.20))
                        / ((list.subList((int) (list.size() * 0.20), list.size()).stream().mapToDouble(o -> o.getPrice()).sum()) / (list.size() - (int) (list.size() * 0.20)));
            }
        }catch (Exception e)
        {
            logger.error("Error in calculating moving average");
        }
        return mv;
    }

    public static  Map<String,List<Ticker>> convertTickersIntoMap(List<Ticker> allTickers) {
        HashMap<String, List<Ticker>> allTicMap = (HashMap<String, List<Ticker>>) allTickers.stream().collect(Collectors.groupingBy(Ticker::getSymbol));
        return allTicMap;
    }
    public static  Map<String,List<KlineData>> convertKlineIntoMap(List<KlineData> allKlines) {
        HashMap<String, List<KlineData>> allKlineMap = (HashMap<String, List<KlineData>>) allKlines.stream().collect(Collectors.groupingBy(KlineData::getSymbol));
        return allKlineMap;
    }
    public static Map<String,Coin24HrMarket> convertTickersIntoCoin24Map(List<Ticker> allTickerPriceStats) {
        Map<String,Coin24HrMarket> coinsMarketAsMap = new HashMap<>();
        for (Ticker ticker:allTickerPriceStats){
            coinsMarketAsMap.put(ticker.getSymbol(),ticker.getCoin24HrMarket());
        }
        return coinsMarketAsMap;
    }
    public static Map<String,CoinStats> convertCoinStatsIntoMap(List<CoinStats> allCoinStats) {
        Map<String,CoinStats> coinsStatsMap = new HashMap<>();
        for (CoinStats coinStats:allCoinStats){
            coinsStatsMap.put(coinStats.getSymbol(),coinStats);
        }
        return coinsStatsMap;
    }

    public static List<CoinStats> convertStatsHistoryToStats(List<CoinStatsHistory> history) {
        return history.stream().map(t -> t.castToCoinStats()).collect(Collectors.toList());
    }

    public static Map<String,List<CoinStats>> getMapStatsHistoryToStats(Iterable<CoinStats> history) {
        Map<String,List<CoinStats>> map = new TreeMap<>(secondCharComparator);
        for (CoinStats coinStats: history) {
            String key = getKeyStatsHistory(coinStats.getTime());
            if (map.containsKey(key)) {
                map.get(key).add(coinStats);
            }else {
                map.put(key,new ArrayList<>());
                map.get(key).add(coinStats);
            }
        }
      return map;
    }
    public static Comparator<String> secondCharComparator = new Comparator<String>() {
        @Override public int compare(String s2, String s1) {

            String str1 = s1.substring(0, s1.indexOf("_"));
            String str2 = s2.substring(0, s2.indexOf("_"));

            Integer i1 = Integer.valueOf(str1);
            Integer i2 = Integer.valueOf(str2);
            int cmp = i1.compareTo(i2);
            if (cmp != 0) {
                return cmp;
            }

            str1 = s1.substring(s1.indexOf("_")+1, s1.length());
            str2 = s2.substring(s2.indexOf("_")+1, s2.length());

            i1 = Integer.valueOf(str1.substring(0, str1.indexOf("_")));
            i2 = Integer.valueOf(str2.substring(0, str2.indexOf("_")));
            cmp = i1.compareTo(i2);
            if (cmp != 0) {
                return cmp;
            }

            str1 = str1.substring(str1.indexOf("_")+1, str1.length());
            str2 = str2.substring(str2.indexOf("_")+1, str2.length());

            i1 = Integer.valueOf(str1.substring(0, str1.indexOf("_")));
            i2 = Integer.valueOf(str2.substring(0, str2.indexOf("_")));
            cmp = i1.compareTo(i2);
            if (cmp != 0) {
                return cmp;
            }

            str1 = str1.substring(str1.indexOf("_")+2, str1.length());
            str2 = str2.substring(str2.indexOf("_")+2, str2.length());

            i1 = Integer.valueOf(str1.substring(0, str1.indexOf("_")));
            i2 = Integer.valueOf(str2.substring(0, str2.indexOf("_")));
            cmp = i1.compareTo(i2);
            if (cmp != 0) {
                return cmp;
            }

            int min = Integer.valueOf(s1.substring(s1.lastIndexOf("_")+1, s1.length())).compareTo(Integer.valueOf(s2.substring(s2.lastIndexOf("_")+1, s2.length())));
            if (min != 0) {
                return min;
            }
            return 0;
            }
    };
    public static Map<String, Map<Integer,CoinStats>> getMapStatsHistoryToStatsWithNumber(List<CoinStats> history) {

        Map<String,List<CoinStats>> asMap = getMapStatsHistoryToStats(history);
        Map<String, Map<Integer,CoinStats>> result = new HashMap<>();
        Set<Map.Entry<String, List<CoinStats>>> set = asMap.entrySet();
        int i = 0;
        for (Map.Entry entry:set)
        {
            i++;
            for (CoinStats stats :(List<CoinStats>)entry.getValue()) {
                if (result.containsKey(stats.getSymbol())) {
                    result.get(stats.getSymbol()).put(i,stats);
                } else {
                    Map<Integer, CoinStats> mapCoinStats = new HashMap<>();
                    mapCoinStats.put(i, stats);
                    result.put(stats.getSymbol(), mapCoinStats);
                }

            }
        }

        return result;
    }
    public static String getKeyStatsHistory( Date startDate) {
    return startDate.getYear()+"_"+startDate.getMonth()+"_"+startDate.getDate()+"__"+startDate.getHours()+"_"+startDate.getMinutes();
    }

    public static boolean isEMAOkay(CoinStats coinStats) {
        if (coinStats.getEmaLargeList() == null)
            return false;
        List<Double> emaLargeList = Arrays.stream(coinStats.getEmaLargeList().split(":"))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        List<Double> emaMedList = Arrays.stream(coinStats.getEmaMedList().split(":"))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        List<Double> emaSmallList = Arrays.stream(coinStats.getEmaSmallList().split(":"))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        boolean isRaise = true;
        if (emaLargeList != null && emaLargeList.size() > 0
            && emaMedList !=null && emaMedList.size() > 0 ) {
               // && emaSmallList !=null && emaSmallList.size() > 0) {
            double previousEMALarge = 0;
            double previousEMAMed = 0;
            double previousEMASmall = 0;

            //Last 3 results
            for (int i = 3; i >= 0; i--) {
                //Indication that they are moving in forward direction
                if (isRaise && previousEMAMed < emaMedList.get(i)
                        && previousEMASmall < emaSmallList.get(i)
                        && previousEMALarge < emaLargeList.get(i)){
                    isRaise = true;
                } else {
                    isRaise = false;
                }
//                //Indication that Small EMA is on top of Mid and Mid EMA is on top of Large
//                if (i < 2) {
//                    if (isRaise && (emaMedList.get(i) - emaSmallList.get(i)) < 0) {
//                        //&& emaLargeList.get(i) - emaMedList.get(i) < 0) {
//                        isRaise = true;
//                    } else {
//                        isRaise = false;
//                    }
//                }
                //Indication that mid EMA crossed the Large EMA
//                if (i == 2) {
//                    if (isRaise && emaLargeList.get(i) - emaMedList.get(i) > 0) {
//                        isRaise = true;
//                    } else {
//                        isRaise = false;
//                    }
//                }
                previousEMAMed = emaMedList.get(i);
                previousEMASmall = emaSmallList.get(i);
                previousEMALarge = emaLargeList.get(i);
            }
        } else {
            return false;
        }
        return isRaise;
    }
    public static boolean isMACDARise(CoinStats coinStats) {
        List<Double> macd = Arrays.stream(coinStats.getMacdSignalList().split(":"))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        boolean isRaise = true;
        if (macd != null && macd.size() > 0) {
            double previousMACD = 0;
            for (int i = 3; i >= 0; i--) {
                if (isRaise && previousMACD < macd.get(i)) {
                    isRaise = true;
                } else {
                    isRaise = false;
                }
                previousMACD = macd.get(i);
            }
//            if(isRaise
//                    //&& (macd.get(3)<0 || macd.get(4)<0 || macd.get(6)<0 || macd.get(8)<0)
//                     && (macd.get(0)>0)){
//                isRaise = true;
//            }else {
//                isRaise = false;
//            }
        }
        return isRaise;
    }
    // THis is for simulation
    public static Map<String,Map<Integer,CoinStats>> collect1HourCoinStats(Map<String, List<CoinStats>> coinStatsHistoryMap, Date startDate) {
        List<CoinStats> coinStats = new ArrayList<>();
        for (int i = 60; i>=0 ;i--) {
            if (coinStatsHistoryMap.containsKey(getKeyStatsHistory(DateUtils.addMinutes(startDate, -i)))){
                coinStats.addAll(coinStatsHistoryMap.get(getKeyStatsHistory(DateUtils.addMinutes(startDate, -i))));
            }
        }
        return getMapStatsHistoryToStatsWithNumber(coinStats);
    }

    //http://localhost:50352/tradeSignals
    public static void triggerSellBotForBuy(String protocol, String host, String path) {

        AsyncHttpClient asyncHttpClient = asyncHttpClient();
        // bound
        Future<Response> whenResponse = asyncHttpClient.preparePost(protocol+"://"+host+path).execute();
        try {
            Response response = whenResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            LogUtil.printLog(logger, LogUtil.STATUS.ERROR.name(), "LogUtil", "Error in calling sell bot",e);
        }
    }

    public static boolean isFunding() {
        Date currentDate = new Date();
        LocalDateTime now = LocalDateTime.now();

        Calendar twoThirty = getCalender(2, 30);
        Calendar four = getCalender(4, 0);
        Calendar tenThirty = getCalender(10, 30);
        Calendar twelve = getCalender(12, 0);
        Calendar eighteenThirty = getCalender(18, 30);
        Calendar twenty = getCalender(20, 0);

        if (twoThirty.getTime().getTime() < currentDate.getTime() && four.getTime().getTime() > currentDate.getTime()) {
            return true;
        }

        if (tenThirty.getTime().getTime() < currentDate.getTime() && twelve.getTime().getTime() > currentDate.getTime()) {
            return true;
        }

        if (eighteenThirty.getTime().getTime() < currentDate.getTime() && twenty.getTime().getTime() > currentDate.getTime()) {
            return true;
        }
        return false;
    }
    private static Calendar getCalender(int hour, int min) {

        LocalDateTime now = LocalDateTime.now();
        Calendar cal = Calendar.getInstance();
        cal.set(now.getYear(), now.getMonthValue()-1,now.getDayOfMonth(), hour, min, 0);
        return cal;
    }
}

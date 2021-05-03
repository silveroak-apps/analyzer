package au.com.crypto.bot.application.utils;

import java.util.Date;

public class TimeFrameUtil {


    public TimeFrameUtil(){

    }

    public static String getTimeFrame(Date currentDate){
        String returnString =  ",15Sec";
            if ( currentDate.getSeconds() == 0) {
                returnString = returnString + ",1Min";
            }
            if ((currentDate.getSeconds() == 0 ) &&
                    currentDate.getMinutes() % 5 == 0) {
                returnString = returnString + ",5Min";
            }
            if ((currentDate.getSeconds() == 0) &&
                    currentDate.getMinutes() % 10 == 0) {
                returnString = returnString + ",10Min";
            }
            if (currentDate.getSeconds() == 0  &&
                    currentDate.getMinutes() % 15 == 0) {
                returnString = returnString + ",15Min";
            }
            if (currentDate.getSeconds() == 0 &&
                    currentDate.getMinutes() % 30 == 0) {
                returnString = returnString + ",30Min";
            }
            if ((currentDate.getSeconds() == 0 && currentDate.getMinutes() == 0)) {
                returnString = returnString + ",1Hour";
            }
            if ((currentDate.getSeconds() == 0 && currentDate.getMinutes() == 0) && currentDate.getHours() % 2 == 0) {
                returnString = returnString + ",2Hour";
            }
            if ((currentDate.getSeconds() == 0 && currentDate.getMinutes() == 0) && currentDate.getHours() % 4 == 0) {
                returnString = returnString + ",4Hour";
            }
            if ((currentDate.getSeconds() == 0 && currentDate.getMinutes() == 0) && currentDate.getHours() % 8 == 0) {
                returnString = returnString + ",8Hour";
            }
            if ((currentDate.getSeconds() == 0 && currentDate.getMinutes() == 0) && currentDate.getHours() == 0) {
                returnString = returnString + ",1Day";
            }
        return returnString;
    }
}

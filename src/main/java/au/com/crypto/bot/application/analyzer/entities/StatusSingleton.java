package au.com.crypto.bot.application.analyzer.entities;

import org.apache.commons.lang3.StringUtils;

@Deprecated
public class StatusSingleton {

    public static final String TICKER_READY = "TIC_READY";
    public static final String ANALYZE_READY = "ANALYZE_READY";


    private String statusFifteenSec;
    private String statusOneMin;
    private String statusFiveMin;
    private String statusTenMin;
    private String statusFifteenMin;
    private String statusOneHour;
    private String statusOneDay;
    private String socketStatus;
    private String statusSimulation;
    private static StatusSingleton statusClass;

    private StatusSingleton() {

    }


    public String getStatusOneMin() {
        return statusOneMin;
    }

    public void setStatusOneMin(String statusOneMin) {
        this.statusOneMin = statusOneMin;
    }

    public String getStatusFiveMin() {
        return statusFiveMin;
    }

    public void setStatusFiveMin(String statusFiveMin) {
        this.statusFiveMin = statusFiveMin;
    }

    public String getStatusTenMin() {
        return statusTenMin;
    }

    public void setStatusTenMin(String statusTenMin) {
        this.statusTenMin = statusTenMin;
    }

    public String getStatusFifteenMin() {
        return statusFifteenMin;
    }

    public void setStatusFifteenMin(String statusFifteenMin) {
        this.statusFifteenMin = statusFifteenMin;
    }

    public String getStatusOneHour() {
        return statusOneHour;
    }

    public void setStatusOneHour(String statusOneHour) {
        this.statusOneHour = statusOneHour;
    }

    public String getStatusOneDay() {
        return statusOneDay;
    }

    public void setStatusOneDay(String statusOneDay) {
        this.statusOneDay = statusOneDay;
    }


    public String getStatusFifteenSec() {
        return statusFifteenSec;
    }

    public void setStatusFifteenSec(String statusFifteenSec) {
        this.statusFifteenSec = statusFifteenSec;
    }

    public String getStatusSimulation() {
        return statusSimulation;
    }

    public void setStatusSimulation(String statusSimulation) {
        this.statusSimulation = statusSimulation;
    }

    public static StatusSingleton getInstance() {
        if (statusClass == null) {
            statusClass = new StatusSingleton();
        }
        return statusClass;
    }

    public String getSocketStatus() {

        return socketStatus;
    }

    public void setSocketStatus(String socketStatus) {

        this.socketStatus = socketStatus;
    }

    public boolean getTicStatus(String tic) {

        if (tic.equalsIgnoreCase("SIM"))
            return TICKER_READY.equalsIgnoreCase(getStatusSimulation()) ? true : false;
        if (tic.equalsIgnoreCase("15Sec"))
            return TICKER_READY.equalsIgnoreCase(getStatusFifteenSec()) ? true : false;
        if (tic.equalsIgnoreCase("1Min"))
            return TICKER_READY.equalsIgnoreCase(getStatusOneMin()) ? true : false;
        if (tic.equalsIgnoreCase("5Min"))
            return TICKER_READY.equalsIgnoreCase(getStatusFiveMin()) ? true : false;
        if (tic.equalsIgnoreCase("10Min"))
            return TICKER_READY.equalsIgnoreCase(getStatusTenMin()) ? true : false;
        if (tic.equalsIgnoreCase("15Min"))
            return TICKER_READY.equalsIgnoreCase(getStatusFifteenMin()) ? true : false;
        if (tic.equalsIgnoreCase("1Hour"))
            return TICKER_READY.equalsIgnoreCase(getStatusOneHour()) ? true : false;
        if (tic.equalsIgnoreCase("1Day"))
            return TICKER_READY.equalsIgnoreCase(getStatusOneDay()) ? true : false;
        return false;
    }


    public synchronized void statusUpdateFromTicker(String timeFrame, String status) {
        if (StringUtils.isNotEmpty(timeFrame)) {
            if (timeFrame.contains("SIM"))
                setStatusSimulation(status);
            if (timeFrame.contains("15Sec"))
                setStatusFifteenSec(status);
            if (timeFrame.contains("1Min"))
                setStatusOneMin(status);
            if (timeFrame.equalsIgnoreCase("5Min") || timeFrame.contains(",5Min"))
                setStatusFiveMin(status);
            if (timeFrame.contains("10Min"))
                setStatusTenMin(status);
            if (timeFrame.contains("15Min"))
                setStatusFifteenMin(status);
            if (timeFrame.contains("1Hour"))
                setStatusOneHour(status);
            if (timeFrame.contains("1Day"))
                setStatusOneDay(status);
        }
    }

    public boolean getAnayazeReady(String tic) {
        if (tic.equalsIgnoreCase("SIM"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusSimulation()) ? true : false;
        if (tic.equalsIgnoreCase("15Sec"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusFifteenSec()) ? true : false;
        if (tic.equalsIgnoreCase("1Min"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusOneMin()) ? true : false;
        if (tic.equalsIgnoreCase("5Min"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusFiveMin()) ? true : false;
        if (tic.equalsIgnoreCase("10Min"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusTenMin()) ? true : false;
        if (tic.equalsIgnoreCase("15Min"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusFifteenMin()) ? true : false;
        if (tic.equalsIgnoreCase("1Hour"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusOneHour()) ? true : false;
        if (tic.equalsIgnoreCase("1Day"))
            return ANALYZE_READY.equalsIgnoreCase(getStatusOneDay()) ? true : false;
        return false;
    }
}

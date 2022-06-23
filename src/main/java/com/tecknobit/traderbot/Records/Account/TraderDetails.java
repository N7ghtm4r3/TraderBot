package com.tecknobit.traderbot.Records.Account;

import java.util.Date;
import java.util.HashMap;

/**
 * The {@code TraderDetails} class is useful to contains details of trader used. <br>
 * Is useful for Android's type traders.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class TraderDetails extends Trader{

    /**
     * {@code CUSTOM_DEF_COLOR} is instance that memorize default color to use for example when asset is at pair
     * @implSpec not available for console color
     * **/
    public static final String CUSTOM_DEF_COLOR = "#A8A8A8";

    /**
     * {@code CUSTOM_RED_COLOR} is instance that memorize red color to use for example when asset is at loss
     * @implSpec not available for console color
     * **/
    public static final String CUSTOM_RED_COLOR = "#C3100C";

    /**
     * {@code CUSTOM_GREEN_COLOR} is instance that memorize green color to use for example when asset is at gain
     * @implSpec not available for console color
     * **/
    public static final String CUSTOM_GREEN_COLOR = "#24B82B";

    /**
     * {@code BINANCE_COLOR} is instance that memorize Binance official color
     * @implSpec not available for console color
     * **/
    public static final String BINANCE_COLOR = "#F3BA2F";

    /**
     * {@code COINBASE_COLOR} is instance that memorize Coinbase official color
     * @implSpec not available for console color
     * **/
    public static final String COINBASE_COLOR = "#1652f0";

    /**
     * {@code TRADER_TYPE_AUTONOMOUS} is instance that memorize AUTONOMOUS trader type in use
     * **/
    public static final String TRADER_TYPE_AUTONOMOUS = "AUTONOMOUS";

    /**
     * {@code TRADER_TYPE_MANUAL} is instance that memorize MANUAL trader type in use
     * **/
    public static final String TRADER_TYPE_MANUAL = "MANUAL";

    /**
     * {@code BINANCE_TRADER_PLATFORM} is instance that memorize BINANCE trader platform in use
     * **/
    public static final String BINANCE_TRADER_PLATFORM = "BINANCE";

    /**
     * {@code COINBASE_TRADER_PLATFORM} is instance that memorize COINBASE trader platform in use
     * **/
    public static final String COINBASE_TRADER_PLATFORM = "COINBASE";

    /**
     * {@code RUNNING_TRADER_STATUS} is instance that memorize RUNNING trader current status
     * **/
    public static final String RUNNING_TRADER_STATUS = "RUNNING";

    /**
     * {@code STOPPED_TRADER_STATUS} is instance that memorize STOPPED trader current status
     * **/
    public static final String STOPPED_TRADER_STATUS = "STOPPED";

    /**
     * {@code TRADER_STATUS_KEY} is instance that memorize trader status key
     * **/
    public static final String TRADER_STATUS_KEY = "trader_status";

    /**
     * {@code TRADER_PLATFORM_KEY} is instance that memorize trader platform key
     * **/
    public static final String TRADER_PLATFORM_KEY = "trader_platform";

    /**
     * {@code TRADER_TYPE_KEY} is instance that memorize trader type key
     * **/
    public static final String TRADER_TYPE_KEY = "trader_type";

    /**
     * {@code REFRESH_PRICES_TIME_KEY} is instance that memorize refresh time key
     * **/
    public static final String REFRESH_PRICES_TIME_KEY = "refresh_time";

    /**
     * {@code LAST_TRADER_ACTIVITY_KEY} is instance that memorize last trader activity key
     * **/
    public static final String LAST_TRADER_ACTIVITY_KEY = "last_trader_activity";

    /**
     * {@code RUNNING_FROM_DATE_KEY} is instance that memorize running from date key
     * **/
    public static final String RUNNING_FROM_DATE_KEY = "running_from_date";

    /**
     * {@code lastTraderActivity} is instance that memorize last time when bot contact server
     * **/
    private String lastTraderActivity;

    /**
     * {@code lastTraderActivityTimestamp} is instance that memorize last time when bot contact server in timestamp format
     * **/
    private long lastTraderActivityTimestamp;

    /**
     * {@code traderType} is instance that memorize type of trader in use {@link #TRADER_TYPE_AUTONOMOUS} or {@link #TRADER_TYPE_MANUAL}
     * **/
    private final String traderType;

    /**
     * {@code traderStatus} is instance that memorize current status of trader {@link #RUNNING_TRADER_STATUS} or {@link #STOPPED_TRADER_STATUS}
     * **/
    private String traderStatus;

    /**
     * {@code traderPlatform} is instance that memorize platform of trader in use {@link #BINANCE_TRADER_PLATFORM} or {@link #COINBASE_TRADER_PLATFORM}
     * **/
    private final String traderPlatform;

    /**
     * {@code refreshPricesTime} is instance that memorize time for refresh last prices
     * @implNote this param can customize with {@link #setRefreshPricesTime(int)}
     * @implSpec valid values are from 5 second to 3600 seconds other will generate an {@link Exception}
     * **/
    private long refreshPricesTime;

    /**
     * {@code traderPlatform} is instance that memorize time stamp of date when trader has been started
     * **/
    private long runningFromDate;

    /** Constructor to init {@link TraderDetails}
     * @param traderType: type of trader in use {@link #TRADER_TYPE_AUTONOMOUS} or {@link #TRADER_TYPE_MANUAL}
     * @param traderPlatform: platform of trader in use {@link #BINANCE_TRADER_PLATFORM} or {@link #COINBASE_TRADER_PLATFORM}
     * @param runningFromDate: time stamp of date when trader has been started
     * **/
    public TraderDetails(String traderType, String traderPlatform, long runningFromDate) {
        initTimeFormatters();
        if(!traderType.equals(TRADER_TYPE_AUTONOMOUS) && !traderType.equals(TRADER_TYPE_MANUAL))
            throw new IllegalArgumentException("Trader type inserted is wrong value, can be AUTONOMOUS or MANUAL type");
        else
            this.traderType = traderType;
        if(!traderPlatform.equals(BINANCE_TRADER_PLATFORM) && !traderPlatform.equals(COINBASE_TRADER_PLATFORM))
            throw new IllegalArgumentException("Trader platform inserted is not supported yet or is a wrong value");
        else
            this.traderPlatform = traderPlatform;
        if(runningFromDate < 0)
            throw new IllegalArgumentException("Running from date timestamp cannot be less than 0");
        else
            this.runningFromDate = runningFromDate;
    }

    /** Constructor to init {@link TraderDetails}
     * @param lastTraderActivity: last time when bot contact server
     * @param traderType: type of trader in use {@link #TRADER_TYPE_AUTONOMOUS} or {@link #TRADER_TYPE_MANUAL}
     * @param traderStatus: current status of trader {@link #RUNNING_TRADER_STATUS} or {@link #STOPPED_TRADER_STATUS}
     * @param traderPlatform: platform of trader in use {@link #BINANCE_TRADER_PLATFORM} or {@link #COINBASE_TRADER_PLATFORM}
     * @param refreshPricesTime: time for refresh last prices
     * @param runningFromDate: date when trader has been started
     * **/
    public TraderDetails(long lastTraderActivity, String traderType, String traderStatus, String traderPlatform,
                         int refreshPricesTime, long runningFromDate) {
        initTimeFormatters();
        boolean isInMillis = refreshPricesTime > 3600;
        if(lastTraderActivity < 0)
            throw new IllegalArgumentException("Last trader activity timestamp cannot be less than 0");
        else {
            this.lastTraderActivity = timeFormat.format(new Date(lastTraderActivity));
            lastTraderActivityTimestamp = lastTraderActivity;
        }
        if(!traderType.equals(TRADER_TYPE_AUTONOMOUS) && !traderType.equals(TRADER_TYPE_MANUAL))
            throw new IllegalArgumentException("Trader type inserted is wrong value, can be AUTONOMOUS or MANUAL type");
        else
            this.traderType = traderType;
        if(!traderStatus.equals(RUNNING_TRADER_STATUS) && !traderStatus.equals(STOPPED_TRADER_STATUS))
            throw new IllegalArgumentException("Trader status inserted is wrong value, can be RUNNING or STOPPED status");
        else
            this.traderStatus = traderStatus;
        if(!traderPlatform.equals(BINANCE_TRADER_PLATFORM) && !traderPlatform.equals(COINBASE_TRADER_PLATFORM))
            throw new IllegalArgumentException("Trader platform inserted is not supported yet or is a wrong value");
        else
            this.traderPlatform = traderPlatform;
        if(isInMillis)
            refreshPricesTime /= 1000;
        if(refreshPricesTime < 5 || refreshPricesTime > 3600)
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        else {
            if(isInMillis)
                refreshPricesTime *= 1000;
            this.refreshPricesTime = refreshPricesTime;
        }
        if(runningFromDate < 0)
            throw new IllegalArgumentException("Running from date timestamp cannot be less than 0");
        else
            this.runningFromDate = runningFromDate;
    }

    public String getLastTraderActivity() {
        if((System.currentTimeMillis() - lastTraderActivityTimestamp) >= ((86400 * 1000) / 2))
            lastTraderActivity = dayPassFormat.format(new Date(lastTraderActivityTimestamp));
        return lastTraderActivity;
    }

    public void setLastTraderActivity(long lastTraderActivity) {
        if(lastTraderActivity < 0)
            throw new IllegalArgumentException("Last trader activity timestamp cannot be less than 0");
        this.lastTraderActivity = timeFormat.format(new Date(lastTraderActivity));
        lastTraderActivityTimestamp = lastTraderActivity;
    }

    public String getTraderType() {
        return traderType;
    }

    public String getTraderStatus() {
        return traderStatus;
    }

    public void setTraderStatus(String traderStatus) {
        if(!traderStatus.equals(RUNNING_TRADER_STATUS) && !traderStatus.equals(STOPPED_TRADER_STATUS))
            throw new IllegalArgumentException("Trader status inserted is wrong value, can be RUNNING or STOPPED status");
        this.traderStatus = traderStatus;
    }

    public String getTraderPlatform() {
        return traderPlatform;
    }

    /**
     * This method is used to get color of platform that trader is using<br>
     * Any params required
     * @return color platform as {@link String}
     * **/
    public String getTraderPlatformColor(){
        switch (traderPlatform){
            case BINANCE_TRADER_PLATFORM: return BINANCE_COLOR;
            case COINBASE_TRADER_PLATFORM: return COINBASE_COLOR;
            default: return CUSTOM_DEF_COLOR;
        }
    }

    public long getRefreshPricesTime() {
        return refreshPricesTime;
    }

    public void setRefreshPricesTime(int refreshPricesTime) {
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            this.refreshPricesTime = refreshPricesTime;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
    }

    public long getRunningFromDate() {
        return runningFromDate;
    }

    public void setRunningFromDate(long runningFromDate){
        if(runningFromDate < 0)
            throw new IllegalArgumentException("Running from date timestamp cannot be less than 0");
        else
            this.runningFromDate = runningFromDate;
    }

    /**
     * This method is used to get date of {@link #runningFromDate} timestamp
     * Any params required
     * @return date from last start as {@link String}
     * **/
    public String getRunningDate(){
        return dayPassFormat.format(runningFromDate);
    }

    /**
     * This method is used to get trader details <br>
     * Any params required
     * @return trader details as {@link HashMap} of {@link Object}
     * **/
    public HashMap<String, Object> getTrader() {
        HashMap<String, Object> trader = new HashMap<>();
        trader.put(RUNNING_FROM_DATE_KEY, runningFromDate);
        trader.put(TRADER_TYPE_KEY, traderType);
        trader.put(LAST_TRADER_ACTIVITY_KEY, lastTraderActivityTimestamp);
        trader.put(REFRESH_PRICES_TIME_KEY, refreshPricesTime);
        trader.put(TRADER_PLATFORM_KEY, traderPlatform);
        trader.put(TRADER_STATUS_KEY, traderStatus);
        return trader;
    }

    // TODO: 23/06/2022 REQUEST TO SERVER FOR REFRESH DATA

}

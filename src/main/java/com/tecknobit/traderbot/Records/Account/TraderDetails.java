package com.tecknobit.traderbot.Records.Account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.text.DateFormat.DATE_FIELD;
import static java.text.DateFormat.getDateInstance;

/**
 * The {@code TraderDetails} class is useful to contains details of trader used. <br>
 * Is useful for Android's type traders.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class TraderDetails {

    /**
     * {@code dayPassFormat} is instance that memorize format of {@link #lastTraderActivity} when a day from last activity
     * is passed
     * **/
    private static final DateFormat dayPassFormat = getDateInstance(DATE_FIELD, Locale.getDefault());

    /**
     * {@code timeFormat} is instance that memorize default format of {@link #lastTraderActivity}
     * **/
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

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
     * {@code traderPlatform} is instance that memorize date when trader has been started
     * **/
    private final String runningFromDate;

    /** Constructor to init {@link TraderDetails}
     * @param traderType: type of trader in use {@link #TRADER_TYPE_AUTONOMOUS} or {@link #TRADER_TYPE_MANUAL}
     * @param traderPlatform: platform of trader in use {@link #BINANCE_TRADER_PLATFORM} or {@link #COINBASE_TRADER_PLATFORM}
     * @param runningFromDate: date when trader has been started
     * **/
    public TraderDetails(String traderType, String traderPlatform, String runningFromDate) {
        if(!traderType.equals(TRADER_TYPE_AUTONOMOUS) && !traderType.equals(TRADER_TYPE_MANUAL))
            throw new IllegalArgumentException("Trader type inserted is wrong value, can be AUTONOMOUS or MANUAL type");
        else
            this.traderType = traderType;
        if(!traderPlatform.equals(BINANCE_TRADER_PLATFORM) && !traderPlatform.equals(COINBASE_TRADER_PLATFORM))
            throw new IllegalArgumentException("Trader platform inserted is not supported yet or is a wrong value");
        else
            this.traderPlatform = traderPlatform;
        if(runningFromDate == null || runningFromDate.trim().isEmpty())
            throw new IllegalArgumentException("Running from date cannot be null or empty");
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
                         int refreshPricesTime, String runningFromDate) {
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
        if(refreshPricesTime < 5 || refreshPricesTime > 3600)
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        else
            this.refreshPricesTime = refreshPricesTime;
        if(runningFromDate == null || runningFromDate.trim().isEmpty())
            throw new IllegalArgumentException("Running from date cannot be null or empty");
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

    public long getRefreshPricesTime() {
        return refreshPricesTime;
    }

    public void setRefreshPricesTime(int refreshPricesTime) {
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            this.refreshPricesTime = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
    }

    public String getRunningFromDate() {
        return runningFromDate;
    }

}

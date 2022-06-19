package com.tecknobit.traderbot.Records.Account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static java.text.DateFormat.DATE_FIELD;
import static java.text.DateFormat.getDateInstance;
import static java.util.Locale.getDefault;

/**
 * The {@code Trader} class is useful for {@link TraderDetails} and {@link TraderAccount} objects<br>
 * Is useful for Android's type traders.
 * @author Tecknobit N7ghtm4r3
 * **/

public abstract class Trader {

    /**
     * {@code dayPassFormat} is instance that memorize format when a day from last activity
     * is passed
     * **/
    protected static DateFormat dayPassFormat;

    /**
     * {@code timeFormat} is instance that memorize default format
     * **/
    protected static SimpleDateFormat timeFormat;

    /**
     * This method is used to init {@link #dayPassFormat} and {@link #timeFormat} time formatters
     * Any params required <br>
     * Any return
     * **/
    protected void initTimeFormatters(){
        dayPassFormat = getDateInstance(DATE_FIELD, getDefault());
        timeFormat = new SimpleDateFormat("HH:mm:ss", getDefault());
    }

    public interface TraderManager {

        /**
         * {@code LOSS_SELL} is code to indicate a sell in losing
         * **/
        int LOSS_SELL = 0;

        /**
         * {@code GAIN_SELL} is code to indicate a sell in gaining
         * **/
        int GAIN_SELL = 1;

        /**
         * {@code GAIN_SELL} is code to indicate a sell in pairing
         * **/
        int PAIR_SELL = 2;

        /**
         * This method is used to get if bot is in running mode
         * @return flag that indicates if the bot is running
         * **/
        boolean isTraderRunning();

        /**
         * This method is used to disable running mode of bot
         * **/
        void disableTrader();

        /**
         * This method is used to enable running mode of bot
         * **/
        void enableTrader();

        /**
         * This method is used to get sales at loss
         * @return sales at loss
         * **/
        double getSalesAtLoss();

        /**
         * This method is used to get sales at gain
         * @return sales at gain
         * **/
        double getSalesAtGain();

        /**
         * This method is used to get sales at pair
         * @return sales at pair
         * **/
        double getSalesInPair();

        /**
         * This method is used to get total sales
         * @return total sales
         * **/
        double getTotalSales();

        /**
         * This method is used to set base currency for change amount value
         * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
         * **/
        void setBaseCurrency(String baseCurrency);

        /**
         * This method is used to get base currency for change amount value <br>
         * Any params required
         * **/
        String getBaseCurrency();

    }

}

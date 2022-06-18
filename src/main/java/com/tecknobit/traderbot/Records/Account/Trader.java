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

}

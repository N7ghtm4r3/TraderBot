package com.tecknobit.traderbot.Records.Portfolio;

import com.tecknobit.traderbot.Routines.RoutineMessages;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.BUY;
import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.SELL;
import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

/**
 * The {@code Transaction} class defines Transaction object. <br>
 * This object is useful for fetch and format transactions list details.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class Transaction implements RoutineMessages {

    /**
     * {@code symbol} is instance that memorize symbol of transaction es. BTCBUSD or BTC-USD
     * **/
    private final String symbol;

    /**
     * {@code side} is instance that memorize side of transaction BUY or SELL
     * **/
    private final String side;

    /**
     * {@code transactionDate} is instance that memorize date of that transaction es 21:08:22 24/05/2022
     * **/
    private final String transactionDate;

    /**
     * {@code value} is instance that memorize value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * **/
    private final double value;

    /**
     * {@code quantity} is instance that memorize value of quantity transfered in that transaction es. 1 BTC
     * **/
    private final double quantity;

    /** Constructor to init {@link Transaction}
     * @param symbol: symbol of transaction es. BTCBUSD or BTC-USD
     * @param side: side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value: value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity: value of quantity transfered in that transaction es. 1 BTC
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     * **/
    public Transaction(String symbol, String side, String transactionDate, double value, double quantity) {
        if(symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        else
            this.symbol = symbol;
        side = side.toUpperCase();
        if(!side.equals(BUY) && !side.equals(SELL))
            throw new IllegalArgumentException("Side can be only value BUY or SELL");
        else
            this.side = side;
        if(transactionDate == null || transactionDate.isEmpty())
            throw new IllegalArgumentException("Transaction date must contains characters");
        else
            this.transactionDate = transactionDate;
        if(value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        else
            this.value = value;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSide() {
        return side;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public double getValue() {
        return value;
    }

    /**
     * This method is used get {@link #value} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #value} formatted as 22.21
     * **/
    public double getValue(int decimals) {
        return tradingTools.roundValue(value, decimals);
    }

    public double getQuantity() {
        return quantity;
    }

    /**
     * This method is used to print details of {@link Transaction} object <br>
     * Any params required
     * **/
    @Override
    public void printDetails() {
        out.println("## [" + symbol + "]\n" +
                getSideInfoLine() +
                "## Date: " + transactionDate + "\n" +
                "## Amount value: " + value + "\n" +
                "## Quantity: " + quantity + "\n" +
                "######################");
    }

    /**
     * This method is used to format colored string for side detail line.<br>
     * Any params required
     * @return side string colored and formatted as {@link String}
     * **/
    private String getSideInfoLine(){
        if(side.equals(BUY))
            return "## Side: " + ANSI_GREEN + side + ANSI_RESET + "\n";
        return "## Side: " + ANSI_RED + side + ANSI_RESET + "\n";
    }

}


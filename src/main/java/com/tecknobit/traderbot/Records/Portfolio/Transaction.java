package com.tecknobit.traderbot.Records.Portfolio;

import com.tecknobit.traderbot.Routines.Interfaces.RecordDetails;

import java.util.HashMap;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.BUY;
import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.SELL;
import static com.tecknobit.traderbot.Records.Account.Trader.TraderManager.*;
import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.*;
import static com.tecknobit.traderbot.Records.Portfolio.Token.BASE_ASSET_KEY;
import static com.tecknobit.traderbot.Records.Portfolio.Token.QUANTITY_KEY;
import static com.tecknobit.traderbot.Routines.Autonomous.AutoTraderCoreRoutines.*;
import static com.tecknobit.traderbot.Routines.Interfaces.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

/**
 * The {@code Transaction} class defines Transaction object. <br>
 * This object is useful for fetch and format transactions list details.
 * @author Tecknobit N7ghtm4r3
 * **/

public class Transaction implements RecordDetails {

    /**
     * {@code TRANSACTION_KEY} is instance that memorize transaction key
     * **/
    public static final String TRANSACTION_KEY = "transaction";

    /**
     * {@code SIDE_KEY} is instance that memorize side key
     * **/
    public static final String SIDE_KEY = "side";

    /**
     * {@code TRANSACTION_DATE_KEY} is instance that memorize transaction date key
     * **/
    public static final String TRANSACTION_DATE_KEY = "transaction_date";

    /**
     * {@code VALUE_KEY} is instance that memorize value key
     * **/
    public static final String VALUE_KEY = "value";

    /**
     * {@code TRANSACTION_TYPE_KEY} is instance that memorize transaction type key
     * **/
    public static final String TRANSACTION_TYPE_KEY = "transaction_type";

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

    /**
     * {@code quoteAsset} is instance that memorize quote asset used in that transaction es. EUR
     * **/
    private final String quoteAsset;

    /**
     * {@code baseAsset} is instance that memorize base asset used in that transaction es. BTC
     * **/
    private final String baseAsset;

    /**
     * {@code incomePercent} is instance that memorize income of that transaction es. +1.5%
     * **/
    private double incomePercent;

    /**
     * {@code transactionType} is instance that memorize type of that transaction es. sold at gain, at loss or at pair
     * **/
    private String transactionType;

    /**
     * Constructor to init {@link Transaction}
     * @param symbol: symbol of transaction es. BTCBUSD or BTC-USD
     * @param side: side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value: value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity: value of quantity transfered in that transaction es. 1 BTC
     * @param quoteAsset: quote asset used in that transaction es. EUR
     * @param baseAsset: base asset used in that transaction es. BTC
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, String side, String transactionDate, double value, double quantity,
                       String quoteAsset, String baseAsset) {
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
        if(quoteAsset == null || quoteAsset.isEmpty())
            throw new IllegalArgumentException("Quote asset must contains characters");
        else
            this.quoteAsset = quoteAsset;
        if(baseAsset == null || baseAsset.isEmpty())
            throw new IllegalArgumentException("Base asset must contains characters");
        else
            this.baseAsset = baseAsset;
        incomePercent = ASSET_NOT_TRADABLE;
        transactionType = null;
    }

    /**
     * Constructor to init {@link Transaction}
     * @param symbol: symbol of transaction es. BTCBUSD or BTC-USD
     * @param side: side of transaction BUY or SELL
     * @param transactionDate: date of that transaction es 21:08:22 24/05/2022
     * @param value: value of symbol in fiat currency amount of transaction transfered es. 1235 USD
     * @param quantity: value of quantity transfered in that transaction es. 1 BTC
     * @param quoteAsset: quote asset used in that transaction es. EUR
     * @param baseAsset: base asset used in that transaction es. BTC
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     **/
    public Transaction(String symbol, String side, String transactionDate, double value, double quantity,
                       String quoteAsset, String baseAsset, double incomePercent) {
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
        if(quoteAsset == null || quoteAsset.isEmpty())
            throw new IllegalArgumentException("Quote asset must contains characters");
        else
            this.quoteAsset = quoteAsset;
        if(baseAsset == null || baseAsset.isEmpty())
            throw new IllegalArgumentException("Base asset must contains characters");
        else
            this.baseAsset = baseAsset;
        if(incomePercent < -100)
            throw new IllegalArgumentException("Income percent must be bigger than -100");
        else
            this.incomePercent = incomePercent;
        transactionType = null;
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

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public double getIncomePercent() {
        return incomePercent;
    }

    /**
     * This method is used get {@link #incomePercent} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #incomePercent} formatted as 21.22
     * **/
    public double getIncomePercent(int decimals) {
        return tradingTools.roundValue(incomePercent, decimals);
    }

    public void setIncomePercent(double incomePercent) {
        if(incomePercent < -100)
            throw new IllegalArgumentException("Income percent must be bigger than -100");
        this.incomePercent = incomePercent;
    }

    public void setTransactionType(String transactionType) {
        if(!transactionType.equals(GAIN_SELL) && !transactionType.equals(LOSS_SELL)
                && !transactionType.equals(PAIR_SELL))
            throw new IllegalArgumentException("Transaction type can be only be GAIN_SELL, LOSS_SELL or PAIR_SELL");
        this.transactionType = transactionType;
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
                "## Base asset: " + baseAsset + "\n" +
                "## Quote asset: " + quoteAsset + "\n" +
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

    /**
     * This method is used to get transaction details <br>
     * Any params required
     * @return transaction details as {@link HashMap} of {@link Object}
     * **/
    public HashMap<String, Object> getTransaction() {
        HashMap<String, Object> transaction = new HashMap<>();
        transaction.put(SYMBOL_KEY, symbol);
        transaction.put(SIDE_KEY, side);
        transaction.put(TRANSACTION_DATE_KEY, transactionDate);
        transaction.put(QUANTITY_KEY, quantity);
        transaction.put(QUOTE_ASSET_KEY, quoteAsset);
        transaction.put(VALUE_KEY, value);
        transaction.put(BASE_ASSET_KEY, baseAsset);
        if(incomePercent != ASSET_NOT_TRADABLE)
            transaction.put(INCOME_PERCENT_KEY, incomePercent);
        if(transactionType != null)
            transaction.put(TRANSACTION_TYPE_KEY, transactionType);
        return transaction;
    }

}


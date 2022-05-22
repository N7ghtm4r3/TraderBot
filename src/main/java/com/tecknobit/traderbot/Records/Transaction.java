package com.tecknobit.traderbot.Records;

import com.tecknobit.traderbot.Routines.RoutineMessages;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.BUY;
import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.SELL;
import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

public final class Transaction implements RoutineMessages {

    private final String symbol;
    private final String side;
    private final String transactionDate;
    private final double value;
    private final double quantity;

    public Transaction(String symbol, String side, String transactionDate, double value, double quantity) {
        if(symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("Symbol must contains characters");
        this.symbol = symbol;
        side = side.toUpperCase();
        if(!side.equals(BUY) && !side.equals(SELL))
            throw new IllegalArgumentException("Side can be only value BUY or SELL");
        this.side = side;
        if(transactionDate == null || transactionDate.isEmpty())
            throw new IllegalArgumentException("Transaction date must contains characters");
        this.transactionDate = transactionDate;
        if(value < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        this.value = value;
        if(quantity < 0)
            throw new IllegalArgumentException("Quantity value cannot be less than 0");
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

    public double getValue(int decimals) {
        return tradingTools.roundValue(value, decimals);
    }

    public double getQuantity() {
        return quantity;
    }

    @Override
    public void printDetails() {
        out.println("## [" + symbol + "]\n" +
                "## Side: " + side + "\n" +
                "## Date: " + transactionDate + "\n" +
                "## Amount value: " + value + "\n" +
                "## Quantity: " + quantity + "\n" +
                "######################");
    }

}


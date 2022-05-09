package com.tecknobit.traderbot.Records;

public class Transaction {

    private final String symbol;
    private final String side;
    private final String transactionDate;
    private final double value;
    private final double quantity;

    public Transaction(String symbol, String side, String transactionDate, double value, double quantity) {
        this.symbol = symbol;
        this.side = side;
        this.transactionDate = transactionDate;
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

    public double getValue(int decimals) {
        return value;
    }

    public double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "symbol='" + symbol + '\'' +
                ", side='" + side + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", value=" + value +
                ", quantity=" + quantity +
                '}';
    }

}


package com.tecknobit.traderbot.Records;

/**
 * The {@code Asset} class defines Asset object. <br>
 * This object is useful for format user wallet asset.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class Asset extends Token {

    /**
     * {@code balance} is instance that memorize balance for this asset and is constantly refreshed.
     * **/
    private double balance;

    /**
     * {@code currency} is instance that memorize currency for balance valuating es. EUR
     * **/
    private final String currency;

    /** Constructor to init {@link Asset}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @param balance: balance for this asset
     * @param currency: currency for balance valuating es. EUR
     * **/
    public Asset(String assetIndex, String assetName, double quantity, double balance, String currency) {
        super(assetIndex, assetName, quantity);
        if(balance < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        this.balance = balance;
        if(currency == null || currency.isEmpty())
            throw new IllegalArgumentException("Currency must contains characters");
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if(balance < 0)
            throw new IllegalArgumentException("Value cannot be less than 0");
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

}

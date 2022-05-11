package com.tecknobit.traderbot.Records;

public final class Coin {

    private final String name;
    private boolean tradingEnabled;
    private final String asset;
    private double balance;

    public Coin(String name, boolean tradingEnabled, String asset, double balance) {
        this.name = name;
        this.tradingEnabled = tradingEnabled;
        this.asset = asset;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public boolean isTradingEnabled() {
        return tradingEnabled;
    }

    public void setTradingEnabled(boolean tradingEnabled) {
        this.tradingEnabled = tradingEnabled;
    }

    public String getAsset() {
        return asset;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if(balance < 0)
            throw new IllegalArgumentException("Balance value cannot be less than 0");
        this.balance = balance;
    }

}

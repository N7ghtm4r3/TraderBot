package com.tecknobit.traderbot.Records;

public final class Coin extends Token{

    private boolean tradingEnabled;

    public Coin(String assetIndex, String assetName, double quantity, boolean tradingEnabled) {
        super(assetIndex, assetName, quantity);
        this.tradingEnabled = tradingEnabled;
    }

    public boolean isTradingEnabled() {
        return tradingEnabled;
    }

    public void setTradingEnabled(boolean tradingEnabled) {
        this.tradingEnabled = tradingEnabled;
    }

}

package com.tecknobit.traderbot.Records;

/**
 * The {@code Coin} class defines Coin object. <br>
 * This object is useful for all traders routines and also useful as asset container
 * @author Tecknobit N7ghtm4r3
 * **/

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

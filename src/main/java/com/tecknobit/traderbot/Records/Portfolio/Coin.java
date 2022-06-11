package com.tecknobit.traderbot.Records.Portfolio;

import java.io.Serializable;

/**
 * The {@code Coin} class defines Coin object. <br>
 * This object is useful for all traders routines and also useful as asset container
 * @author Tecknobit N7ghtm4r3
 * **/

public final class Coin extends Token implements Serializable {

    /**
     * {@code tradingEnabled} is instance that memorize if this coin is available or not for trading or if is not bought yet.
     * **/
    private boolean tradingEnabled;

    /** Constructor to init {@link Asset}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @param tradingEnabled: available or not for trading or if is not bought yet
     * **/
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

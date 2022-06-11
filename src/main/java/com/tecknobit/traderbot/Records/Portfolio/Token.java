package com.tecknobit.traderbot.Records.Portfolio;

/**
 * The {@code Token} class defines base methods for asset and coin objects. <br>
 * These methods allow to use inheritance object more efficiently.
 * @author Tecknobit N7ghtm4r3
 * **/

public class Token {

    /**
     * {@code assetIndex} is instance that memorize index of token es. BTC
     * **/
    protected final String assetIndex;

    /**
     * {@code assetName} is instance that memorize full name of token es Bitcoin
     * **/
    protected final String assetName;

    /**
     * {@code quantity} is instance that memorize value of quantity bought for this token es 1
     * **/
    protected double quantity;

    /** Constructor to init {@link Cryptocurrency}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @throws IllegalArgumentException when parameters inserted do not respect right value form.
     * **/
    public Token(String assetIndex, String assetName, double quantity) {
        if(assetIndex == null || assetIndex.isEmpty())
            throw new IllegalArgumentException("Asset index must contains characters");
        this.assetIndex = assetIndex;
        if(assetName == null || assetName.isEmpty())
            throw new IllegalArgumentException("Asset name must contains characters");
        this.assetName = assetName;
        if(quantity < 0)
            throw new IllegalArgumentException("Balance value cannot be less than 0");
        this.quantity = quantity;
    }

    public String getAssetIndex() {
        return assetIndex;
    }

    public String getAssetName() {
        return assetName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        if(quantity < 0)
            throw new IllegalArgumentException("Balance value cannot be less than 0");
        this.quantity = quantity;
    }

}

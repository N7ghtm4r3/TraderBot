package com.tecknobit.traderbot.Records;

/**
 * The {@code Token} class defines base methods for asset and coin objects. <br>
 * These methods allow to use inheritance object more efficiently.
 * @author Tecknobit N7ghtm4r3
 * **/

public class Token {

    private final String assetIndex;
    private final String assetName;
    private double quantity;

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

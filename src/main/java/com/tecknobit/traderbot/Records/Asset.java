package com.tecknobit.traderbot.Records;

public class Asset {

    private final String index;
    private final String assetName;
    private final double quantity;
    private final double value;

    private final String currency;
    public Asset(String index, String assetName, double quantity, double value, String currency) {
        this.index = index;
        this.assetName = assetName;
        this.quantity = quantity;
        this.value = value;
        this.currency = currency;
    }

    public String getIndex() {
        return index;
    }

    public String getAssetName() {
        return assetName;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "index='" + index + '\'' +
                ", assetName='" + assetName + '\'' +
                ", quantity=" + quantity +
                ", value=" + value +
                ", currency='" + currency + '\'' +
                '}';
    }

}

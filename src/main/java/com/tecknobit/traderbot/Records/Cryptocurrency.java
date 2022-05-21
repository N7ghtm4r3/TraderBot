package com.tecknobit.traderbot.Records;

import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines.TradingConfig;

public final class Cryptocurrency extends Token{

    private final String symbol;
    private final double lastPrice;
    private final double tptopIndex;
    private final Object candleGap;
    private final double priceChangePercent;
    private final String quoteAsset;
    private final TradingConfig tradingConfig;

    public Cryptocurrency(String assetIndex, String assetName, double quantity, String symbol, double lastPrice,
                          double tptopIndex, Object candleGap, double priceChangePercent, String quoteAsset,
                          TradingConfig tradingConfig) {
        super(assetIndex, assetName, quantity);
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.tptopIndex = tptopIndex;
        this.candleGap = candleGap;
        this.priceChangePercent = priceChangePercent;
        this.quoteAsset = quoteAsset;
        this.tradingConfig = tradingConfig;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public double getTptopIndex() {
        return tptopIndex;
    }

    public Object getCandleGap() {
        return candleGap;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public TradingConfig getTradingConfig() {
        return tradingConfig;
    }

    @Override
    public String toString() {
        return "Cryptocurrency{" +
                "symbol='" + symbol + '\'' +
                ", lastPrice=" + lastPrice +
                ", tptopIndex=" + tptopIndex +
                ", candleGap=" + candleGap +
                ", priceChangePercent=" + priceChangePercent +
                ", quoteAsset='" + quoteAsset + '\'' +
                ", tradingConfig=" + tradingConfig +
                ", quantity= " + getQuantity() +
                '}';
    }

}

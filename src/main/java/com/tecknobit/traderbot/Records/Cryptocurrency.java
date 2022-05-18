package com.tecknobit.traderbot.Records;

import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines.TradingConfig;

public final class Cryptocurrency extends Token{

    private final String symbol;
    private final double lastPrice;
    private final double tptopIndex;
    private final Object candleGap;
    private final TradingConfig tradingConfig;

    public Cryptocurrency(String assetIndex, String assetName, double quantity, String symbol,
                          double lastPrice, double tptopIndex, Object candleGap, TradingConfig tradingConfig) {
        super(assetIndex, assetName, quantity);
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.tptopIndex = tptopIndex;
        this.candleGap = candleGap;
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

    public TradingConfig getTradingConfig() {
        return tradingConfig;
    }

}

package com.tecknobit.traderbot.Records;

import com.tecknobit.traderbot.Routines.RoutineMessages;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines.TradingConfig;

import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

public final class Cryptocurrency extends Token implements RoutineMessages {

    private final String symbol;
    private double firstPrice;
    private double lastPrice;
    private final double tptopIndex;
    private final Object candleGap;
    private final double priceChangePercent;
    private final String quoteAsset;
    private double trendPercent;
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
        firstPrice = -1;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(double firstPrice) {
        if(firstPrice < 0)
            throw new IllegalArgumentException("First price cannot be less than 0");
        if(this.firstPrice == -1)
            this.firstPrice = firstPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        if(lastPrice < 0)
            throw new IllegalArgumentException("Last price cannot be less than 0");
        this.lastPrice = lastPrice;
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

    public double getTrendPercent() {
        return trendPercent;
    }

    public String getTextTrendPercent(){
        return tradingTools.textualizeAssetPercent(trendPercent);
    }

    public String getTextTrendPercent(int decimals){
        return tradingTools.textualizeAssetPercent(tradingTools.roundValue(trendPercent, decimals));
    }

    public String getTextTptopIndex(){
        return tradingTools.textualizeAssetPercent(tptopIndex);
    }

    public String getTextTptopIndex(int decimals){
        return tradingTools.textualizeAssetPercent(tradingTools.roundValue(tptopIndex, decimals));
    }

    public void setTrendPercent(double trendPercent) {
        if(trendPercent < -100)
            throw new IllegalArgumentException("Trend percent cannot be less than -100");
        this.trendPercent = trendPercent;
    }

    public TradingConfig getTradingConfig() {
        return tradingConfig;
    }

    @Override
    public void printDetails() {
        out.println("## [" + symbol + "]\n" +
                getANSIText("## Trend: ", getTextTrendPercent(2)) +
                "## Last: " + lastPrice + "\n" +
                "## Quantity: " + getQuantity() + "\n" +
                getANSIText("## Estimated asset trend: ", getTextTptopIndex(2)) +
                "######################");
    }

    private String getANSIText(String tail, String percent){
        if(percent.contains("+"))
            return tail + ANSI_GREEN + percent + ANSI_RESET + "\n";
        else if (percent.contains("-"))
            return tail + ANSI_RED + percent + ANSI_RESET + "\n";
        else
            return tail + percent + "\n";
    }

}

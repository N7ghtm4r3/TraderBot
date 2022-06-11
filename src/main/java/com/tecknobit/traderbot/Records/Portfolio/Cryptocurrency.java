package com.tecknobit.traderbot.Records.Portfolio;

import com.tecknobit.traderbot.Routines.RoutineMessages;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines.TradingConfig;

import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

/**
 * The {@code Cryptocurrency} class defines Cryptocurrency object. <br>
 * This object is useful for auto trader bot routines and useful also for Android's interfaces, <br>
 * because allow to interact with specific asset, and it's trading details like trend percent and last price.
 * @author Tecknobit N7ghtm4r3
 * **/

public class Cryptocurrency extends Token implements RoutineMessages {

    /**
     * {@code symbol} is instance that memorize symbol of cryptocurrency es. BTCBUSD or BTC-USD
     * **/
    private final String symbol;

    /**
     * {@code firstPrice} is instance that memorize first inserted price of cryptocurrency and not more changeable.
     * **/
    private double firstPrice;

    /**
     * {@code lastPrice} is instance that memorize last inserted price of cryptocurrency and is constantly refreshed.
     * **/
    private double lastPrice;

    /**
     * {@code tptopIndex} is instance that memorize forecast trend of cryptocurrency.
     * **/
    private final double tptopIndex;

    /**
     * {@code candleGap} is instance that memorize day gap for forecast trend of cryptocurrency.
     * **/
    private final Object candleGap;

    /**
     * {@code priceChangePercent} is instance that memorize previous day percent gap of trend of cryptocurrency.
     * **/
    private final double priceChangePercent;

    /**
     * {@code quoteAsset} is instance that memorize quote asset to buy this cryptocurrency es. USD
     * **/
    private final String quoteAsset;

    /**
     * {@code trendPercent} is instance that memorize trend percent of cryptocurrency and is constantly refreshed.
     * **/
    private double trendPercent;

    /**
     * {@code tradingConfig} is instance that memorize model of trading to use for this cryptocurrency.
     * **/
    private final TradingConfig tradingConfig;

    /** Constructor to init {@link Cryptocurrency}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @param symbol: symbol of cryptocurrency es. BTCBUSD or BTC-USD
     * @param lastPrice: last inserted price of cryptocurrency
     * @param tptopIndex: forecast trend of cryptocurrency
     * @param candleGap: previous day percent gap of trend of cryptocurrency
     * @param priceChangePercent: previous day percent gap of trend of cryptocurrency
     * @param quoteAsset: quote asset to buy this cryptocurrency es. USD
     * @param tradingConfig: model of trading to use for this cryptocurrency
     * **/
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

    /** Constructor to init {@link Cryptocurrency}
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @param symbol: symbol of cryptocurrency es. BTCBUSD or BTC-USD
     * @param lastPrice: last inserted price of cryptocurrency
     * @param tptopIndex: forecast trend of cryptocurrency
     * @param candleGap: previous day percent gap of trend of cryptocurrency
     * @param priceChangePercent: previous day percent gap of trend of cryptocurrency
     * @param quoteAsset: quote asset to buy this cryptocurrency es. USD
     * @param trendPercent: trend that this asset is having compare to {@link #firstPrice} and {@link #lastPrice}
     * @param tradingConfig: model of trading to use for this cryptocurrency
     * **/
    public Cryptocurrency(String assetIndex, String assetName, double quantity, String symbol, double lastPrice,
                          double tptopIndex, Object candleGap, double priceChangePercent, String quoteAsset,
                          double trendPercent, TradingConfig tradingConfig) {
        super(assetIndex, assetName, quantity);
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.tptopIndex = tptopIndex;
        this.candleGap = candleGap;
        this.priceChangePercent = priceChangePercent;
        this.quoteAsset = quoteAsset;
        this.trendPercent = trendPercent;
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

    /**
     * This method is used get {@link #lastPrice} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #lastPrice} formatted as 1.65 or -1.65
     * **/
    public double getLastPrice(int decimals){
        return tradingTools.roundValue(lastPrice, decimals);
    }

    public void setLastPrice(double lastPrice) {
        if(lastPrice < 0)
            throw new IllegalArgumentException("Last price cannot be less than 0");
        this.lastPrice = lastPrice;
    }

    public double getTptopIndex() {
        return tptopIndex;
    }

    /**
     * This method is used get {@link #tptopIndex} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #tptopIndex} formatted as 1.65 or -1.65
     * **/
    public double getTptopIndex(int decimals) {
        return tradingTools.roundValue(tptopIndex, decimals);
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

    /**
     * This method is used get {@link #trendPercent} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #trendPercent} formatted as 1.65 or -1.65
     * **/
    public double getTrendPercent(int decimals){
        return tradingTools.roundValue(trendPercent, decimals);
    }

    /**
     * This method is used to format like a {@link String} object {@link #trendPercent}.<br>
     * Any params required
     * @return {@link #trendPercent} formatted as +1.653% or -1.6563% as {@link String}
     * **/
    public String getTextTrendPercent(){
        return tradingTools.textualizeAssetPercent(trendPercent);
    }

    /**
     * This method is used to format like a {@link String} object {@link #trendPercent}.<br>
     * @param decimals: number of decimal digits es. 2
     * @return {@link #trendPercent} formatted as +1.65% or -1.65% as {@link String}
     * **/
    public String getTextTrendPercent(int decimals){
        return tradingTools.textualizeAssetPercent(tradingTools.roundValue(trendPercent, decimals));
    }

    /**
     * This method is used to format like a {@link String} object {@link #tptopIndex}.<br>
     * Any params required
     * @return {@link #tptopIndex} formatted as +1.653% or -1.6563% as {@link String}
     * **/
    public String getTextTptopIndex(){
        return tradingTools.textualizeAssetPercent(tptopIndex);
    }

    /**
     * This method is used to format like a {@link String} object {@link #tptopIndex}.<br>
     * @param decimals: number of decimal digits es. 2
     * @return {@link #tptopIndex} formatted as +1.65% or -1.65%
     * **/
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

    /**
     * This method is used to get balance for this cryptocurrency
     * Any params required
     * @return balance for this cryptocurrency es 2221
     * **/
    public double getCryptocurrencyBalance(){
        return lastPrice * quantity;
    }

    /**
     * This method is used to get balance for this cryptocurrency
     * @param decimals: number of decimal digits es. 2221.08
     * @return balance for this cryptocurrency
     * **/
    public double getCryptocurrencyBalance(int decimals){
        return tradingTools.roundValue(getCryptocurrencyBalance(), decimals);
    }

    /**
     * This method is used to print details of {@link Cryptocurrency} object <br>
     * Any params required
     * **/
    @Override
    public void printDetails() {
        out.println("## [" + symbol + "]\n" +
                getANSIText("## Trend: ", getTextTrendPercent(2)) +
                "## Last: " + lastPrice + "\n" +
                "## Quantity: " + quantity + "\n" +
                "## Portfolio balance: " + getCryptocurrencyBalance(2) + "\n" +
                getANSIText("## Estimated asset trend: ", getTextTptopIndex(2)) +
                "######################");
    }

    /**
     * This method is used to format colored string for percent detail line.<br>
     * @param tail: text to indicate the info es. Trend:
     * @param percent: value of percent to color es +8%
     * @return percent string colored and formatted as {@link String}
     * **/
    private String getANSIText(String tail, String percent){
        if(percent.contains("+"))
            return tail + ANSI_GREEN + percent + ANSI_RESET + "\n";
        else if (percent.contains("-"))
            return tail + ANSI_RED + percent + ANSI_RESET + "\n";
        else
            return tail + percent + "\n";
    }

}

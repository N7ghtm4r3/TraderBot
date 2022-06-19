package com.tecknobit.traderbot.Records.Portfolio;

import com.tecknobit.traderbot.Routines.Interfaces.RecordDetails;
import com.tecknobit.traderbot.Routines.Autonomous.AutoTraderCoreRoutines.TradingConfig;

import java.util.ArrayList;

import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.*;
import static com.tecknobit.traderbot.Routines.Interfaces.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

/**
 * The {@code Cryptocurrency} class defines Cryptocurrency object. <br>
 * This object is useful for auto trader bot routines and useful also for Android's interfaces, <br>
 * because allow to interact with specific asset, and it's trading details like trend percent and last price.
 * @author Tecknobit N7ghtm4r3
 * **/

public class Cryptocurrency extends Token implements RecordDetails {

    /**
     * {@code symbol} is instance that memorize symbol of cryptocurrency es. BTCBUSD or BTC-USD
     * **/
    private final String symbol;

    /**
     * {@code firstPrice} is instance that memorize first inserted price of cryptocurrency and not more changeable.
     * **/
    private final ArrayList<Double> firstPrices;

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
    private double priceChangePercent;

    /**
     * {@code quoteAsset} is instance that memorize quote asset to buy this cryptocurrency es. USD
     * **/
    private final String quoteAsset;

    /**
     * {@code incomePercent} is instance that memorize income percent of cryptocurrency and is constantly refreshed.
     * **/
    private double incomePercent;

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
        firstPrices = new ArrayList<>();
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
     * @param incomePercent: trend that this asset is having compare to {@link #firstPrices} and {@link #lastPrice}
     * @param tradingConfig: model of trading to use for this cryptocurrency
     * **/
    public Cryptocurrency(String assetIndex, String assetName, double quantity, String symbol, double lastPrice,
                          double tptopIndex, Object candleGap, double priceChangePercent, String quoteAsset,
                          double incomePercent, TradingConfig tradingConfig) {
        super(assetIndex, assetName, quantity);
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.tptopIndex = tptopIndex;
        this.candleGap = candleGap;
        this.priceChangePercent = priceChangePercent;
        this.quoteAsset = quoteAsset;
        this.incomePercent = incomePercent;
        this.tradingConfig = tradingConfig;
        firstPrices = new ArrayList<>();
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
     * @param tradingConfig: model of trading to use for this cryptocurrency
     * @param firstPrices: list of first prices inserted in past orders
     * **/
    public Cryptocurrency(String assetIndex, String assetName, double quantity, String symbol, double lastPrice,
                          double tptopIndex, Object candleGap, double priceChangePercent, String quoteAsset,
                          TradingConfig tradingConfig, ArrayList<Double> firstPrices) {
        super(assetIndex, assetName, quantity);
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.tptopIndex = tptopIndex;
        this.candleGap = candleGap;
        this.priceChangePercent = priceChangePercent;
        this.quoteAsset = quoteAsset;
        this.tradingConfig = tradingConfig;
        this.firstPrices = firstPrices;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getFirstPrice() {
        double firstPrice = 0;
        for (double price : firstPrices)
            firstPrice += price;
        return firstPrice / firstPrices.size();
    }

    /**
     * This method is used to get first price
     * @param decimals: number of decimal digits es. 2
     * @return first price formatted as 1.65 or -1.65
     * **/
    public double getFirstPrice(int decimals){
        return tradingTools.roundValue(getFirstPrice(), decimals);
    }

    /**
     * This method is used to add new first price
     * @param firstPrice: first price to add at {@link #firstPrices} list <br>
     * Any params.
     * **/
    public void addFirstPrice(double firstPrice) {
        if(firstPrice < 0)
            throw new IllegalArgumentException("First price cannot be less than 0");
        firstPrices.add(firstPrice);
    }

    public double getLastPrice() {
        return lastPrice;
    }

    /**
     * This method is used to get {@link #lastPrice} instance
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
     * This method is used to get {@link #tptopIndex} instance
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

    /**
     * This method is used to get {@link #priceChangePercent} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #priceChangePercent} formatted as 1.65 or -1.65
     * **/
    public double getPriceChangePercent(int decimals){
        return tradingTools.roundValue(priceChangePercent, decimals);
    }

    /**
     * This method is used to format like a {@link String} object {@link #priceChangePercent}.<br>
     * Any params required
     * @return {@link #priceChangePercent} formatted as +1.653% or -1.6563% as {@link String}
     * **/
    public String getTextPriceChangePercent(){
        return tradingTools.textualizeAssetPercent(priceChangePercent);
    }

    /**
     * This method is used to format like a {@link String} object {@link #priceChangePercent}.<br>
     * @param decimals: number of decimal digits es. 2
     * @return {@link #priceChangePercent} formatted as +1.65% or -1.65% as {@link String}
     * **/
    public String getTextPriceChangePercent(int decimals){
        return tradingTools.textualizeAssetPercent(tradingTools.roundValue(priceChangePercent, decimals));
    }

    public void setPriceChangePercent(double priceChangePercent) {
        if(priceChangePercent < -100)
            throw new IllegalArgumentException("Price change percent cannot be less than -100");
        this.priceChangePercent = priceChangePercent;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public double getIncomePercent() {
        return incomePercent;
    }

    /**
     * This method is used get {@link #incomePercent} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #incomePercent} formatted as 1.65 or -1.65
     * **/
    public double getIncomePercent(int decimals){
        return tradingTools.roundValue(incomePercent, decimals);
    }

    /**
     * This method is used to format like a {@link String} object {@link #incomePercent}.<br>
     * Any params required
     * @return {@link #incomePercent} formatted as +1.653% or -1.6563% as {@link String}
     * **/
    public String getTextIncomePercent(){
        return tradingTools.textualizeAssetPercent(incomePercent);
    }

    /**
     * This method is used to format like a {@link String} object {@link #incomePercent}.<br>
     * @param decimals: number of decimal digits es. 2
     * @return {@link #incomePercent} formatted as +1.65% or -1.65% as {@link String}
     * **/
    public String getTextIncomePercent(int decimals){
        return tradingTools.textualizeAssetPercent(tradingTools.roundValue(incomePercent, decimals));
    }

    public void setIncomePercent(double incomePercent) {
        if(incomePercent < -100)
            throw new IllegalArgumentException("Trend percent cannot be less than -100");
        this.incomePercent = incomePercent;
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
                getANSIText("## Income: ", getTextIncomePercent(2)) +
                "## Last price: " + lastPrice + "\n" +
                "## Quantity: " + quantity + "\n" +
                getANSIText("## Asset trend: ", getTextPriceChangePercent(2)) +
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

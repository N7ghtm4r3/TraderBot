package com.tecknobit.traderbot.Records.Portfolio;

import com.tecknobit.traderbot.Routines.Interfaces.RecordDetails;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.TradingConfig.MODEL_ID_KEY;
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
     * {@code CRYPTOCURRENCY_KEY} is instance that memorize cryptocurrency key
     * **/
    public static final String CRYPTOCURRENCY_KEY = "cryptocurrency";

    /**
     * {@code SYMBOL_KEY} is instance that memorize symbol key
     * **/
    public static final String SYMBOL_KEY = "symbol";

    /**
     * {@code LAST_PRICE_KEY} is instance that memorize last price key
     * **/
    public static final String LAST_PRICE_KEY = "last_price";

    /**
     * {@code FIRST_PRICE_KEY} is instance that memorize last price key
     * **/
    public static final String FIRST_PRICE_KEY = "first_price";

    /**
     * {@code TPTOP_INDEX_KEY} is instance that memorize tptop index key
     * **/
    public static final String TPTOP_INDEX_KEY = "tptop_index";

    /**
     * {@code CANDLE_GAP_KEY} is instance that memorize candle gap key
     * **/
    public static final String CANDLE_GAP_KEY = "candle_gap";

    /**
     * {@code PRICE_CHANGE_PERCENT_KEY} is instance that memorize price change percent key
     * **/
    public static final String PRICE_CHANGE_PERCENT_KEY = "price_change_percent";

    /**
     * {@code QUOTE_ASSET_KEY} is instance that memorize quote asset key
     * **/
    public static final String QUOTE_ASSET_KEY = "quote_asset";

    /**
     * {@code INCOME_PERCENT_KEY} is instance that memorize income percent key
     * **/
    public static final String INCOME_PERCENT_KEY = "income_percent";

    /**
     * {@code TRADING_CONFIG_KEY} is instance that memorize trading config key
     * **/
    public static final String TRADING_CONFIG_KEY = "trading_config";

    /**
     * {@code FIRST_PRICES_SUM_KEY} is instance that memorize first prices size key
     * **/
    public static final String FIRST_PRICES_SUM_KEY = "first_prices_sum";

    /**
     * {@code FIRST_PRICES_SIZE_KEY} is instance that memorize first prices size key
     * **/
    public static final String FIRST_PRICES_SIZE_KEY = "first_prices_size";

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

    /**
     * {@code firstPricesSum} is instance that memorize sum of first prices of order for cryptocurrency.
     * **/
    private final double firstPricesSum;

    /**
     * {@code previousFirstPricesSize} is instance that memorize size of first prices of order for cryptocurrency <br>
     * passed by previous first prices list
     * **/
    private final int previousFirstPricesSize;

    /**
     * {@code firstPricesSize} is instance that memorize size of first prices of order for cryptocurrency.
     * **/
    private int firstPricesSize;

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
        firstPricesSum = 0;
        previousFirstPricesSize = 0;
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
        firstPricesSum = 0;
        previousFirstPricesSize = 0;
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
        firstPricesSum = 0;
        previousFirstPricesSize = 0;
    }

    /** Constructor to init {@link Cryptocurrency}
     * @implNote is useful for Android's interfaces not for a normal workflow
     * @param assetIndex: index of cryptocurrency es. BTC
     * @param assetName: full name of cryptocurrency es Bitcoin
     * @param quantity: value of quantity bought for this cryptocurrency es 1
     * @param tptopIndex: forecast trend of cryptocurrency
     * @param candleGap: previous day percent gap of trend of cryptocurrency
     * @param tradingConfig: model of trading to use for this cryptocurrency
     * **/
    public Cryptocurrency(String assetIndex, String assetName, double quantity, TradingConfig tradingConfig,
                          double tptopIndex, Object candleGap, String quoteAsset, double firstPricesSum,
                          int previousFirstPricesSize) {
        super(assetIndex, assetName, quantity);
        this.symbol = null;
        this.tptopIndex = tptopIndex;
        this.candleGap = candleGap;
        this.quoteAsset = quoteAsset;
        this.tradingConfig = tradingConfig;
        firstPrices = new ArrayList<>();
        this.firstPricesSum = firstPricesSum;
        this.previousFirstPricesSize = previousFirstPricesSize;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getFirstPrice() {
        if(firstPrices != null) {
            firstPricesSize = firstPrices.size() + previousFirstPricesSize;
            if(firstPricesSize > 0){
                double firstPrice = 0;
                for (double price : firstPrices)
                    firstPrice += price;
                if(firstPrice > 0)
                    return ((firstPrice + firstPricesSum) / firstPricesSize);
            }
        }
        return lastPrice;
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
        if(firstPrices != null)
            return incomePercent = tradingTools.computeAssetPercent(getFirstPrice(), lastPrice);
        return incomePercent;
    }

    /**
     * This method is used get {@link #incomePercent} instance
     * @param decimals: number of decimal digits es. 2
     * @return {@link #incomePercent} formatted as 1.65 or -1.65
     * **/
    public double getIncomePercent(int decimals){
        return tradingTools.roundValue(getIncomePercent(), decimals);
    }

    /**
     * This method is used to format like a {@link String} object {@link #incomePercent}.<br>
     * Any params required
     * @return {@link #incomePercent} formatted as +1.653% or -1.6563% as {@link String}
     * **/
    public String getTextIncomePercent(){
        return tradingTools.textualizeAssetPercent(getIncomePercent());
    }

    /**
     * This method is used to format like a {@link String} object {@link #incomePercent}.<br>
     * @param decimals: number of decimal digits es. 2
     * @return {@link #incomePercent} formatted as +1.65% or -1.65% as {@link String}
     * **/
    public String getTextIncomePercent(int decimals){
        return tradingTools.textualizeAssetPercent(tradingTools.roundValue(getIncomePercent(), decimals));
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

    /**
     * This method is used to get cryptocurrency details <br>
     * Any params required
     * @return cryptocurrency details as {@link HashMap} of {@link Object}
     * **/
    public HashMap<String, Object> getCryptocurrency() {
        HashMap<String, Object> crypto = new HashMap<>();
        crypto.put(BASE_ASSET_KEY, assetIndex);
        crypto.put(ASSET_NAME_KEY, assetName);
        crypto.put(QUANTITY_KEY, quantity);
        crypto.put(SYMBOL_KEY, symbol);
        crypto.put(LAST_PRICE_KEY, lastPrice);
        crypto.put(FIRST_PRICE_KEY, getFirstPrice(2));
        crypto.put(TPTOP_INDEX_KEY, tptopIndex);
        crypto.put(CANDLE_GAP_KEY, candleGap);
        crypto.put(PRICE_CHANGE_PERCENT_KEY, priceChangePercent);
        crypto.put(QUOTE_ASSET_KEY, quoteAsset);
        crypto.put(INCOME_PERCENT_KEY, getIncomePercent());
        if(tradingConfig != null)
            crypto.put(MODEL_ID_KEY, tradingConfig.getModelId());
        crypto.put(FIRST_PRICES_SUM_KEY, getFirstPrice());
        crypto.put(FIRST_PRICES_SIZE_KEY, firstPricesSize);
        return crypto;
    }

    /**
     * The {@code TradingConfig} class is useful for trading operation.<br>
     * Represent model to use for a {@link Cryptocurrency} in trading phases. (BUY and SELL)
     * @author Tecknobit N7ghtm4r3
     * **/
    public static final class TradingConfig{

        /**
         * {@code MAX_FAILS_NUMBER} is instance that memorize max fails number key
         * **/
        public static final int MAX_FAILS_NUMBER = 30;

        /**
         * {@code MODEL_ID_KEY} is instance that memorize model id key
         * **/
        public static final String MODEL_ID_KEY = "model_id";

        /**
         * {@code MARKET_PHASE_KEY} is instance that memorize market phase key
         * **/
        public static final String MARKET_PHASE_KEY = "market_phase";

        /**
         * {@code WASTE_RANGE_KEY} is instance that memorize waste range key
         * **/
        public static final String WASTE_RANGE_KEY = "waste_range";

        /**
         * {@code DAIS_GAP_KEY} is instance that memorize days gap key
         * **/
        public static final String DAIS_GAP_KEY = "days_gap";

        /**
         * {@code MIN_GAIN_FOR_ORDER_KEY} is instance that memorize min gain for order key
         * **/
        public static final String MIN_GAIN_FOR_ORDER_KEY = "min_gain_for_order";

        /**
         * {@code MAX_LOSS_KEY} is instance that memorize max loss key
         * **/
        public static final String MAX_LOSS_KEY = "max_loss";

        /**
         * {@code MAX_GAIN_KEY} is instance that memorize max gain key
         * **/
        public static final String MAX_GAIN_KEY = "max_gain";

        /**
         * {@code MODEL_FAILED_KEY} is instance that memorize model failed key
         * **/
        public static final String MODEL_FAILED_KEY = "model_failed";

        /**
         * {@code modelId} is instance that memorize identifier of model of {@link TradingConfig}
         * **/
        private final long modelId;

        /**
         * {@code marketPhase} is instance that memorize market phase when buy a {@link Cryptocurrency}
         * **/
        private final double marketPhase;

        /**
         * {@code wasteRange} is instance that memorize waste range gap to buy and to make forecast for {@link Cryptocurrency}
         * **/
        private final double wasteRange;

        /**
         * {@code daysGap} is instance that memorize days gap to make forecast for {@link Cryptocurrency}
         * **/
        private final int daysGap;

        /**
         * {@code minGainForOrder} is instance that memorize minimum gain to obtain by an order. This is used in sell phase.
         * **/
        private final double minGainForOrder;

        /**
         * {@code maxLoss} is instance that memorize maximum loss for a {@link Cryptocurrency} and is used in buy phase to check <br>
         * if a cryptocurrency is in correct range to be bought, and is used in sell phase to sell when cryptocurrency is dropping.
         * **/
        private final double maxLoss;

        /**
         * {@code maxGain} is instance that memorize maximum gain for a {@link Cryptocurrency} and is used in buy phase to check
         * if a cryptocurrency is in correct range to be bought.
         * **/
        private final double maxGain;

        /**
         * Constructor to init {@link TradingConfig}
         *
         * @param modelId: identifier of model of {@link TradingConfig}
         * @param marketPhase     : market phase when buy a {@link Cryptocurrency}
         * @param wasteRange      : waste range gap to buy and to make forecast for {@link Cryptocurrency}
         * @param daysGap         : days gap to make forecast for {@link Cryptocurrency}
         * @param minGainForOrder : minimum gain to obtain by an order. This is used in sell phase.
         * @param maxLoss         : maximum loss for a {@link Cryptocurrency}
         * @param maxGain         : maximum gain for a {@link Cryptocurrency} in checking phase
         **/
        public TradingConfig(long modelId, double marketPhase, double wasteRange, int daysGap, double minGainForOrder,
                             double maxLoss, double maxGain) {
            this.modelId = modelId;
            this.marketPhase = marketPhase;
            this.wasteRange = wasteRange;
            this.daysGap = daysGap;
            this.minGainForOrder = minGainForOrder;
            this.maxLoss = maxLoss;
            this.maxGain = maxGain;
        }

        public long getModelId() {
            return modelId;
        }

        public double getMarketPhase() {
            return marketPhase;
        }

        /**
         * This method is used get {@link #marketPhase} instance
         * @param decimals: number of decimal digits es. 2
         * @return {@link #marketPhase} formatted as 1.65 or -1.65
         * **/
        public double getMarketPhase(int decimals) {
            return tradingTools.roundValue(marketPhase, decimals);
        }

        /**
         * This method is used to format like a {@link String} object {@link #marketPhase}.<br>
         * Any params required
         * @return {@link #marketPhase} formatted as +1.653% or -1.6563% as {@link String}
         * **/
        public String getTextMarketPhase(){
            return tradingTools.textualizeAssetPercent(marketPhase);
        }

        /**
         * This method is used to format like a {@link String} object {@link #marketPhase}.<br>
         * @param decimals: number of decimal digits es. 2
         * @return {@link #marketPhase} formatted as +1.65% or -1.65%
         * **/
        public String getTextMarketPhase(int decimals){
            return tradingTools.textualizeAssetPercent(marketPhase, decimals);
        }

        public double getWasteRange() {
            return wasteRange;
        }

        /**
         * This method is used get {@link #wasteRange} instance
         * @param decimals: number of decimal digits es. 2
         * @return {@link #wasteRange} formatted as 1.65 or -1.65
         * **/
        public double getWasteRange(int decimals) {
            return tradingTools.roundValue(wasteRange, decimals);
        }

        /**
         * This method is used to format like a {@link String} object {@link #wasteRange}.<br>
         * Any params required
         * @return {@link #wasteRange} formatted as +1.653% or -1.6563% as {@link String}
         * **/
        public String getTextWasteRange(){
            return tradingTools.textualizeAssetPercent(wasteRange);
        }

        /**
         * This method is used to format like a {@link String} object {@link #wasteRange}.<br>
         * @param decimals: number of decimal digits es. 2
         * @return {@link #wasteRange} formatted as +1.65% or -1.65%
         * **/
        public String getTextWasteRange(int decimals){
            return tradingTools.textualizeAssetPercent(wasteRange, decimals);
        }

        public int getDaysGap() {
            return daysGap;
        }

        public double getMinGainForOrder() {
            return minGainForOrder;
        }

        /**
         * This method is used get {@link #minGainForOrder} instance
         * @param decimals: number of decimal digits es. 2
         * @return {@link #minGainForOrder} formatted as 1.65 or -1.65
         * **/
        public double getMinGainForOrder(int decimals) {
            return tradingTools.roundValue(minGainForOrder, decimals);
        }

        /**
         * This method is used to format like a {@link String} object {@link #minGainForOrder}.<br>
         * Any params required
         * @return {@link #minGainForOrder} formatted as +1.653% or -1.6563% as {@link String}
         * **/
        public String getTextMinGainForOrder(){
            return tradingTools.textualizeAssetPercent(minGainForOrder);
        }

        /**
         * This method is used to format like a {@link String} object {@link #minGainForOrder}.<br>
         * @param decimals: number of decimal digits es. 2
         * @return {@link #minGainForOrder} formatted as +1.65% or -1.65%
         * **/
        public String getTextMinGainForOrder(int decimals){
            return tradingTools.textualizeAssetPercent(minGainForOrder, decimals);
        }

        public double getMaxLoss() {
            return maxLoss;
        }

        /**
         * This method is used get {@link #maxLoss} instance
         * @param decimals: number of decimal digits es. 2
         * @return {@link #maxLoss} formatted as 1.65 or -1.65
         * **/
        public double getMaxLoss(int decimals) {
            return tradingTools.roundValue(maxLoss, decimals);
        }

        /**
         * This method is used to format like a {@link String} object {@link #maxLoss}.<br>
         * Any params required
         * @return {@link #maxLoss} formatted as +1.653% or -1.6563% as {@link String}
         * **/
        public String getTextMaxLoss(){
            return tradingTools.textualizeAssetPercent(maxLoss);
        }

        /**
         * This method is used to format like a {@link String} object {@link #maxLoss}.<br>
         * @param decimals: number of decimal digits es. 2
         * @return {@link #maxLoss} formatted as +1.65% or -1.65%
         * **/
        public String getTextMaxLoss(int decimals){
            return tradingTools.textualizeAssetPercent(maxLoss, decimals);
        }

        public double getMaxGain() {
            return maxGain;
        }

        /**
         * This method is used get {@link #maxGain} instance
         * @param decimals: number of decimal digits es. 2
         * @return {@link #maxGain} formatted as 1.65 or -1.65
         * **/
        public double getMaxGain(int decimals) {
            return tradingTools.roundValue(maxGain, decimals);
        }

        /**
         * This method is used to format like a {@link String} object {@link #maxGain}.<br>
         * Any params required
         * @return {@link #maxGain} formatted as +1.653% or -1.6563% as {@link String}
         * **/
        public String getTextMaxGain(){
            return tradingTools.textualizeAssetPercent(maxGain);
        }

        /**
         * This method is used to format like a {@link String} object {@link #maxGain}.<br>
         * @param decimals: number of decimal digits es. 2
         * @return {@link #maxGain} formatted as +1.65% or -1.65%
         * **/
        public String getTextMaxGain(int decimals){
            return tradingTools.textualizeAssetPercent(maxGain, decimals);
        }

        /**
         * This method is used to get trading config details <br>
         * Any params required
         * @return trading config details as {@link HashMap} of {@link Object}
         * **/
        public HashMap<String, Object> getTradingConfig() {
            HashMap<String, Object> config = new HashMap<>();
            config.put(MODEL_ID_KEY, modelId);
            config.put(MARKET_PHASE_KEY, marketPhase);
            config.put(WASTE_RANGE_KEY, wasteRange);
            config.put(DAIS_GAP_KEY, daysGap);
            config.put(MIN_GAIN_FOR_ORDER_KEY, minGainForOrder);
            config.put(MAX_LOSS_KEY, maxLoss);
            config.put(MAX_GAIN_KEY, maxGain);
            return config;
        }

    }

}

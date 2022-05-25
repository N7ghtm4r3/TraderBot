package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.coinbasemanager.Managers.ExchangePro.Currencies.Records.Currency;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Products.Records.Ticker;
import com.tecknobit.traderbot.Helpers.Orders.MarketOrder;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Cryptocurrency;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Autonomous.Utils.AutoTraderBotAccount;
import com.tecknobit.traderbot.Traders.Interfaces.Native.CoinbaseTraderBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Products.Records.Candle.GRANULARITY_1d;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;

/**
 * The {@code CoinbaseAutoTraderBot} class is trader for {@link CoinbaseTraderBot} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Coinbase's account autonomously.<br>
 * Is derived class of {@code CoinbaseTraderBot} class from inherit all core routines methods and instances.
 * @implNote for autonomous operations use {@link AutoTraderCoreRoutines} and {@link MarketOrder} routines.
 * @author Tecknobit N7ghtm4r3
 * **/

public class CoinbaseAutoTraderBot extends CoinbaseTraderBot implements AutoTraderCoreRoutines, MarketOrder {

    /**
     * {@code AutoTraderBotAccount} is instance that memorize and manage account information and trading reports of auto trader
     * account
     * **/
    protected final AutoTraderBotAccount autoTraderBotAccount = new AutoTraderBotAccount();

    /**
     * {@code MIN_NOTIONAL_FILTER} is instance that contains key for {@code MIN_NOTIONAL} filter
     * **/
    public static final String MIN_NOTIONAL_FILTER = "MIN_NOTIONAL";

    /**
     * {@code LOT_SIZE_FILTER} is instance that contains key for {@code LOT_SIZE} filter
     * **/
    public static final String LOT_SIZE_FILTER = "LOT_SIZE";

    /**
     * {@code walletList} is a map that contains wallet list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * @implNote is used in {@link #buyCryptocurrencies()} and {@link #updateWallet()} routines
     * **/
    private final ConcurrentHashMap<String, Cryptocurrency> walletList;

    /**
     * {@code checkingList} is a map that contains checking list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * @implNote is used in {@link #checkCryptocurrencies()} routine
     * **/
    private final HashMap<String, Cryptocurrency> checkingList;

    /**
     * {@code tradingConfig} is instance that memorize model of trading to use for trading routines
     * **/
    private TradingConfig tradingConfig;

    /**
     * {@code sendStatsReport} is instance that memorize flag to insert to send or not reports
     * **/
    private boolean sendStatsReport;

    /**
     * {@code printRoutineMessages} is instance that memorize flag to insert to print or not routine messages
     * **/
    private boolean printRoutineMessages;

    /**
     * {@code runningBot} is instance that memorize flag that indicates if the bot is running
     * **/
    private boolean runningBot;

    /**
     * {@code previousChecking} is instance that memorize previous timestamp when {@link #checkCryptocurrencies()} is called
     * **/
    private long previousChecking;

    /**
     * {@code previousBuying} is instance that memorize previous timestamp when {@link #buyCryptocurrencies()} is called
     * **/
    private long previousBuying;

    /**
     * {@code previousUpdating} is instance that memorize previous timestamp when {@link #updateWallet()} is called
     * **/
    private long previousUpdating;

    /**
     * {@code baseCurrency} is instance that memorize base currency to get all amount value of traders routine es. EUR
     * **/
    private String baseCurrency;

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, ArrayList<String> quoteCurrencies, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                                 ArrayList<String> quoteCurrencies, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 ArrayList<String> quoteCurrencies, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, int refreshPricesTime, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout, int refreshPricesTime,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 short refreshPricesTime, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, short refreshPricesTime,
                                 boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, ArrayList<String> quoteCurrencies, int refreshPricesTime, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                                 ArrayList<String> quoteCurrencies, int refreshPricesTime, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 ArrayList<String> quoteCurrencies, int refreshPricesTime, boolean sendStatsReport,
                                 boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public CoinbaseAutoTraderBot(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                 int refreshPricesTime, boolean sendStatsReport, boolean printRoutineMessages,
                                 String baseCurrency) throws Exception {
        super(apiKey, apiSecret, passphrase, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    @Override
    public void start() {
        tradingConfig = fetchTradingConfig();
        previousBuying = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        while (runningBot){
                            if(makeRoutine(previousChecking, CHECKING_GAP_TIME)){
                                previousChecking = System.currentTimeMillis();
                                checkCryptocurrencies();
                            }
                            if(makeRoutine(previousBuying, BUYING_GAP_TIME)){
                                previousBuying = System.currentTimeMillis();
                                buyCryptocurrencies();
                            }
                            if(makeRoutine(previousUpdating, UPDATING_GAP_TIME)){
                                previousUpdating = System.currentTimeMillis();
                                updateWallet();
                            }
                        }
                        System.out.println("Bot is stopped, waiting for reactivation");
                        Thread.sleep(5000);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void checkCryptocurrencies() throws Exception {
        System.out.println("## CHECKING NEW CRYPTOCURRENCIES");
        tradingConfig = fetchTradingConfig();
        int daysGap = tradingConfig.getDaysGap();
        for (Ticker ticker : coinbaseProductsManager.getAllTickersList()){
            String symbol = ticker.getProductId();
            String quoteAsset = ticker.getQuoteAsset();
            if(quoteCurrencies.isEmpty() || quoteContained(quoteAsset)){
                String baseAsset = ticker.getBaseAsset();
                Coin coin = coins.get(baseAsset);
                if(coin != null && !walletList.containsKey(baseAsset)){
                    double lastPrice = ticker.getPrice();
                    double priceChangePercent = 0;
                    Cryptocurrency cryptocurrency = checkingList.get(baseAsset);
                    if(cryptocurrency != null) {
                        priceChangePercent = coinbaseProductsManager.getTrendPercent(cryptocurrency.getLastPrice(),
                                lastPrice, 8);
                    }
                    double tptop = isTradable(symbol, tradingConfig, GRANULARITY_1d, priceChangePercent);
                    if(tptop != ASSET_NOT_TRADABLE){
                        checkingList.put(baseAsset, new Cryptocurrency(baseAsset,
                                coin.getAssetName(),
                                0,
                                symbol,
                                lastPrice,
                                tptop,
                                GRANULARITY_1d,
                                priceChangePercent,
                                quoteAsset,
                                tradingConfig
                        ));
                    }else
                        checkingList.remove(baseAsset);
                }
            }
        }
    }

    @Override
    public double isTradable(String symbol, TradingConfig tradingConfig, Object candleInterval,
                             double priceChangePercent) throws Exception {
        double wasteRange = tradingConfig.getWasteRange();
        if((abs(priceChangePercent - tradingConfig.getMarketPhase()) <= wasteRange) &&
                ((priceChangePercent >= tradingConfig.getMaxLoss()) && (priceChangePercent <= tradingConfig.getMaxGain()))){
            double tptop = computeTPTOPIndex(symbol, tradingConfig, candleInterval, wasteRange);
            if(tptop >= tradingConfig.getMinGainForOrder())
                return tptop;
        }
        return ASSET_NOT_TRADABLE;
    }

    @Override
    public double computeTPTOPIndex(String symbol, TradingConfig tradingConfig, Object candleInterval,
                                    double wasteRange) throws Exception {
        return coinbaseProductsManager.getSymbolForecast(symbol, tradingConfig.getDaysGap(), (Integer) candleInterval,
                (int) tradingConfig.getWasteRange());
    }

    @Override
    public void buyCryptocurrencies() throws Exception {
        System.out.println("## BUYING NEW CRYPTOCURRENCIES");
        for (Cryptocurrency cryptocurrency : checkingList.values()){
            String symbol = cryptocurrency.getSymbol();
            double quantity = getMarketOrderQuantity(cryptocurrency);
            if(quantity != -1) {
                try {
                    buyMarket(symbol, quantity);
                    cryptocurrency.setQuantity(quantity);
                    cryptocurrency.setFirstPrice(cryptocurrency.getLastPrice());
                    walletList.put(cryptocurrency.getAssetIndex(), cryptocurrency);
                    if(printRoutineMessages)
                        System.out.println("Buying [" + symbol + "], quantity: " + quantity);
                }catch (Exception e){
                    printError(symbol, e);
                }
            }
        }
        checkingList.clear();
        if(printRoutineMessages)
            for (Transaction transaction : getAllTransactions(true))
                transaction.printDetails();
    }

    @Override
    public void updateWallet() throws Exception {
        System.out.println("## UPDATING WALLET CRYPTOCURRENCIES");
    }

    @Override
    public void incrementSellsSale(Cryptocurrency cryptocurrency, int codeOpe) throws Exception {

    }

    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        super.buyMarket(symbol, quantity);
        if(sendStatsReport)
            sendStatsReport(/*params*/);
    }

    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        super.sellMarket(symbol, quantity);
        if(sendStatsReport)
            sendStatsReport(/*params*/);
    }

    @Override
    public void setSendStatsReport(boolean sendStatsReport) {
        this.sendStatsReport = sendStatsReport;
    }

    @Override
    public boolean canSendStatsReport() {
        return sendStatsReport;
    }

    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        this.printRoutineMessages = printRoutineMessages;
    }

    @Override
    public boolean canPrintRoutineMessages() {
        return printRoutineMessages;
    }

    @Override
    public boolean isRunningBot() {
        return runningBot;
    }

    @Override
    public void disableBot() {
        runningBot = false;
    }

    @Override
    public void enableBot() {
        runningBot = true;
    }

    @Override
    public double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception {
        double coinBalance = getCoinBalance(cryptocurrency.getQuoteAsset());
        String assetIndex = cryptocurrency.getAssetIndex();
        Currency currency = coinbaseCurrenciesManager.getCurrencyObject(assetIndex);
        double quantity = coinbaseProductsManager.roundValue(coinBalance * cryptocurrency.getTptopIndex() / 100, 6);
        if(quantity >= currency.getMinSize()) {
            if(quantity % currency.getMaxPrecision() != 0)
                quantity = ceil(quantity);
        }else
            quantity = -1;
        return quantity;
    }

    // TODO: 25/05/2022 INSERT RIGHT ROUTINE METHOD
    @Override
    public double getCoinBalance(String quote) {
        return 100;
        /*Coin coin = coins.get(quote);
        return coinbaseAccountManager.roundValue(coin.getQuantity() *
                lastPrices.get(coin.getAssetIndex() + "-" + USD_CURRENCY), 8);*/
    }

    @Override
    public double getSellsAtLoss() {
        return autoTraderBotAccount.getSellsAtLoss();
    }

    @Override
    public double getSellsAtGain() {
        return autoTraderBotAccount.getSellsAtGain();
    }

    @Override
    public double getSellsInPair() {
        return autoTraderBotAccount.getSellsAtPair();
    }

    @Override
    public double getTotalSells() {
        return autoTraderBotAccount.getTotalSells();
    }

    @Override
    public void setBaseCurrency(String baseCurrency) {
        if(baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USDT");
        this.baseCurrency = baseCurrency;
    }

    @Override
    public String getBaseCurrency() {
        return baseCurrency;
    }

}

package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.traderbot.Helpers.Orders.MarketOrder;
import com.tecknobit.traderbot.Records.Cryptocurrency;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Autonomous.Utils.AutoTraderBotAccount;
import com.tecknobit.traderbot.Traders.Interfaces.Native.CoinbaseTraderBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

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
    public double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception {
        return 0;
    }

    @Override
    public void start() {

    }

    @Override
    public void checkCryptocurrencies() throws Exception {

    }

    @Override
    public void buyCryptocurrencies() throws Exception {

    }

    @Override
    public double isTradable(String symbol, TradingConfig tradingConfig, Object candleInterval, double lastPrice,
                             double priceChangePercent) throws IOException {
        return 0;
    }

    @Override
    public double computeTPTOPIndex(String symbol, TradingConfig tradingConfig, Object candleInterval,
                                    double wasteRange) throws IOException {
        return 0;
    }

    @Override
    public void updateWallet() throws Exception {

    }

    @Override
    public void incrementSellsSale(Cryptocurrency cryptocurrency, int codeOpe) throws Exception {

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
    public double getCoinBalance(double lastPrice, String quote) {
        return 0;
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

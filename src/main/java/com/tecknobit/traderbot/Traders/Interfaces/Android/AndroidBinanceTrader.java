package com.tecknobit.traderbot.Traders.Interfaces.Android;

import com.tecknobit.apimanager.Tools.Trading.CryptocurrencyTool;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Account.TraderDetails;
import com.tecknobit.traderbot.Records.Portfolio.*;
import com.tecknobit.traderbot.Routines.Android.AndroidCoreRoutines;
import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow;
import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow.Credentials;
import com.tecknobit.traderbot.Routines.Android.ServerRequest;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.SELL;
import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.*;
import static com.tecknobit.traderbot.Records.Portfolio.Token.BASE_ASSET_KEY;
import static java.lang.Math.toIntExact;
import static java.lang.System.currentTimeMillis;
import static java.text.DateFormat.getDateTimeInstance;

public class AndroidBinanceTrader extends BinanceTraderBot implements AndroidCoreRoutines {

    private final DateFormat transactionDateFormat;

    /**
     * {@code cryptocurrencyTool} is instance helpful to manage cryptocurrencies details
     * **/
    private final CryptocurrencyTool cryptocurrencyTool;
    private final TraderDetails traderDetails;
    private final TraderAccount traderAccount;
    private final AndroidWorkflow androidWorkflow;
    private final String authToken;
    private final String token;
    private final String ivSpec;
    private final String secretKey;
    private String symbol;
    private String side;

    /**
     * {@code walletList} is a map that contains wallet list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * **/
    private final ConcurrentHashMap<String, Cryptocurrency> walletList;

    /**
     * {@code runningTrader} is instance that memorize flag that indicates if the trader is running
     * **/
    private boolean runningTrader;

    /**
     * {@code baseCurrency} is instance that memorize base currency to get all amount value of traders routine es. EUR
     * **/
    private String baseCurrency;

    public AndroidBinanceTrader(String apiKey, String secretKey, Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, Credentials credentials,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        checkCredentialsValidity(credentials);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        initCredentials(credentials);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    @Override
    protected void initTrader() throws Exception {
        printAndroidDisclaimer();
        super.initTrader();
    }

    @Override
    public void initCredentials(Credentials credentials) throws Exception {
        checkCredentialsValidity(credentials);
        if(credentials.getToken() == null) {
            credentials.setTraderDetails(traderDetails);
            credentials.sendRegistrationRequest();
        }
    }

    @Override
    public void workflowHandler() {
        runningTrader = true;
        refreshWalletList();
        androidWorkflow.startWorkflow();
    }

    @Override
    public void refreshWalletList() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                JSONArray wallet = new JSONArray();
                while (true){
                    try {
                        if(isRefreshTime())
                            refreshLatestPrice();
                        for (Cryptocurrency cryptocurrency : walletList.values()){
                            String assetIndex = cryptocurrency.getAssetIndex();
                            TickerPriceChange ticker = lastPrices.get(assetIndex + USDT_CURRENCY);
                            double lastPrice = ticker.getLastPrice();
                            double priceChangePercent = ticker.getPriceChangePercent();
                            cryptocurrency.setLastPrice(lastPrice);
                            cryptocurrency.setPriceChangePercent(priceChangePercent);
                            walletList.put(assetIndex, cryptocurrency);
                            wallet.put(new JSONObject().put(BASE_ASSET_KEY, assetIndex)
                                    .put(LAST_PRICE_KEY, lastPrice)
                                    .put(PRICE_CHANGE_PERCENT_KEY, priceChangePercent)
                                    .put(INCOME_PERCENT_KEY, cryptocurrency.getIncomePercent(2)));
                        }
                        androidWorkflow.insertRefreshedPrices(wallet);
                        wallet.clear();
                        sleep(10000);
                    }catch (Exception e){
                        e.printStackTrace();
                        printRed("Error during refreshing wallet list");
                    }
                }
            }
        }.start();
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
        if(runningTrader) {
            balance = super.getWalletBalance(currency, forceRefresh);
            androidWorkflow.insertWalletBalance(binanceMarketManager.roundValue(balance, 2));
            return balance;
        }
        return -1;
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        if(runningTrader)
            return super.getWalletBalance(currency, forceRefresh, decimals);
        return -1;
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAssetsList(currency, forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(dateFormat, forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, dateFormat, forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, forceRefresh);
        return null;
    }

    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(runningTrader){
            super.buyMarket(symbol, quantity);
            this.symbol = symbol;
        }
    }

    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if(runningTrader){
            super.sellMarket(symbol, quantity);
            this.symbol = symbol;
        }
    }

    @Override
    protected void placeAnOrder(String symbol, double quantity, String side) throws Exception {
        super.placeAnOrder(symbol, quantity, side);
        this.side = side;
    }

    @Override
    protected void insertCoin(String index, String name, double quantity) {
        super.insertCoin(index, name, quantity);
        Coin coin = coins.get(index);
        Cryptocurrency cryptocurrency;
        TickerPriceChange ticker = lastPrices.get(symbol);
        String quoteAsset = tradingPairsList.get(symbol).getQuoteAsset();
        double lastPrice = ticker.getLastPrice();
        int sales = 0;
        Transaction transaction = new Transaction(symbol, side, transactionDateFormat.format(new Date(currentTimeMillis())),
                binanceMarketManager.roundValue(quantity * lastPrice, 2), quantity, quoteAsset, index);
        if(side.equals(SELL)){
            cryptocurrency = walletList.get(index);
            double income = cryptocurrency.getIncomePercent();
            String sellCode = getTypeSellCode(income);
            traderAccount.addIncome(income);
            transaction.setIncomePercent(cryptocurrency.getIncomePercent(2));
            transaction.setTransactionType(sellCode);
            switch (sellCode){
                case LOSS_SELL:
                    traderAccount.addLoss();
                    sales = traderAccount.getSalesAtLoss();
                    break;
                case GAIN_SELL:
                    traderAccount.addGain();
                    sales = traderAccount.getSalesAtGain();
                    break;
                default:
                    traderAccount.addPair();
                    sales = traderAccount.getSalesAtPair();
            }
        }else{
            cryptocurrency = walletList.get(index);
            if(cryptocurrency == null) {
                cryptocurrency = new Cryptocurrency(index, cryptocurrencyTool.getCryptocurrencyName(index), quantity,
                    symbol, lastPrice, -1 , null, ticker.getPriceChangePercent(), quoteAsset, null);
                cryptocurrency.addFirstPrice(lastPrice);
            }else {
                cryptocurrency.setQuantity(coin.getQuantity());
                cryptocurrency.addFirstPrice(lastPrice);
                cryptocurrency.setIncomePercent(cryptocurrency.getIncomePercent());
            }
            walletList.put(index, cryptocurrency);
        }
        if(coin.isTradingEnabled())
            androidWorkflow.insertCryptocurrency(cryptocurrency, transaction, sales, traderAccount.getTotalIncome(2));
        else {
            androidWorkflow.removeCryptocurrency(index, transaction);
            walletList.remove(index);
        }
    }

    @Override
    public void setRefreshPricesTime(int refreshPricesTime) {
        super.setRefreshPricesTime(refreshPricesTime);
        traderDetails.setRefreshPricesTime(refreshPricesTime);
    }

    @Override
    public boolean isTraderRunning() {
        return runningTrader;
    }

    @Override
    public void disableTrader() {
        runningTrader = false;
        traderDetails.setTraderStatus(STOPPED_TRADER_STATUS);
    }

    @Override
    public void enableTrader() {
        runningTrader = true;
        traderDetails.setTraderStatus(RUNNING_TRADER_STATUS);
    }

    @Override
    public double getSalesAtLoss() {
        if(runningTrader)
            return traderAccount.getSalesAtLoss();
        return -1;
    }

    @Override
    public double getSalesAtGain() {
        if(runningTrader)
            return traderAccount.getSalesAtGain();
        return -1;
    }

    @Override
    public double getSalesInPair() {
        if(runningTrader)
            return traderAccount.getSalesAtPair();
        return -1;
    }

    @Override
    public double getTotalSales() {
        if(runningTrader)
            return traderAccount.getTotalSales();
        return -1;
    }

    @Override
    public void setBaseCurrency(String baseCurrency) {
        if(baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USDT");
        this.baseCurrency = baseCurrency;
    }

    @Override
    public String getBaseCurrency() {
        if(runningTrader)
            return baseCurrency;
        return null;
    }

    /**
     * This method is used to get credentials inserted for trader login
     * @return trader credentials as {@link Credentials} object
     * **/
    @Override
    public Credentials getCredentials() {
        return androidWorkflow.getCredentials();
    }

    /**
     * This method is used to set flag to print routine messages
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        androidWorkflow.setPrintRoutineMessages(printRoutineMessages);
    }

    /**
     * This method is used to get flag to print or not routine messages
     * @return flag that indicates the possibility or not to print or not routine messages
     * **/
    @Override
    public boolean canPrintRoutineMessages() {
        return androidWorkflow.canPrintRoutineMessages();
    }

}

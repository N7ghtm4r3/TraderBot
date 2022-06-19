package com.tecknobit.traderbot.Traders.Interfaces.Android;

import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Account.TraderDetails;
import com.tecknobit.traderbot.Records.Portfolio.Asset;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.Android.AndroidCoreRoutines;
import com.tecknobit.traderbot.Routines.Android.ServerRequest;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;

import java.util.ArrayList;

import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static java.lang.Math.toIntExact;

public class AndroidBinanceTrader extends BinanceTraderBot implements AndroidCoreRoutines {

    private TraderDetails traderDetails;
    private final TraderAccount traderAccount;
    private final Credentials credentials;
    private final String authToken;
    private String mail;
    private String password;
    private final String token;
    private final String ivSpec;
    private final String secretKey;
    private ServerRequest serverRequest;

    /**
     * {@code runningTrader} is instance that memorize flag that indicates if the trader is running
     * **/
    private boolean runningTrader;

    /**
     * {@code baseCurrency} is instance that memorize base currency to get all amount value of traders routine es. EUR
     * **/
    private String baseCurrency;

    public AndroidBinanceTrader(String apiKey, String secretKey, Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey);
        this.credentials = credentials;
        this.baseCurrency = baseCurrency;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint,
                                Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.credentials = credentials;
        this.baseCurrency = baseCurrency;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, int refreshPricesTime,
                                Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        checkCredentialsValidity(credentials);
        this.credentials = credentials;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime,
                                Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.credentials = credentials;
        this.baseCurrency = baseCurrency;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.credentials = credentials;
        this.baseCurrency = baseCurrency;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.credentials = credentials;
        this.baseCurrency = baseCurrency;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.credentials = credentials;
        this.baseCurrency = baseCurrency;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                Credentials credentials, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.credentials = credentials;
        this.baseCurrency = baseCurrency;
        initCredentials();
        authToken = credentials.getAuthToken();
        mail = credentials.getMail();
        password = credentials.getPassword();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
    }

    @Override
    protected void initTrader() throws Exception {
        printAndroidDisclaimer();
        super.initTrader();
    }

    @Override
    public void initCredentials() throws Exception {
        checkCredentialsValidity(credentials);
        if(credentials.getToken() == null) {
            long timestamp = System.currentTimeMillis();
            traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                    toIntExact(REFRESH_PRICES_TIME), timestamp);
            credentials.setTraderDetails(traderDetails);
            credentials.sendRegistrationRequest();
        }
        workflowHandler();
    }

    @Override
    public void workflowHandler() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        /*while (runningTrader){
                            getRoutines(AndroidBinanceTrader.this, new ServerRequest(ivSpec, secretKey,
                                    authToken, token));
                            sleep(2000);
                        }*/
                        getRoutines(AndroidBinanceTrader.this, new ServerRequest(ivSpec, secretKey,
                                authToken, token));
                        sleep(2000);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
        return super.getWalletBalance(currency, forceRefresh);
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        return super.getWalletBalance(currency, forceRefresh, decimals);
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        return super.getAssetsList(currency, forceRefresh);
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        return super.getAllTransactions(dateFormat, forceRefresh);
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        return super.getAllTransactions(forceRefresh);
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        return super.getTransactionsList(quoteCurrency, dateFormat, forceRefresh);
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        return super.getTransactionsList(quoteCurrency, forceRefresh);
    }

    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        super.buyMarket(symbol, quantity);
    }

    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        super.sellMarket(symbol, quantity);
    }

    @Override
    protected void insertCoin(String index, String name, double quantity) {
        super.insertCoin(index, name, quantity);
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
        return traderAccount.getSalesAtLoss();
    }

    @Override
    public double getSalesAtGain() {
        return traderAccount.getSalesAtGain();
    }

    @Override
    public double getSalesInPair() {
        return traderAccount.getSalesAtPair();
    }

    @Override
    public double getTotalSales() {
        return traderAccount.getTotalSales();
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

    public Credentials getCredentials() {
        return credentials;
    }

}

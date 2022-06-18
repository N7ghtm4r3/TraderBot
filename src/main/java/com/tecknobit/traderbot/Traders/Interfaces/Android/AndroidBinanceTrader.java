package com.tecknobit.traderbot.Traders.Interfaces.Android;

import com.tecknobit.traderbot.Records.Account.TraderDetails;
import com.tecknobit.traderbot.Records.Portfolio.Asset;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.Android.AndroidCoreRoutines;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;

import java.util.ArrayList;

import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static java.lang.Math.toIntExact;

public class AndroidBinanceTrader extends BinanceTraderBot implements AndroidCoreRoutines {

    private TraderDetails traderDetails;
    private final Credentials credentials;

    public AndroidBinanceTrader(String apiKey, String secretKey, Credentials credentials) throws Exception {
        super(apiKey, secretKey);
        this.credentials = credentials;
        initCredentials();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint,
                                Credentials credentials) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.credentials = credentials;
        initCredentials();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, int refreshPricesTime,
                                Credentials credentials) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        checkCredentialsValidity(credentials);
        this.credentials = credentials;
        initCredentials();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime,
                                Credentials credentials) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.credentials = credentials;
        initCredentials();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                Credentials credentials) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.credentials = credentials;
        initCredentials();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, Credentials credentials) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.credentials = credentials;
        initCredentials();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                Credentials credentials) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.credentials = credentials;
        initCredentials();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                Credentials credentials) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.credentials = credentials;
        initCredentials();
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

    public Credentials getCredentials() {
        return credentials;
    }

}

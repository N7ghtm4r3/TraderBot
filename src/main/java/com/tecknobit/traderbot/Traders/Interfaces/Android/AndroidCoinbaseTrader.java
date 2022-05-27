package com.tecknobit.traderbot.Traders.Interfaces.Android;

import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Traders.Interfaces.Native.CoinbaseTraderBot;

import java.util.ArrayList;

public class AndroidCoinbaseTrader extends CoinbaseTraderBot {

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, int timeout) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase) throws Exception {
        super(apiKey, apiSecret, passphrase);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, quoteCurrencies);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, int timeout,
                                 ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, quoteCurrencies);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, quoteCurrencies);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, apiSecret, passphrase, quoteCurrencies);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, refreshPricesTime);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, int timeout, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, refreshPricesTime);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 short refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, refreshPricesTime);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, short refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, refreshPricesTime);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 int timeout, ArrayList<String> quoteCurrencies, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, quoteCurrencies, refreshPricesTime);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, int timeout,
                                 ArrayList<String> quoteCurrencies, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, quoteCurrencies, refreshPricesTime);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                 ArrayList<String> quoteCurrencies, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, quoteCurrencies, refreshPricesTime);
    }

    public AndroidCoinbaseTrader(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                 int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, quoteCurrencies, refreshPricesTime);
    }

    @Override
    protected void initTrader() throws Exception {
        super.initTrader();
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) {
        return super.getWalletBalance(currency, forceRefresh);
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) {
        return super.getWalletBalance(currency, forceRefresh, decimals);
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) {
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

}

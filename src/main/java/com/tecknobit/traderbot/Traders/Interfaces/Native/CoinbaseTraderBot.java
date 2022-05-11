package com.tecknobit.traderbot.Traders.Interfaces.Native;

import com.tecknobit.coinbasemanager.Managers.ExchangePro.Account.CoinbaseAccountManager;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;

import java.util.ArrayList;
import java.util.HashMap;

public class CoinbaseTraderBot extends TraderCoreRoutines {

    private final CoinbaseAccountManager coinbaseAccountManager;

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage, int timeout) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage,timeout);
    }
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage, int timeout,
                             ArrayList<String> quoteCurrencies) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage,timeout);
    }
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                             ArrayList<String> quoteCurrencies) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             ArrayList<String> quoteCurrencies) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase,
                             ArrayList<String> quoteCurrencies) {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
    }

    @Override
    protected void initTrader() throws Exception {
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        if(quoteCurrencies == null)
            quoteCurrencies = new ArrayList<>();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
        System.out.println(coinbaseAccountManager.getCoinbaseWallets());
        return 0;
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        return 0;
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        return null;
    }

    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {

    }

    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {

    }

    @Override
    public void placeAnOrder(String symbol, double quantity, String side) throws Exception {

    }

    @Override
    public  <T> T getOrderStatus(FormatResponseType formatResponseType) {
        return super.getOrderStatus(formatResponseType);
    }

    @Override
    protected String getErrorResponse() {
        return null;
    }

    @Override
    protected void refreshLatestPrice() throws Exception {

    }

    @Override
    public void setQuoteCurrencies(ArrayList<String> quoteCurrencies) {
        super.setQuoteCurrencies(quoteCurrencies);
    }

    @Override
    public void setRefreshPricesTime(int refreshPricesTime) {
        super.setRefreshPricesTime(refreshPricesTime);
    }

    @Override
    public void insertQuoteCurrency(String newQuote) {
        super.insertQuoteCurrency(newQuote);
    }

    @Override
    public boolean removeQuoteCurrency(String quoteToRemove) {
        return super.removeQuoteCurrency(quoteToRemove);
    }

    @Override
    public ArrayList<String> getQuoteCurrencies() {
        return super.getQuoteCurrencies();
    }

}

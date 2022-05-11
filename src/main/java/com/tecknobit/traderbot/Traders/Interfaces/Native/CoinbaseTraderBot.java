package com.tecknobit.traderbot.Traders.Interfaces.Native;

import com.tecknobit.coinbasemanager.Managers.ExchangePro.Account.CoinbaseAccountManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Account.Records.CoinbaseAccount;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Currencies.CoinbaseCurrenciesManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Products.CoinbaseProductsManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Products.Records.TradingPair;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;

import java.util.ArrayList;
import java.util.HashMap;

public class CoinbaseTraderBot extends TraderCoreRoutines {

    private final CoinbaseAccountManager coinbaseAccountManager;
    private final CoinbaseProductsManager coinbaseProductsManager;
    private final CoinbaseCurrenciesManager coinbaseCurrenciesManager;
    protected HashMap<String, TradingPair> tradingPairsList;

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             int timeout) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        initTrader();
    }
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, timeout);
        initTrader();
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        initTrader();
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase);
        initTrader();
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage, int timeout,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, timeout);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
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
        refreshLatestPrice();
        for (CoinbaseAccount coin : coinbaseAccountManager.getCoinbaseWalletsList()){
            double balance = coin.getBalance();
            if(balance > 0){
                String symbol = coin.getId();
                coins.put(symbol, new Coin(coinbaseCurrenciesManager.getCurrencyObject(coin.getCurrency()).getName(),
                        true,
                        symbol,
                        balance
                ));
            }
        }
        for (TradingPair tradingPair : coinbaseProductsManager.getAllTradingPairsList())
            tradingPairsList.put(tradingPair.getId(), tradingPair);
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
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
        return coinbaseAccountManager.getErrorResponse();
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

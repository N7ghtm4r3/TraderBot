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
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class CoinbaseTraderBot extends TraderCoreRoutines {

    protected static final String COMPARE_CURRENCY = "USD";
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
        for (CoinbaseAccount coin : coinbaseAccountManager.getCoinbaseWalletsList()){
            double balance = coin.getBalance();
            if(balance > 0){
                String symbol = coin.getCurrency();
                coins.put(symbol, new Coin(symbol,
                        coinbaseCurrenciesManager.getCurrencyObject(symbol).getName(),
                        balance,
                        true
                ));
            }
        }
        for (TradingPair tradingPair : coinbaseProductsManager.getAllTradingPairsList())
            tradingPairsList.put(tradingPair.getId(), tradingPair);
        refreshLatestPrice();
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastBalanceCurrency.equals(currency) || forceRefresh){
            refreshLatestPrice();
            lastBalanceCurrency = currency;
            balance = 0;
            for (Coin coin : coins.values())
                if(coin.isTradingEnabled())
                    balance += coin.getQuantity() * lastPrices.get(coin.getAssetIndex());
            if(!lastBalanceCurrency.equals(COMPARE_CURRENCY)){
                try {
                    balance /= coinbaseProductsManager.getProductTickerObject(currency + COMPARE_CURRENCY).getPrice();
                }catch (Exception ignored){
                }
            }
        }
        return balance;
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        return coinbaseProductsManager.roundValue(getWalletBalance(currency, forceRefresh), decimals);
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastAssetCurrency.equals(currency) || forceRefresh){
            refreshLatestPrice();
            lastAssetCurrency = currency;
            assets.clear();
            for (Coin coin : coins.values()) {
                String index = coin.getAssetIndex();
                double quantity = coin.getQuantity();
                double balance = quantity * lastPrices.get(index);
                if(!currency.equals(COMPARE_CURRENCY)){
                    try {
                        balance /= coinbaseProductsManager.getProductTickerObject(BinanceTraderBot.COMPARE_CURRENCY
                                + "-" + currency).getPrice();
                    }catch (Exception ignored){
                        
                    }
                }
                assets.add(new Asset(index,
                        coin.getAssetName(),
                        quantity,
                        balance,
                        currency
                ));
            }
        }
        return assets;
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
    public <T> T getOrderStatus(FormatResponseType formatResponseType) {
        return super.getOrderStatus(formatResponseType);
    }

    @Override
    protected String getErrorResponse() {
        return coinbaseAccountManager.getErrorResponse();
    }

    @Override
    protected void refreshLatestPrice() throws Exception {
        if(isRefreshTime()){
            lastPricesRefresh = System.currentTimeMillis();
            for (String productId : tradingPairsList.keySet()) {
                String[] productIds = productId.split("-");
                try {
                    if(productIds[1].equals(COMPARE_CURRENCY))
                        lastPrices.put(productIds[0], coinbaseProductsManager.getProductTickerObject(productId).getPrice());
                }catch (JSONException ignored){
                }
            }
        }
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

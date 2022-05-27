package com.tecknobit.traderbot.Traders.Interfaces.Android;

import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;

import java.util.ArrayList;

public class AndroidBinanceTrader extends BinanceTraderBot {

    public AndroidBinanceTrader(String apiKey, String secretKey) throws Exception {
        super(apiKey, secretKey);
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint,
                                int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint,
                                ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
    }

    @Override
    protected void initTrader() throws Exception {
        super.initTrader();
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

}

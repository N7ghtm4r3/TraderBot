package com.tecknobit.traderbot.Routines;

import com.tecknobit.apimanager.Tools.Trading.TradingTools;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.FormatResponseType.String;
import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.FormatResponseType.*;

public abstract class TraderCoreRoutines {

    public static TradingTools tradingTools = new TradingTools();
    protected static final String USD_CURRENCY = "USD";
    protected static final String USDT_CURRENCY = "USDT";
    protected ArrayList<Transaction> transactions;
    protected ArrayList<Transaction> allTransactions;
    protected HashMap<String, Double> lastPrices;
    protected HashMap<String, Coin> coins;
    protected ArrayList<String> quoteCurrencies;
    protected String lastTransactionCurrency;
    protected ArrayList<Asset> assets;
    protected String lastBalanceCurrency;
    protected String lastAssetCurrency;
    protected long REFRESH_PRICES_TIME;
    protected long lastPricesRefresh;
    protected double balance;
    protected String orderStatus;

    public enum FormatResponseType{
        String,
        JSON,
        CustomObject
    }

    protected abstract void initTrader() throws Exception;

    protected abstract double getWalletBalance(String currency, boolean forceRefresh) throws Exception;

    protected abstract double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception;

    protected abstract ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception;

    protected abstract ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception;

    protected abstract ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception;

    protected abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception;

    protected abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat,
                                                                  boolean forceRefresh) throws Exception;

    protected abstract void buyMarket(String symbol, double quantity) throws Exception;

    protected abstract void sellMarket(String symbol, double quantity) throws Exception;

    protected abstract void placeAnOrder(String symbol, double quantity, String side) throws Exception;

    protected <T> T getOrderStatus(FormatResponseType formatResponseType) {
        if(formatResponseType.equals(String) || formatResponseType.equals(CustomObject)){
            return (T) orderStatus;
        }else if(formatResponseType.equals(JSON)){
            try {
                return (T) new JSONObject(orderStatus);
            }catch (JSONException e){
                try {
                    return (T) new JSONArray(orderStatus);
                }catch (JSONException requestError){
                    return null;
                }
            }

        }
        return null;
    }

    protected abstract String getErrorResponse();

    protected abstract void refreshLatestPrice() throws Exception;

    protected void setRefreshPricesTime(int refreshPricesTime) {
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
    }

    protected void setQuoteCurrencies(ArrayList<String> quoteCurrencies) {
        this.quoteCurrencies = quoteCurrencies;
    }

    protected void insertQuoteCurrency(String newQuote){
        if(!quoteCurrencies.contains(newQuote))
            quoteCurrencies.add(newQuote);
    }

    protected boolean removeQuoteCurrency(String quoteToRemove){
        return quoteCurrencies.remove(quoteToRemove);
    }

    protected ArrayList<String> getQuoteCurrencies() {
        return quoteCurrencies;
    }

    protected boolean isRefreshTime(){
        return (System.currentTimeMillis() - lastPricesRefresh) >= REFRESH_PRICES_TIME;
    }

    protected abstract void insertCoin(String symbol, String name, double quantity) throws Exception;

}

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

/**
 * The {@code TraderCoreRoutines} class defines base routines methods for all traders. <br>
 * These routines allow the different traders to do some operation about wallet info and make trading operations.
 * @author Tecknobit N7ghtm4r3
 * **/

public abstract class TraderCoreRoutines {

    /**
     * {@code TradingTools} is an instance to use everywhere also out from traders
     * **/
    public static TradingTools tradingTools = new TradingTools();

    /**
     * {@code USD_CURRENCY} is the identifier of USD currency used by traders to get default prices
     * **/
    protected static final String USD_CURRENCY = "USD";

    /**
     * {@code USDT_CURRENCY} is the identifier of USDT cryptocurrency used by traders to get default prices
     * **/
    protected static final String USDT_CURRENCY = "USDT";

    /**
     * {@code transactions} is a list of transactions made by user account plus transactions made by a
     * trader on a single symbol.
     * @implNote every time that is call {@link #getTransactionsList(java.lang.String, boolean)}
     * or {@link #getTransactionsList(java.lang.String, java.lang.String, boolean)} {@code transactions}
     * instance will be clear only if currency used is different from previous time or is time to refresh different
     * data or user force refresh of this list. <br>
     * This list is {@link ArrayList} of custom object {@link Transaction} give by {@code TraderBot} library.
     * **/
    protected ArrayList<Transaction> transactions;

    /**
     * {@code allTransactions} is a list of all transactions made by user account plus transactions made by a
     * trader.
     * @implNote every time that is call {@link #getAllTransactions(boolean)} <br>
     * or {@link #getTransactionsList(java.lang.String, boolean)} {@code allTransactions}
     * instance will be clear only if currency used is different from previous time or is time to refresh different
     * data or user force refresh of this list. <br>
     * This list is {@link ArrayList} of custom object {@link Transaction} give by {@code TraderBot} library.
     * **/
    protected ArrayList<Transaction> allTransactions;

    /**
     * {@code lastPrices} is a map that contains asset index (es. BTC) as key {@link String} and last price in
     * {@link #USD_CURRENCY} or {@link #USDT_CURRENCY} of that cryptocurrency as {@link Double}.
     * @apiNote prices inserted in this map are only prices of coins inserted in {@link #coins} list
     * @implNote refresh of last prices, by default, is every 10 seconds, but you can set programmatically
     * {@link #REFRESH_PRICES_TIME} to customize refresh time.
     * **/
    protected HashMap<String, Double> lastPrices;

    /**
     * {@code coins} is a map that contains assets of base user wallet and all assets that a trader will trade
     * during his operation.
     * @implSpec this map has as key the asset index (es. BTC) as {@link String} and has as value custom object
     * {@link Coin} give by {@code TraderBot} library.
     * **/
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

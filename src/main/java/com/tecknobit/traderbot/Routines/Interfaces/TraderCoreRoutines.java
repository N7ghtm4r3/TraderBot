package com.tecknobit.traderbot.Routines.Interfaces;

import com.tecknobit.apimanager.Tools.Trading.TradingTools;
import com.tecknobit.traderbot.Records.Portfolio.Asset;
import com.tecknobit.traderbot.Records.Portfolio.Coin;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.traderbot.Routines.Interfaces.TraderCoreRoutines.FormatResponseType.String;
import static com.tecknobit.traderbot.Routines.Interfaces.TraderCoreRoutines.FormatResponseType.*;

/**
 * The {@code TraderCoreRoutines} class defines base routines methods for all traders. <br>
 * These routines allow the different traders to do some operations about wallet info and make trading operations.
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
     * {@code coins} is a map that contains assets of base user wallet and all assets that a trader will trade
     * during his operation.
     * @implSpec this map has as key the asset index (es. BTC) as {@link String} and has as value custom object
     * {@link Coin} give by {@code TraderBot} library.
     * **/
    protected HashMap<String, Coin> coins;

    /**
     * {@code quoteCurrencies} is a list of quote currencies used in past orders and current active
     * user wallets ready to make order es (USD or EUR).
     * @implNote methods that allow to operate on list are: {@link #setQuoteCurrencies(ArrayList)},
     * {@link #insertQuoteCurrency(java.lang.String)}, {@link #removeQuoteCurrency(java.lang.String)} and
     * {@link #insertQuoteCurrency(java.lang.String)}
     * **/
    protected ArrayList<String> quoteCurrencies;

    /**
     * {@code lastTransactionCurrency} is instance that memorize currency used in transactions list methods. <br>
     * Every time that those methods are called if is different from new currency inserted will be replaced with that new currency.
     * **/
    protected String lastTransactionCurrency;

    /**
     * {@code assets} is a list of custom object {@link Asset} give by {@code TraderBot} library.
     * @implNote is used for fetch user wallet and its cryptocurrencies.
     * **/
    protected ArrayList<Asset> assets;

    /**
     * {@code lastBalanceCurrency} is instance that memorize currency used in balance methods. <br>
     * Every time that those methods are called if is different from new currency inserted will be replaced with that new currency.
     * **/
    protected String lastBalanceCurrency;

    /**
     * {@code lastAssetCurrency} is instance that memorize currency used in asset methods. <br>
     * Every time that those methods are called if is different from new currency inserted will be replaced with that new currency.
     * **/
    protected String lastAssetCurrency;

    /**
     * {@code REFRESH_PRICES_TIME} is instance that memorize time for refresh last prices.
     * @implNote this param can customize with {@link #setRefreshPricesTime(int)}
     * @implSpec valid values are from 5 second to 3600 seconds other will generate an {@link Exception}
     * **/
    protected long REFRESH_PRICES_TIME;

    /**
     * {@code lastPricesRefresh} is instance that memorize last time that prices are updated.
     * @implNote when is called {@link #isRefreshTime()} if timestamp of the call minus
     * {@code lastPricesRefresh} is bigger or equal than {@link #REFRESH_PRICES_TIME} execute refresh.
     * **/
    protected long lastPricesRefresh;

    /**
     * {@code balance} is instance that memorize balance of all cryptocurrencies of the user and all cryptocurrencies
     * traded by bot.
     * **/
    protected double balance;

    /**
     * {@code orderStatus} is instance that memorize response and result of an order.
     * @implNote it is obtained calling {@link #getOrderStatus(FormatResponseType)} method
     * **/
    protected String orderStatus;

    /**
     * {@code FormatResponseType} is enum list to format order in different formats.
     * @implSpec format type are {@link String}, JSON type {@link JSONArray} or {@link JSONObject}, CustomObject
     * **/
    public enum FormatResponseType{
        String,
        JSON,
        CustomObject
    }

    /**
     * This method is used by traders to init all instances that routine need. <br>
     * Any params required <br>
     * **/
    protected abstract void initTrader() throws Exception;

    /**
     * This method is used by traders to get user wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return wallet balance in currency value
     * **/
    protected abstract double getWalletBalance(String currency, boolean forceRefresh) throws Exception;

    /**
     * This method is used by traders to get user wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @return wallet balance in currency value
     * **/
    protected abstract double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception;

    /**
     * This method is used to get asset list of user wallet.<br>
     * @param currency: currency of asset balance value es. EUR will return asset balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    protected abstract ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception;

    /**
     * This method is used to get all transactions from all {@link #quoteCurrencies} inserted.<br>
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    protected abstract ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception;

    /**
     * This method is used to get all transactions from all {@link #quoteCurrencies} inserted.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    protected abstract ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception;

    /**
     * This method is used to get all transactions from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    protected abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat,
                                                                  boolean forceRefresh) throws Exception;
    /**
     * This method is used to get all transactions from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    protected abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception;

    /**
     * This method is used to send a buy market order<br>
     * @param symbol: this indicates the symbol for the order es. (BTCBUSD or BTC-USDT)
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * **/
    protected abstract void buyMarket(String symbol, double quantity) throws Exception;

    /**
     * This method is used to send a sell market order<br>
     * @param symbol: this indicates the symbol for the order es. (BTCBUSD or BTC-USDT)
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * **/
    protected abstract void sellMarket(String symbol, double quantity) throws Exception;

    /**
     * This method is used to place an order<br>
     * @param symbol: this indicates the symbol for the order es. (BTCBUSD or BTC-USDT)
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * @param side: this indicates the side of the order (BUY or SELL)
     * **/
    protected abstract void placeAnOrder(String symbol, double quantity, String side) throws Exception;

    /**
     * This method is used to insert or update a coin in {@link #coins} list.
     * @param index: index of the coin es. BTC
     * @param name: name of the coin es Bitcoin
     * @param quantity: quantity of that coin es. 0.28
     * **/
    protected abstract void insertCoin(String index, String name, double quantity);

    /**
     * This method is used fetch details of an order request<br>
     * @implNote you must call it when is placed an order before, so when {@link #buyMarket(java.lang.String, double)}
     * or {@link #sellMarket(java.lang.String, double)} is being called.
     * @param formatResponseType: this indicates the format of order status that have to return.
     * @implSpec if {@code formatResponseType} is equal to {@code String} order status will be returned as {@link String} <br>
     * if {@code formatResponseType} is equal to {@code JSON} order status will be returned as {@link JSONObject} or {@link JSONArray} <br>
     * if {@code formatResponseType} is equal to {@code CustomObject} order status will be returned as custom object given by libraries<br>
     * **/
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

    /**
     * This method is used to get error of any requests<br>
     * Any params required
     * **/
    protected abstract String getErrorResponse();

    /**
     * This method is used to refresh latest prices<br>
     * Any params required
     * **/
    protected abstract void refreshLatestPrice() throws Exception;

    /**
     * This method is used to set time to refresh the latest prices <br>
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * **/
    public void setRefreshPricesTime(int refreshPricesTime) {
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
    }

    /**
     * This method is used to get {@link #REFRESH_PRICES_TIME}<br>
     * Any params required.
     * @return {@link #REFRESH_PRICES_TIME} list long
     * **/
    public long getRefreshPricesTime() {
        return REFRESH_PRICES_TIME;
    }

    /**
     * This method is used to set new list of {@link #quoteCurrencies} overwritten the past list<br>
     * @param quoteCurrencies: list of quote currencies to insert.
     * **/
    public void setQuoteCurrencies(ArrayList<String> quoteCurrencies) {
        this.quoteCurrencies = quoteCurrencies;
    }

    /**
     * This method is used to insert a new quote currency in {@link #quoteCurrencies} list<br>
     * If this value is already inserted in list will be not inserted to avoid duplicate values.
     * @param newQuote: quote currency to insert es. SOL
     * **/
    public void insertQuoteCurrency(String newQuote){
        if(!quoteCurrencies.contains(newQuote))
            quoteCurrencies.add(newQuote);
    }

    /**
     * This method is used to remove a quote currency from {@link #quoteCurrencies} list<br>
     * If this value is not inserted in list will be not removed and will be returned false.
     * @param quoteToRemove: quote currency to remove es. SOL
     * @return status of deletion for {@code quoteToRemove}, will be true only if that value exists in list and can
     * be removed
     * **/
    public boolean removeQuoteCurrency(String quoteToRemove){
        return quoteCurrencies.remove(quoteToRemove);
    }

    /**
     * This method is used to get {@link #quoteCurrencies} list<br>
     * Any params required.
     * @return {@link #quoteCurrencies} list as {@link ArrayList} of {@link String}
     * **/
    public ArrayList<String> getQuoteCurrencies() {
        return quoteCurrencies;
    }

    /**
     * This method is used to check if any quote is inserted in {@link #quoteCurrencies} list
     * @param compareQuote: quote to compare and check if is inserted
     * @return true if is inserted o false if not
     * **/
    protected boolean quoteContained(String compareQuote){
        for (String quote : quoteCurrencies)
            if(compareQuote.equals(quote))
                return true;
        return false;
    }

    /**
     * This method is used check if is refreshing time <br>
     * Any params required.
     * @return true only if current timestamp when this method is called minus last timestamp when this method is called,
     * it is memorized in {@link #lastPricesRefresh}, is bigger or equal than {@link #REFRESH_PRICES_TIME}
     * **/
    protected boolean isRefreshTime(){
        return (System.currentTimeMillis() - lastPricesRefresh) >= REFRESH_PRICES_TIME;
    }

    /**
     * This method is used print error when request is made, if error is not in request
     * will print {@link Exception} error message
     * **/
    protected abstract void printError(String symbol, Exception e);

    /**
     * This method is used to print disclaimer alert to warn user of responsibility of storage and manage
     * api keys create on exchange platforms. <br>
     * It is printed in all traders. <br>
     * Any params required
     * **/
    protected void printCredentialsDisclaimer(){
        System.out.println("""
                ############################### DISCLAIMER ALERT #################################\s
                ## Note: You are solely responsible for storing and using your api keys created ##\s
                ## on the exchange platforms, this is because they will never be saved in our   ##\s   
                ## infrastructures. Never share these personal credentials with anyone,         ##\s
                ## Tecknobit will never ask you for any of the credentials external to our      ##\s
                ## service outside of starting the interfaces, therefore not by email, messages,##\s 
                ## forums or social networks.                                                   ##\s
                ##################################################################################
                """);
    }

}

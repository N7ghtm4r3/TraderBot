package com.tecknobit.traderbot.Routines;

import com.tecknobit.apimanager.Tools.Trading.TradingTools;
import com.tecknobit.binancemanager.Managers.Market.Records.Stats.ExchangeInformation;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.FormatResponseType.*;

public abstract class TraderCoreRoutines {

    public static TradingTools tradingTools = new TradingTools();
    protected static final String COMPARE_CURRENCY = "USDT";
    protected ArrayList<Transaction> transactions;
    protected ArrayList<Transaction> allTransactions;
    protected HashMap<String, ExchangeInformation.Symbol> tradingPairsList;
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

    protected abstract double getWalletBalance(String currency, boolean forceRefresh) throws IOException;

    protected abstract double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws IOException;

    protected abstract ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws IOException;

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

}

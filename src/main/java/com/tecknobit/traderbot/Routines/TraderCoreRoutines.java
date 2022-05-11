package com.tecknobit.traderbot.Routines;

import com.tecknobit.apimanager.Tools.Trading.TradingTools;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.FormatResponseType.*;

public abstract class TraderCoreRoutines {

    public static TradingTools tradingTools = new TradingTools();
    protected static final String COMPARE_CURRENCY = "USDT";
    protected String orderStatus;

    public enum FormatResponseType{
        String,
        JSON,
        CustomObject
    }

    protected abstract void initTrader() throws Exception;

    protected abstract double getWalletBalance(String currency) throws IOException;

    protected abstract double getWalletBalance(String currency, int decimals) throws IOException;

    protected abstract ArrayList<Asset> getAssetsList(String currency) throws IOException;

    protected abstract ArrayList<Transaction> getAllTransactions() throws Exception;

    protected abstract ArrayList<Transaction> getAllTransactions(String dateFormat) throws Exception;

    protected abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency) throws Exception;

    protected abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat) throws Exception;

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

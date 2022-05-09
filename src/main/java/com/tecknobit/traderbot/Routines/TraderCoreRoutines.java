package com.tecknobit.traderbot.Routines;

import com.tecknobit.apimanager.Tools.Trading.TradingTools;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;

import java.io.IOException;
import java.util.ArrayList;

public abstract class TraderCoreRoutines {

    public static TradingTools tradingTools = new TradingTools();

    protected static final String COMPARE_CURRENCY = "USDT";

    protected abstract void initTrader() throws Exception;

    protected abstract double getWalletBalance(String currency) throws IOException;

    protected abstract double getWalletBalance(String currency, int decimals) throws IOException;

    protected abstract ArrayList<Asset> getAssetsList(String currency) throws IOException;

    public abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat) throws Exception;

    public abstract ArrayList<Transaction> getTransactionsList(String quoteCurrency) throws Exception;

    protected abstract String getErrorResponse();

}

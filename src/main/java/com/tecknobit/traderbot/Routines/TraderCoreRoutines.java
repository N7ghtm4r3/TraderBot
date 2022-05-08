package com.tecknobit.traderbot.Routines;

import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;

import java.util.ArrayList;

public abstract class TraderCoreRoutines {

    protected static final String COMPARE_CURRENCY = "USDT";

    public abstract double getWalletBalance(String currency) throws Exception;

    public abstract double getWalletBalance(String currency, int decimals) throws Exception;

    public abstract ArrayList<Asset> getAssetsList(String currency) throws Exception;

    public abstract ArrayList<Transaction> getTransactionsList() throws Exception;

    public abstract String getErrorResponse();

}

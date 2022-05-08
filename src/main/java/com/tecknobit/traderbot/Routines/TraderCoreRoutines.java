package com.tecknobit.traderbot.Routines;

import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;

import java.util.ArrayList;

public abstract class TraderCoreRoutines {

    protected static final String COMPARE_CURRENCY = "USDT";

    protected abstract void initTrader() throws Exception;

    protected abstract double getWalletBalance(String currency);

    protected abstract double getWalletBalance(String currency, int decimals);

    protected abstract ArrayList<Asset> getAssetsList(String currency);

    protected abstract ArrayList<Transaction> getTransactionsList() throws Exception;

    protected abstract String getErrorResponse();

}

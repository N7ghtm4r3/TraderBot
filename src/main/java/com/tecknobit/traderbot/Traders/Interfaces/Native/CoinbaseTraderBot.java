package com.tecknobit.traderbot.Traders.Interfaces.Native;

import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;

import java.io.IOException;
import java.util.ArrayList;

public class CoinbaseTraderBot extends TraderCoreRoutines {

    @Override
    protected void initTrader() throws Exception {
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws IOException {
        return 0;
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws IOException {
        return 0;
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws IOException {
        return null;
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
    public  <T> T getOrderStatus(FormatResponseType formatResponseType) {
        return super.getOrderStatus(formatResponseType);
    }

    @Override
    protected String getErrorResponse() {
        return null;
    }

}

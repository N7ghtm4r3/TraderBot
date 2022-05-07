package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Exceptions.SystemException;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;
import com.tecknobit.traderbot.Routines.TradersCoreRoutines;

import java.io.IOException;
import java.util.ArrayList;

public class BinanceTraderBot extends TradersCoreRoutines {

    private final BinanceWalletManager binanceWalletManager;

    public BinanceTraderBot(String apiKey, String secretKey) throws SystemException, IOException {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws SystemException, IOException {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
    }

    @Override
    public double getWalletBalance() {
        return 0;
    }

    @Override
    public <T> ArrayList<T> getAssetsList() throws Exception {
        return null;
    }

    @Override
    public <T> ArrayList<T> getTransactionsList() {
        return null;
    }

    @Override
    public <T> T getAccountDetails() {
        return null;
    }

    @Override
    public String getErrorResponse() {
        return binanceWalletManager.getErrorResponse();
    }
}

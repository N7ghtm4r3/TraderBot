package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Exceptions.SystemException;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;

import java.io.IOException;

public class BinanceTraderBot {

    private final BinanceWalletManager binanceWalletManager;

    public BinanceTraderBot(String apiKey, String secretKey) throws SystemException, IOException {
        this.binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws SystemException, IOException {
        this.binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
    }

}

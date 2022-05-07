package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Exceptions.SystemException;
import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.BinanceSpotManager;
import com.tecknobit.traderbot.Routines.TradersCoreRoutines;

import java.io.IOException;

public class BinanceTraderBot extends TradersCoreRoutines {

    public static final String COMPARE_CURRENCY = "BNB";
    private final BinanceSpotManager binanceSpotManager;

    private final BinanceMarketManager binanceMarketManager;

    public BinanceTraderBot(String apiKey, String secretKey) throws SystemException, IOException {
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws SystemException, IOException {
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
    }

    public double getWalletBalance() throws Exception {
        return 0;
    }

    public double getWalletBalance(int decimals) throws Exception {
        return binanceMarketManager.roundValue(getWalletBalance(), decimals);
    }

    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

}

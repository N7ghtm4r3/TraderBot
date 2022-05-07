package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Exceptions.SystemException;
import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.BinanceSpotManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.Records.Account.SpotAccountInformation;
import com.tecknobit.traderbot.Routines.TradersCoreRoutines;

import java.io.IOException;
import java.util.HashMap;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.Records.AccountSnapshots.AccountSnapshotSpot.BalancesSpot;

public class BinanceTraderBot extends TradersCoreRoutines {

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

    @Override
    public double getWalletBalance(String currency) throws Exception {
        SpotAccountInformation accountInformation = binanceSpotManager.getObjectSpotAccountInformation();
        HashMap<String, Double> lastPrice = new HashMap<>();
        double balance = 0;
        for(TickerPriceChange tickerPriceChange : binanceMarketManager.getTickerPriceChangeList()) {
            String symbol = tickerPriceChange.getSymbol();
            if(symbol.endsWith(COMPARE_CURRENCY))
                lastPrice.put(symbol, tickerPriceChange.getLastPrice());
        }
        for (BalancesSpot balancesSpot : accountInformation.getBalancesSpots()) {
            double free = balancesSpot.getFree();
            if(free > 0) {
                try {
                    balance += free * lastPrice.get(balancesSpot.getAsset() + COMPARE_CURRENCY);
                }catch (Exception ignored){}
            }
        }
        if(!currency.equals(COMPARE_CURRENCY)){
            try {
                balance /= binanceMarketManager.getCurrentAveragePriceValue(currency + COMPARE_CURRENCY);
            }catch (Exception ignored){}
        }
        return balance;
    }

    @Override
    public double getWalletBalance(String currency, int decimals) throws Exception {
        return binanceMarketManager.roundValue(getWalletBalance(currency), decimals);
    }

    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

}

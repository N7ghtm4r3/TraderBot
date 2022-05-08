package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Exceptions.SystemException;
import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.BinanceSpotManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.Records.Account.SpotAccountInformation;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.Records.AccountSnapshots.AccountSnapshotSpot.BalancesSpot;

public class BinanceTraderBot extends TraderCoreRoutines {

    private final BinanceWalletManager binanceWalletManager;
    private final BinanceSpotManager binanceSpotManager;
    private final BinanceMarketManager binanceMarketManager;

    public BinanceTraderBot(String apiKey, String secretKey) throws SystemException, IOException {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws SystemException, IOException {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
    }

    @Override
    public double getWalletBalance(String currency) throws Exception {
        SpotAccountInformation accountInformation = binanceSpotManager.getObjectSpotAccountInformation();
        HashMap<String, Double> lastPrice = getLastPrices();
        double balance = 0;
        for (BalancesSpot balanceSpot : accountInformation.getBalancesSpots()) {
            double free = balanceSpot.getFree();
            if(free > 0) {
                try {
                    balance += free * lastPrice.get(balanceSpot.getAsset() + COMPARE_CURRENCY);
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
    public ArrayList<Asset> getAssetsList(String currency) throws Exception {
        SpotAccountInformation accountInformation = binanceSpotManager.getObjectSpotAccountInformation();
        JSONArray allCoins = binanceWalletManager.getJSONAllCoins();
        HashMap<String, Double> lastPrice = getLastPrices();
        ArrayList<Asset> assets = new ArrayList<>();
        for (BalancesSpot balanceSpot : accountInformation.getBalancesSpots()){
            String asset = balanceSpot.getAsset();
            double free = balanceSpot.getFree();
            double value;
            try {
                if(free > 0) {
                    String name = "";
                    for (int j = 0; j < allCoins.length(); j++){
                        JSONObject coin = allCoins.getJSONObject(j);
                        if(coin.getString("coin").equals(asset))
                            name = coin.getString("name");
                    }
                    value = free * lastPrice.get(asset + COMPARE_CURRENCY);
                    if(!currency.equals(COMPARE_CURRENCY)){
                        try {
                            value /= binanceMarketManager.getCurrentAveragePriceValue(currency + COMPARE_CURRENCY);
                        }catch (Exception ignored){}
                    }
                    assets.add(new Asset(asset,
                            name,
                            balanceSpot.getFree(),
                            value,
                            currency
                    ));
                }
            }catch (Exception ignored){}
        }
        return assets;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList() throws Exception {
        ArrayList<Transaction> transactions = new ArrayList<>();

        return transactions;
    }

    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

    private HashMap<String, Double> getLastPrices() throws IOException {
        HashMap<String, Double> lastPrices = new HashMap<>();
        for(TickerPriceChange tickerPriceChange : binanceMarketManager.getTickerPriceChangeList()) {
            String symbol = tickerPriceChange.getSymbol();
            if(symbol.endsWith(COMPARE_CURRENCY))
                lastPrices.put(symbol, tickerPriceChange.getLastPrice());
        }
        return lastPrices;
    }

}

package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.BinanceSpotManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BinanceTraderBot extends TraderCoreRoutines {

    private final BinanceWalletManager binanceWalletManager;
    private final BinanceSpotManager binanceSpotManager;
    private final BinanceMarketManager binanceMarketManager;
    private final HashMap<String, Double> lastPrices;

    private final ArrayList<Coin> coins;

    public BinanceTraderBot(String apiKey, String secretKey) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        lastPrices = new HashMap<>();
        coins = new ArrayList<>();
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        lastPrices = new HashMap<>();
        coins = new ArrayList<>();
        initTrader();
    }

    @Override
    protected void initTrader() throws Exception {
        getLastPrices();
        JSONArray allCoins = binanceWalletManager.getJSONAllCoins();
        for (int j=0; j < allCoins.length(); j++){
            JSONObject coin = allCoins.getJSONObject(j);
            double free = coin.getDouble("free");
            if(free > 0){
                coins.add(new Coin(coin.getBoolean("trading"),
                        coin.getString("coin"),
                        free,
                        coin.getDouble("locked"),
                        coin.getString("name")
                ));
            }
        }
    }

    @Override
    public double getWalletBalance(String currency) {
        double balance = 0;
        for (Coin coin : coins)
            if(coin.isTradingEnabled())
                balance += coin.getFree() * lastPrices.get(coin.getAsset() + COMPARE_CURRENCY);
        if(!currency.equals(COMPARE_CURRENCY)){
            try {
                balance /= binanceMarketManager.getCurrentAveragePriceValue(currency + COMPARE_CURRENCY);
            }catch (Exception ignored){}
        }
        return balance;
    }

    @Override
    public double getWalletBalance(String currency, int decimals) {
        return binanceMarketManager.roundValue(getWalletBalance(currency), decimals);
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency) {
        ArrayList<Asset> assets = new ArrayList<>();
        for (Coin coin : coins){
            if(coin.isTradingEnabled()){
                double free = coin.getFree();
                String asset = coin.getAsset();
                double value = free * lastPrices.get(asset + COMPARE_CURRENCY);
                if(!currency.equals(COMPARE_CURRENCY)){
                    try {
                        value /= binanceMarketManager.getCurrentAveragePriceValue(currency + COMPARE_CURRENCY);
                    }catch (Exception ignored){}
                }
                assets.add(new Asset(asset,
                        coin.getName(),
                        free,
                        value,
                        currency
                ));
            }
        }
        return assets;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList() throws Exception {
        ArrayList<Transaction> transactions = new ArrayList<>();
        System.out.println(binanceWalletManager.getDepositHistory().size());
        return transactions;
    }

    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

    private void getLastPrices() throws IOException {
        for(TickerPriceChange tickerPriceChange : binanceMarketManager.getTickerPriceChangeList()) {
            String symbol = tickerPriceChange.getSymbol();
            if(symbol.endsWith(COMPARE_CURRENCY))
                lastPrices.put(symbol, tickerPriceChange.getLastPrice());
        }
    }

}

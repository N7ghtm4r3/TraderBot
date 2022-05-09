package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.BinanceSpotManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.Records.Orders.Response.SpotOrderStatus;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BinanceTraderBot extends TraderCoreRoutines {

    private final BinanceWalletManager binanceWalletManager;
    private final BinanceMarketManager binanceMarketManager;
    private final BinanceSpotManager binanceSpotManager;
    private final ArrayList<Transaction> transactions;
    private final HashMap<String, Double> lastPrices;
    private final ArrayList<Asset> assets;
    private final HashMap<String, Coin> coins;
    private String lastTransactionCurrency;
    private String lastBalanceCurrency;
    private String lastAssetCurrency;
    private long REFRESH_PRICES_TIME;
    private long lastPricesRefresh;
    private double balance;

    public BinanceTraderBot(String apiKey, String secretKey) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        REFRESH_PRICES_TIME = 10000L;
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        balance = -1;
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        REFRESH_PRICES_TIME = 10000L;
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        balance = -1;
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        balance = -1;
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        balance = -1;
        initTrader();
    }

    @Override
    protected void initTrader() throws Exception {
        refreshLastPrices();
        JSONArray allCoins = binanceWalletManager.getJSONAllCoins();
        for (int j=0; j < allCoins.length(); j++){
            JSONObject coin = allCoins.getJSONObject(j);
            double free = coin.getDouble("free");
            if(free > 0){
                String symbol = coin.getString("coin");
                coins.put(symbol, new Coin(coin.getBoolean("trading"),
                        symbol,
                        free,
                        coin.getDouble("locked"),
                        coin.getString("name")
                ));
            }
        }
    }

    @Override
    public double getWalletBalance(String currency) throws IOException {
        if(isRefreshTime() || !lastBalanceCurrency.equals(currency)){
            refreshLastPrices();
            lastBalanceCurrency = currency;
            balance = 0;
            for (Coin coin : coins.values())
                if(coin.isTradingEnabled())
                    balance += coin.getFree() * lastPrices.get(coin.getAsset() + COMPARE_CURRENCY);
            if(!currency.equals(COMPARE_CURRENCY)){
                try {
                    balance /= binanceMarketManager.getCurrentAveragePriceValue(currency + COMPARE_CURRENCY);
                }catch (Exception ignored){}
            }
        }
        return balance;
    }

    @Override
    public double getWalletBalance(String currency, int decimals) throws IOException {
        return binanceMarketManager.roundValue(getWalletBalance(currency), decimals);
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency) throws IOException {
        if(isRefreshTime() || !lastAssetCurrency.equals(currency)){
            refreshLastPrices();
            assets.clear();
            lastAssetCurrency = currency;
            for (Coin coin : coins.values()){
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
        }
        return assets;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat) throws Exception {
        if(isRefreshTime() || !lastTransactionCurrency.equals(quoteCurrency)){
            refreshLastPrices();
            lastTransactionCurrency = quoteCurrency;
            transactions.clear();
            for (Coin coin : coins.values()){
                if(coin.isTradingEnabled()){
                    String symbol = coin.getAsset() + lastTransactionCurrency;
                    if(!symbol.startsWith(lastTransactionCurrency)) {
                        for (SpotOrderStatus order : binanceSpotManager.getObjectAllOrdersList(symbol)){
                            if(order.getStatus().equals("FILLED")){
                                String date;
                                if(dateFormat != null)
                                    date = new SimpleDateFormat(dateFormat).format(new Date(order.getTime()));
                                else
                                    date = new Date(order.getTime()).toString();
                                transactions.add(new Transaction(symbol,
                                        order.getSide(),
                                        date,
                                        order.getCummulativeQuoteQty(),
                                        order.getExecutedQty()
                                ));
                            }
                        }
                    }
                }
            }
        }
        return transactions;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency) throws Exception {
        return getTransactionsList(quoteCurrency, null);
    }

    public void setRefreshPricesTime(int refreshPricesTime) {
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
    }

    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

    private void refreshLastPrices() throws IOException {
        if(isRefreshTime()){
            lastPricesRefresh = System.currentTimeMillis();
            for(TickerPriceChange tickerPriceChange : binanceMarketManager.getTickerPriceChangeList()) {
                String symbol = tickerPriceChange.getSymbol();
                if(symbol.endsWith(COMPARE_CURRENCY))
                    lastPrices.put(symbol, tickerPriceChange.getLastPrice());
            }
        }
    }

    private boolean isRefreshTime(){
        return (System.currentTimeMillis() - lastPricesRefresh) >= REFRESH_PRICES_TIME;
    }

}

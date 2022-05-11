package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.BinanceSpotManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.Records.Orders.Response.SpotOrderStatus;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.Records.Asset.CoinInformation;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;
import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.ExchangeInformation.Symbol;
import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.*;

public class BinanceTraderBot extends TraderCoreRoutines {

    protected final BinanceWalletManager binanceWalletManager;
    protected final BinanceMarketManager binanceMarketManager;
    protected final BinanceSpotManager binanceSpotManager;
    protected final ArrayList<Transaction> transactions;
    protected final ArrayList<Transaction> allTransactions;
    protected final HashMap<String, Symbol> tradingPairsList;
    protected final HashMap<String, Double> lastPrices;
    protected final HashMap<String, Coin> coins;
    protected ArrayList<String> quoteCurrencies;
    protected String lastTransactionCurrency;
    protected final ArrayList<Asset> assets;
    protected String lastBalanceCurrency;
    protected String lastAssetCurrency;
    protected long REFRESH_PRICES_TIME;
    protected long lastPricesRefresh;
    protected double balance;

    public BinanceTraderBot(String apiKey, String secretKey) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        quoteCurrencies = new ArrayList<>();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        REFRESH_PRICES_TIME = 10000L;
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        quoteCurrencies = new ArrayList<>();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        REFRESH_PRICES_TIME = 10000L;
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        quoteCurrencies = new ArrayList<>();
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
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        quoteCurrencies = new ArrayList<>();
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
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                            int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        this.quoteCurrencies = quoteCurrencies;
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
        initTrader();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                            int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        this.quoteCurrencies = quoteCurrencies;
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
        initTrader();
    }

    @Override
    protected void initTrader() throws Exception {
        refreshLastPrices();
        for (CoinInformation coin : binanceWalletManager.getAllCoinsList()){
            double free = coin.getFree();
            if(free > 0){
                String symbol = coin.getCoin();
                coins.put(symbol, new Coin(coin.isTrading(),
                        symbol,
                        free,
                        coin.getLocked(),
                        coin.getName()
                ));
            }
        }
        for (Symbol symbol : binanceMarketManager.getObjectExchangeInformation().getSymbols())
            tradingPairsList.put(symbol.getSymbol(), symbol);
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws IOException {
        if(isRefreshTime() || !lastBalanceCurrency.equals(currency) || forceRefresh){
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
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws IOException {
        return binanceMarketManager.roundValue(getWalletBalance(currency, forceRefresh), decimals);
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws IOException {
        if(isRefreshTime() || !lastAssetCurrency.equals(currency) || forceRefresh){
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
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || allTransactions.isEmpty() || forceRefresh) {
            allTransactions.clear();
            for (String quoteCurrency : quoteCurrencies)
                allTransactions.addAll(getTransactionsList(quoteCurrency, dateFormat, forceRefresh));
            transactions.clear();
        }
        return allTransactions;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        return getAllTransactions(null, forceRefresh);
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastTransactionCurrency.equals(quoteCurrency) || forceRefresh){
            refreshLastPrices();
            lastTransactionCurrency = quoteCurrency;
            transactions.clear();
            for (Coin coin : coins.values()){
                if(coin.isTradingEnabled()){
                    String symbol = coin.getAsset() + lastTransactionCurrency;
                    if(!symbol.startsWith(lastTransactionCurrency)) {
                        try {
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
                        }catch (JSONException ignored){
                        }
                    }
                }
            }
        }
        return transactions;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        return getTransactionsList(quoteCurrency, null, forceRefresh);
    }

    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        placeAnOrder(symbol, quantity, BUY_SIDE);
        int statusCode = binanceSpotManager.getStatusResponse();
        if(statusCode == 200) {
            Symbol coinSymbol = tradingPairsList.get(symbol);
            insertQuoteCurrency(coinSymbol.getQuoteAsset());
            String baseAsset = coinSymbol.getBaseAsset();
            Coin coin = coins.get(baseAsset);
            if(coin != null)
                insertCoin(baseAsset, coin.getFree() + quantity);
            else
                insertCoin(baseAsset, quantity);
        }else
            throw new Exception("Error during buy order status code: [" + statusCode + "]" +
                    " error message: [" + binanceSpotManager.getErrorResponse() + "]");
    }

    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        placeAnOrder(symbol, quantity, SELL_SIDE);
        int statusCode = binanceSpotManager.getStatusResponse();
        if(statusCode == 200) {
            Symbol coinSymbol = tradingPairsList.get(symbol);
            String baseAsset = coinSymbol.getBaseAsset();
            Coin coin = coins.get(baseAsset);
            double newQuantity = coin.getFree() - quantity;
            if(newQuantity == 0)
                coins.remove(baseAsset);
            else
                insertCoin(baseAsset, newQuantity);
        }else
            throw new Exception("Error during sell order status code: [" + statusCode + "]" +
                    " error message: [" + binanceSpotManager.getErrorResponse() + "]");
    }

    @Override
    protected void placeAnOrder(String symbol, double quantity, String side) throws Exception {
        HashMap<String, Object> quantityParam = new HashMap<>();
        quantityParam.put("quantity", quantity);
        orderStatus = binanceSpotManager.testNewOrder(symbol, side, MARKET_TYPE, quantityParam);
    }

    private void insertCoin(String symbol, double quantity) throws Exception {
        coins.put(symbol, new Coin(true,
                symbol,
                quantity,
                0,
                binanceWalletManager.getSingleCoinObject(symbol).getName()
        ));
    }

    @Override
    public <T> T getOrderStatus(FormatResponseType formatResponseType) {
        return super.getOrderStatus(formatResponseType);
    }

    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

    public void setRefreshPricesTime(int refreshPricesTime) {
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
    }

    public void setQuoteCurrencies(ArrayList<String> quoteCurrencies) {
        this.quoteCurrencies = quoteCurrencies;
    }

    public void insertQuoteCurrency(String newQuote){
        if(!quoteCurrencies.contains(newQuote))
            quoteCurrencies.add(newQuote);
    }

    public boolean removeQuoteCurrency(String quoteToRemove){
        return quoteCurrencies.remove(quoteToRemove);
    }

    public ArrayList<String> getQuoteCurrencies() {
        return quoteCurrencies;
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

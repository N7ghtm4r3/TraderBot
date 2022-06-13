package com.tecknobit.traderbot.Traders.Interfaces.Native;

import com.tecknobit.binancemanager.Managers.BinanceManager;
import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.BinanceSpotManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Spot.Records.Orders.Response.SpotOrderStatus;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.Records.Asset.CoinInformation;
import com.tecknobit.traderbot.Records.Portfolio.Asset;
import com.tecknobit.traderbot.Records.Portfolio.Coin;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.ExchangeInformation.Symbol;
import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.*;

/**
 * The {@code BinanceTraderBot} class is trader for {@link BinanceManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Binance's account.<br>
 * Is derived class of {@code TraderCoreRoutines} class from inherit all core routines methods and instances.
 * @author Tecknobit N7ghtm4r3
 * **/

public class BinanceTraderBot extends TraderCoreRoutines {

    /**
     * {@code binanceWalletManager} is instance of {@link BinanceWalletManager} helpful to wallet operations
     * **/
    protected final BinanceWalletManager binanceWalletManager;

    /**
     * {@code binanceMarketManager} is instance of {@link BinanceMarketManager} helpful to market requests
     * **/
    protected final BinanceMarketManager binanceMarketManager;

    /**
     * {@code binanceSpotManager} is instance of {@link BinanceSpotManager} helpful to spot operations
     * **/
    protected final BinanceSpotManager binanceSpotManager;

    /**
     * {@code tradingPairsList} is a map that contains symbols of trading pair list
     * @implSpec this map has as key the asset index (es. BTC) as {@link String} and has as value custom object
     * {@link Symbol} give by {@link BinanceManager} library.
     * **/
    protected HashMap<String, Symbol> tradingPairsList;

    /**
     * {@code lastPrices} is a map that contains asset index (es. BTC) as key {@link String} and last ticker as {@link TickerPriceChange}
     * @apiNote values inserted in this map are only tickers of coins inserted in {@link #coins} list
     * @implNote refresh of last prices, by default, is every 10 seconds, but you can set programmatically
     * {@link #REFRESH_PRICES_TIME} to customize refresh time.
     * **/
    protected HashMap<String, TickerPriceChange> lastPrices;

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        REFRESH_PRICES_TIME = 10000L;
        initTrader();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        REFRESH_PRICES_TIME = 10000L;
        initTrader();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                            int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        this.quoteCurrencies = quoteCurrencies;
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                            int refreshPricesTime) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        this.quoteCurrencies = quoteCurrencies;
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies) throws Exception {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceSpotManager = new BinanceSpotManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    /**
     * This method init all instances that {@link BinanceTraderBot}'s routine need. <br>
     * Any params required <br>
     * **/
    @Override
    protected void initTrader() throws Exception {
        tradingPairsList = new HashMap<>();
        allTransactions = new ArrayList<>();
        if(quoteCurrencies == null)
            quoteCurrencies = new ArrayList<>();
        transactions = new ArrayList<>();
        lastBalanceCurrency = "";
        lastAssetCurrency = "";
        lastTransactionCurrency = "";
        lastPrices = new HashMap<>();
        assets = new ArrayList<>();
        coins = new HashMap<>();
        for (CoinInformation coin : binanceWalletManager.getAllCoinsList()){
            double free = coin.getFree();
            boolean isTradingEnable = true;
            String index = coin.getCoin();
            if(free == 0 || !coin.isTrading())
                isTradingEnable = false;
            coins.put(index, new Coin(index,
                    coin.getName(),
                    free,
                    isTradingEnable
            ));
        }
        for (Symbol symbol : binanceMarketManager.getObjectExchangeInformation().getSymbols())
            tradingPairsList.put(symbol.getSymbol(), symbol);
        refreshLatestPrice();
    }

    /**
     * This method is used by traders to get user Binance's wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @return wallet balance in currency value
     * **/
    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastBalanceCurrency.equals(currency) || forceRefresh){
            refreshLatestPrice();
            lastBalanceCurrency = currency;
            balance = 0;
            for (Coin coin : coins.values())
                if(coin.isTradingEnabled())
                    balance += coin.getQuantity() * lastPrices.get(coin.getAssetIndex() + USDT_CURRENCY).getLastPrice();
            if(!currency.contains(USD_CURRENCY)){
                try {
                    balance /= binanceMarketManager.getCurrentAveragePriceValue(currency + USDT_CURRENCY);
                }catch (Exception ignored){}
            }
        }
        return balance;
    }

    /**
     * This method is used by traders to get user Binance's wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @return wallet balance in currency value
     * **/
    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        return binanceMarketManager.roundValue(getWalletBalance(currency, forceRefresh), decimals);
    }

    /**
     * This method is used to get asset list of Binance's user wallet.<br>
     * @param currency: currency of asset balance value es. EUR will return asset balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastAssetCurrency.equals(currency) || forceRefresh){
            refreshLatestPrice();
            assets.clear();
            lastAssetCurrency = currency;
            for (Coin coin : coins.values()){
                if(coin.isTradingEnabled()){
                    double free = coin.getQuantity();
                    String asset = coin.getAssetIndex();
                    double value = free * lastPrices.get(asset + USDT_CURRENCY).getLastPrice();
                    if(!currency.contains(USD_CURRENCY)){
                        try {
                            value /= binanceMarketManager.getCurrentAveragePriceValue(currency + USDT_CURRENCY);
                        }catch (Exception ignored){}
                    }
                    assets.add(new Asset(asset,
                            coin.getAssetName(),
                            free,
                            value,
                            currency
                    ));
                }
            }
        }
        return assets;
    }

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
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

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        return getAllTransactions(null, forceRefresh);
    }

    /**
     * This method is used to get all transactions for a Binance's account from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastTransactionCurrency.equals(quoteCurrency) || forceRefresh){
            refreshLatestPrice();
            lastTransactionCurrency = quoteCurrency;
            transactions.clear();
            for (Coin coin : coins.values()){
                if(coin.isTradingEnabled()){
                    String baseAsset = coin.getAssetIndex();
                    String symbol = baseAsset + lastTransactionCurrency;
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
                                            order.getExecutedQty(),
                                            lastTransactionCurrency,
                                            baseAsset
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

    /**
     * This method is used to get all transactions for a Binance's account from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        return getTransactionsList(quoteCurrency, null, forceRefresh);
    }

    /**
     * This method is used to send a buy market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        placeAnOrder(symbol, quantity, BUY_SIDE);
        int statusCode = binanceSpotManager.getStatusResponse();
        if(statusCode == 200) {
            Symbol coinSymbol = tradingPairsList.get(symbol);
            insertQuoteCurrency(coinSymbol.getQuoteAsset());
            String baseAsset = coinSymbol.getBaseAsset();
            Coin coin = coins.get(baseAsset);
            insertCoin(baseAsset, coin.getAssetName(), coin.getQuantity() + quantity);
        }else {
            throw new Exception("Error during buy order status code: [" + statusCode + "]" +
                    " error message: [" + binanceSpotManager.getErrorResponse() + "]");
        }
    }

    /**
     * This method is used to send a sell market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        Symbol coinSymbol = tradingPairsList.get(symbol);
        String baseAsset = coinSymbol.getBaseAsset();
        Coin coin = coins.get(baseAsset);
        if(coin != null){
            placeAnOrder(symbol, quantity, SELL_SIDE);
            int statusCode = binanceSpotManager.getStatusResponse();
            if(statusCode == 200) {
                double newQuantity = coin.getQuantity() - quantity;
                if(newQuantity == 0)
                    coins.remove(baseAsset);
                else
                    insertCoin(baseAsset, coin.getAssetName(), newQuantity);
            }else {
                throw new Exception("Error during sell order status code: [" + statusCode + "]" +
                        " error message: [" + binanceSpotManager.getErrorResponse() + "]");
            }
        }else
            throw new Exception("Your wallet doesn't have this coin to sell [" + symbol + "]");
    }

    /**
     * This method is used to place an order<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to trade es. 10
     * @param side: this indicates the side of the order (BUY or SELL)
     * **/
    @Override
    protected void placeAnOrder(String symbol, double quantity, String side) throws Exception {
        HashMap<String, Object> quantityParam = new HashMap<>();
        quantityParam.put("quantity", quantity);
        orderStatus = binanceSpotManager.testNewOrder(symbol, side, MARKET_TYPE, quantityParam);
    }

    /**
     * This method is used to insert or update a coin in {@link #coins} list.
     * @param index: index of the coin es. BTC
     * @param name: name of the coin es Bitcoin
     * @param quantity: quantity of that coin es. 0.28
     * **/
    @Override
    protected void insertCoin(String index, String name, double quantity) {
        coins.put(index, new Coin(index,
                name,
                quantity,
                true
        ));
    }

    /**
     * This method is used fetch details of a Binance's order request<br>
     * @implNote you must call it when is placed an order before, so when {@link #buyMarket(java.lang.String, double)}
     * or {@link #sellMarket(java.lang.String, double)} is being called.
     * @param formatResponseType: this indicates the format of order status that have to return.
     * @implSpec if {@code formatResponseType} is equal to {@code String} order status will be returned as {@link String} <br>
     * if {@code formatResponseType} is equal to {@code JSON} order status will be returned as {@link JSONObject} or {@link JSONArray} <br>
     * if {@code formatResponseType} is equal to {@code CustomObject} order status will be returned as custom object given by libraries<br>
     * **/
    @Override
    public <T> T getOrderStatus(FormatResponseType formatResponseType) {
        return super.getOrderStatus(formatResponseType);
    }

    /**
     * This method is used to get error of any requests<br>
     * Any params required
     * **/
    @Override
    public String getErrorResponse() {
        return binanceSpotManager.getErrorResponse();
    }

    /**
     * This method is used to refresh latest prices<br>
     * Any params required
     * **/
    @Override
    protected void refreshLatestPrice() throws Exception {
        lastPricesRefresh = System.currentTimeMillis();
        for(TickerPriceChange tickerPriceChange : binanceMarketManager.getTickerPriceChangeList()) {
            String symbol = tickerPriceChange.getSymbol();
            try {
                if(coins.get(tradingPairsList.get(symbol).getBaseAsset()).isTradingEnabled() || symbol.endsWith(USDT_CURRENCY))
                    lastPrices.put(symbol, tickerPriceChange);
            }catch (NullPointerException ignored){
            }
        }
    }

    /**
     * This method is used print error when request is made, if error is not in request
     * will print {@link Exception} error message
     * **/
    @Override
    protected void printError(String symbol, Exception e){
        if(binanceSpotManager.getStatusResponse() != 200)
            System.out.println(getErrorResponse() + " on [" + symbol + "]");
        else
            e.printStackTrace();
    }

}

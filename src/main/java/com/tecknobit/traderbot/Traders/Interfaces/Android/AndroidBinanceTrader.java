package com.tecknobit.traderbot.Traders.Interfaces.Android;

import com.tecknobit.apimanager.Tools.Trading.CryptocurrencyTool;
import com.tecknobit.binancemanager.Managers.BinanceManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Account.TraderDetails;
import com.tecknobit.traderbot.Records.Portfolio.Asset;
import com.tecknobit.traderbot.Records.Portfolio.Coin;
import com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.Android.AndroidCoreRoutines;
import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow;
import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow.Credentials;
import com.tecknobit.traderbot.Routines.Android.ServerRequest;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Trade.Common.TradeConstants.SELL;
import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.*;
import static com.tecknobit.traderbot.Records.Portfolio.Token.BASE_ASSET_KEY;
import static java.lang.Math.toIntExact;
import static java.lang.System.currentTimeMillis;
import static java.text.DateFormat.getDateTimeInstance;

/**
 * The {@code AndroidBinanceTrader} class is trader for {@link BinanceManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Binance's account.<br>
 * Is derived class of {@link BinanceTraderBot} class from inherit all core routines methods and instances.
 * @implNote This is an Android's interface you can
 * find the apk source at <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>
 * @author Tecknobit N7ghtm4r3
 * **/

public class AndroidBinanceTrader extends BinanceTraderBot implements AndroidCoreRoutines {

    /**
     * {@code transactionDateFormat} is instance helpful to format transaction date
     * **/
    private final DateFormat transactionDateFormat;

    /**
     * {@code cryptocurrencyTool} is instance helpful to manage cryptocurrencies details
     * **/
    private final CryptocurrencyTool cryptocurrencyTool;

    /**
     * {@code traderDetails} is instance helpful to manage trader details
     * @implNote will be instantiated with default values by {@link AndroidBinanceTrader}
     * **/
    private final TraderDetails traderDetails;

    /**
     * {@code traderAccount} is instance helpful to manage trader account stats
     * @implNote will be instantiated with account values with server request
     * **/
    private final TraderAccount traderAccount;

    /**
     * {@code androidWorkflow} is instance helpful to manage Android's workflow
     * **/
    private final AndroidWorkflow androidWorkflow;

    /**
     * {@code authToken} is instance that memorize identifier of server trader to log in and requests operations
     * **/
    private final String authToken;

    /**
     * {@code token} is instance that memorize identifier of user to log in and requests operations
     * **/
    private final String token;

    /**
     * {@code ivSpec} is instance that memorize initialization vector used in server requests
     * **/
    private final String ivSpec;

    /**
     * {@code secretKey} is instance that memorize secret key used in server requests
     * **/
    private final String secretKey;

    /**
     * {@code symbol} is instance that memorize symbol of cryptocurrency es. BTCBUSD or BTC-USD
     * **/
    private String symbol;

    /**
     * {@code side} is instance that memorize side of order BUY or SELL
     * **/
    private String side;

    /**
     * {@code walletList} is a map that contains wallet list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * **/
    private final ConcurrentHashMap<String, Cryptocurrency> walletList;

    /**
     * {@code runningTrader} is instance that memorize flag that indicates if the trader is running
     * **/
    private boolean runningTrader;

    /**
     * {@code baseCurrency} is instance that memorize base currency to get all amount value of traders routine es. EUR
     * **/
    private String baseCurrency;

    /** Constructor to init {@link AndroidBinanceTrader}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param refreshPricesTime    : is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public AndroidBinanceTrader(String apiKey, String secretKey, Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency, int refreshPricesTime) throws Exception {
        super(apiKey, secretKey);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /** Constructor to init {@link AndroidBinanceTrader}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param refreshPricesTime    : is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, Credentials credentials,
                                boolean printRoutineMessages, String baseCurrency, int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /** Constructor to init {@link AndroidBinanceTrader}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime    : is time in seconds to set for refresh the latest prices
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public AndroidBinanceTrader(String apiKey, String secretKey, int refreshPricesTime, Credentials credentials,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        checkCredentialsValidity(credentials);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /** Constructor to init {@link AndroidBinanceTrader}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /** Constructor to init {@link AndroidBinanceTrader}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /** Constructor to init {@link AndroidBinanceTrader}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /** Constructor to init {@link AndroidBinanceTrader}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency,
                                int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.baseCurrency = baseCurrency;
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        traderAccount = new TraderAccount(serverRequest);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * This method init all instances that {@link AndroidBinanceTrader}'s routine need. <br>
     * Any params required <br>
     * **/
    @Override
    protected void initTrader() throws Exception {
        printAndroidDisclaimer();
        super.initTrader();
    }

    /**
     * This method is used to init a {@link Credentials} object to start {@link AndroidWorkflow}
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * **/
    @Override
    public void initCredentials(Credentials credentials) throws Exception {
        checkCredentialsValidity(credentials);
        credentials.setTraderDetails(traderDetails);
        if(credentials.getToken() == null)
            credentials.sendRegistrationRequest();
        else
            credentials.sendLoginRequest(baseCurrency, quoteCurrencies);
    }

    /**
     * This method is used to handle {@link AndroidWorkflow} <br>
     * Any params required
     * **/
    @Override
    public void workflowHandler() {
        enableTrader();
        refreshWalletList();
        androidWorkflow.startWorkflow();
    }

    /**
     * This method is used to update account wallet list <br>
     * Any params required
     * **/
    @Override
    public void refreshWalletList() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                JSONArray wallet = new JSONArray();
                while (true){
                    if(runningTrader){
                        try {
                            if(isRefreshTime())
                                refreshLatestPrice();
                            for (Cryptocurrency cryptocurrency : walletList.values()){
                                String assetIndex = cryptocurrency.getAssetIndex();
                                TickerPriceChange ticker = lastPrices.get(assetIndex + USDT_CURRENCY);
                                double lastPrice = ticker.getLastPrice();
                                double priceChangePercent = ticker.getPriceChangePercent();
                                cryptocurrency.setLastPrice(lastPrice);
                                cryptocurrency.setPriceChangePercent(priceChangePercent);
                                walletList.put(assetIndex, cryptocurrency);
                                wallet.put(new JSONObject().put(BASE_ASSET_KEY, assetIndex)
                                        .put(LAST_PRICE_KEY, lastPrice)
                                        .put(PRICE_CHANGE_PERCENT_KEY, priceChangePercent)
                                        .put(INCOME_PERCENT_KEY, cryptocurrency.getIncomePercent(2)));
                            }
                            androidWorkflow.insertRefreshedPrices(wallet);
                            wallet.clear();
                        }catch (Exception e){
                            printRed("Error during refreshing wallet list");
                        }finally {
                            try {
                                sleep(REFRESH_PRICES_TIME);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * This method is used by traders to get user Binance's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh) throws Exception {
        if(runningTrader) {
            balance = super.getWalletBalance(baseCurrency, forceRefresh);
            androidWorkflow.insertWalletBalance(binanceMarketManager.roundValue(balance, 2));
            return balance;
        }
        return -1;
    }

    /**
     * This method is used by traders to get user Binance's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh, int decimals) throws Exception {
        if(runningTrader)
            return binanceMarketManager.roundValue(getWalletBalance(forceRefresh), decimals);
        return -1;
    }

    /**
     * This method is used to get asset list of Binance's user wallet.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    public ArrayList<Asset> getAssetsList(boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAssetsList(baseCurrency, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(dateFormat, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Binance's account from all {@link #quoteCurrencies} inserted.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Binance's account from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, dateFormat, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Binance's account from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, forceRefresh);
        return null;
    }

    /**
     * This method is used to send a buy market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * @implNote if {@link #runningTrader} is false will do not the buy operation
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(runningTrader){
            super.buyMarket(symbol, quantity);
            this.symbol = symbol;
        }
    }

    /**
     * This method is used to send a sell market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * @implNote if {@link #runningTrader} is false will do not the sell operation
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if(runningTrader){
            super.sellMarket(symbol, quantity);
            this.symbol = symbol;
        }
    }

    /**
     * This method is used to place an order<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to trade es. 10
     * @param side: this indicates the side of the order (BUY or SELL)
     * **/
    @Override
    protected void placeAnOrder(String symbol, double quantity, String side) throws Exception {
        super.placeAnOrder(symbol, quantity, side);
        this.side = side;
    }

    /**
     * This method is used to insert or update a coin in {@link #coins} list.
     * @param index: index of the coin es. BTC
     * @param name: name of the coin es Bitcoin
     * @param quantity: quantity of that coin es. 0.28
     * @implNote in Android's interfaces this method updates also {@link #traderAccount} instance
     * stats and insert a new transaction
     * **/
    @Override
    protected void insertCoin(String index, String name, double quantity) {
        super.insertCoin(index, name, quantity);
        String quoteAsset = tradingPairsList.get(symbol).getQuoteAsset();
        Cryptocurrency cryptocurrency = walletList.get(index);
        TickerPriceChange ticker = lastPrices.get(symbol);
        double lastPrice = ticker.getLastPrice();
        Coin coin = coins.get(index);
        int sales = 0;
        Transaction transaction = new Transaction(symbol, side, transactionDateFormat.format(new Date(currentTimeMillis())),
                binanceMarketManager.roundValue(quantity * lastPrice, 2), quantity, quoteAsset, index);
        if(side.equals(SELL)){
            double income = cryptocurrency.getIncomePercent();
            sales = androidWorkflow.getSellSales(transaction, traderAccount, cryptocurrency, getTypeSellCode(income));
            traderAccount.addIncome(income);
        }else{
            if(cryptocurrency == null) {
                cryptocurrency = new Cryptocurrency(index, cryptocurrencyTool.getCryptocurrencyName(index), quantity,
                    symbol, lastPrice, -1 , null, ticker.getPriceChangePercent(), quoteAsset, null);
                cryptocurrency.addFirstPrice(lastPrice);
            }else {
                cryptocurrency.setQuantity(coin.getQuantity());
                cryptocurrency.addFirstPrice(lastPrice);
            }
            walletList.put(index, cryptocurrency);
        }
        if(coin.isTradingEnabled())
            androidWorkflow.insertCryptocurrency(cryptocurrency, transaction, sales, traderAccount.getTotalIncome(2));
        else {
            androidWorkflow.removeCryptocurrency(index, transaction);
            walletList.remove(index);
        }
    }

    /**
     * This method is used to set time to refresh the latest prices <br>
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote in Android's interfaces this method updates also {@link #traderDetails} instance
     * **/
    @Override
    public void setRefreshPricesTime(int refreshPricesTime) {
        super.setRefreshPricesTime(refreshPricesTime);
        if(traderDetails != null)
            traderDetails.setRefreshPricesTime(refreshPricesTime);
    }

    /**
     * This method is used to get if bot is in running mode
     * @return flag that indicates if the bot is running
     * **/
    @Override
    public boolean isTraderRunning() {
        return runningTrader;
    }

    /**
     * This method is used to disable running mode of trader
     * @implNote in Android's interfaces this method updates also
     * {@link #traderDetails} status instance to STOPPED_TRADER_STATUS
     * **/
    @Override
    public void disableTrader() {
        runningTrader = false;
        traderDetails.setTraderStatus(STOPPED_TRADER_STATUS);
    }

    /**
     * This method is used to enable running mode of trader
     * @implNote in Android's interfaces this method updates also
     * {@link #traderDetails} status instance to RUNNING_TRADER_STATUS
     * **/
    @Override
    public void enableTrader() {
        runningTrader = true;
        traderDetails.setTraderStatus(RUNNING_TRADER_STATUS);
    }

    /**
     * This method is used to get sales at loss
     * @return sales at loss
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getSalesAtLoss() {
        if(runningTrader)
            return traderAccount.getSalesAtLoss();
        return -1;
    }

    /**
     * This method is used to get sales at gain
     * @return sales at gain
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getSalesAtGain() {
        if(runningTrader)
            return traderAccount.getSalesAtGain();
        return -1;
    }

    /**
     * This method is used to get sales at pair
     * @return sales at pair
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getSalesInPair() {
        if(runningTrader)
            return traderAccount.getSalesAtPair();
        return -1;
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * @implNote if {@link #runningTrader} is false will return -1
     * **/
    @Override
    public double getTotalSales() {
        if(runningTrader)
            return traderAccount.getTotalSales();
        return -1;
    }

    /**
     * This method is used to set base currency for change amount value
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * **/
    @Override
    public void setBaseCurrency(String baseCurrency) {
        if(baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USDT");
        this.baseCurrency = baseCurrency;
    }

    /**
     * This method is used to get base currency for change amount value <br>
     * Any params required
     * @implNote if {@link #runningTrader} is false will return null
     * **/
    @Override
    public String getBaseCurrency() {
        if(runningTrader)
            return baseCurrency;
        return null;
    }

    /**
     * This method is used to get credentials inserted for trader login
     * @return trader credentials as {@link Credentials} object
     * **/
    @Override
    public Credentials getCredentials() {
        return androidWorkflow.getCredentials();
    }

    /**
     * This method is used to set flag to print routine messages
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        androidWorkflow.setPrintRoutineMessages(printRoutineMessages);
    }

    /**
     * This method is used to get flag to print or not routine messages
     * @return flag that indicates the possibility or not to print or not routine messages
     * **/
    @Override
    public boolean canPrintRoutineMessages() {
        return androidWorkflow.canPrintRoutineMessages();
    }

}

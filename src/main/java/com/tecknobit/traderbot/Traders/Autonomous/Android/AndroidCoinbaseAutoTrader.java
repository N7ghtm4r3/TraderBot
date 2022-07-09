package com.tecknobit.traderbot.Traders.Autonomous.Android;

import com.tecknobit.apimanager.Tools.Trading.CryptocurrencyTool;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.CoinbaseManager;
import com.tecknobit.traderbot.Helpers.Orders.MarketOrder;
import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Account.TraderDetails;
import com.tecknobit.traderbot.Records.Portfolio.Asset;
import com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.Android.AndroidCoreRoutines;
import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow;
import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow.Credentials;
import com.tecknobit.traderbot.Routines.Android.ServerRequest;
import com.tecknobit.traderbot.Routines.Autonomous.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Autonomous.Native.CoinbaseAutoTraderBot;
import com.tecknobit.traderbot.Traders.Interfaces.Android.AndroidBinanceTrader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.BUY_SIDE;
import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.SELL_SIDE;
import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.*;
import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.TradingConfig.MODEL_ID_KEY;
import static com.tecknobit.traderbot.Records.Portfolio.Token.BASE_ASSET_KEY;
import static com.tecknobit.traderbot.Records.Portfolio.Transaction.TRANSACTION_KEY;
import static java.lang.Math.toIntExact;
import static java.lang.System.currentTimeMillis;
import static java.text.DateFormat.getDateTimeInstance;

/**
 * The {@code AndroidCoinbaseAutoTrader} class is trader for {@link CoinbaseManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Coinbase's account autonomously.<br>
 * Is derived class of {@link CoinbaseAutoTraderBot} class from inherit all core routines methods and instances.
 * @implNote for autonomous operations uses {@link AutoTraderCoreRoutines} and {@link MarketOrder} routines.
 * @implSpec {@link TraderAccount} and {@link #walletList} are objects that will automatically restore, but if
 * you change trader's platform all assets will be deleted from the managing of the trader.
 * @author Tecknobit N7ghtm4r3
 * **/

public class AndroidCoinbaseAutoTrader extends CoinbaseAutoTraderBot implements AndroidCoreRoutines {

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
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     int timeout, boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, int timeout, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials,
                                     int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                        credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials,
                                     int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     int timeout, ArrayList<String> quoteCurrencies, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials,
                                     int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, quoteCurrencies, new TraderAccount(
                new ServerRequest(credentials.getIvSpec(), credentials.getSecretKey(), credentials.getAuthToken(),
                        credentials.getToken())), sendStatsReport, printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param timeout              : custom timeout for request
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, int timeout,
                                     ArrayList<String> quoteCurrencies, boolean sendStatsReport, boolean printRoutineMessages,
                                     String baseCurrency, Credentials credentials, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, quoteCurrencies, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     ArrayList<String> quoteCurrencies, boolean sendStatsReport, boolean printRoutineMessages,
                                     String baseCurrency, Credentials credentials, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, quoteCurrencies, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                        credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                     boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials, int refreshPricesTime) throws Exception {
        super(apiKey, apiSecret, passphrase, quoteCurrencies, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                        credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        setRefreshPricesTime(refreshPricesTime);
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     int timeout, int refreshPricesTime, boolean sendStatsReport, boolean printRoutineMessages,
                                     String baseCurrency, Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, refreshPricesTime, new TraderAccount(
                new ServerRequest(credentials.getIvSpec(), credentials.getSecretKey(), credentials.getAuthToken(),
                        credentials.getToken())), sendStatsReport, printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, int timeout, int refreshPricesTime,
                                     boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, refreshPricesTime, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     int refreshPricesTime, boolean sendStatsReport, boolean printRoutineMessages,
                                     String baseCurrency, Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, refreshPricesTime, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                        credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, int refreshPricesTime,
                                     boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, refreshPricesTime, new TraderAccount(new ServerRequest(credentials.getIvSpec(),
                credentials.getSecretKey(), credentials.getAuthToken(), credentials.getToken())), sendStatsReport,
                printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     int timeout, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                     boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency,
                                     Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout, quoteCurrencies, refreshPricesTime,
                new TraderAccount(new ServerRequest(credentials.getIvSpec(), credentials.getSecretKey(), credentials.getAuthToken(),
                        credentials.getToken())), sendStatsReport, printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();

    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param timeout              : custom timeout for request
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, int timeout,
                                     ArrayList<String> quoteCurrencies, int refreshPricesTime, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, timeout, quoteCurrencies, refreshPricesTime, new TraderAccount(
                new ServerRequest(credentials.getIvSpec(), credentials.getSecretKey(), credentials.getAuthToken(),
                        credentials.getToken())), sendStatsReport, printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param defaultErrorMessage  : custom error to show when is not a request error
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                                     ArrayList<String> quoteCurrencies, int refreshPricesTime, boolean sendStatsReport,
                                     boolean printRoutineMessages, String baseCurrency, Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, defaultErrorMessage, quoteCurrencies, refreshPricesTime, new TraderAccount(
                        new ServerRequest(credentials.getIvSpec(), credentials.getSecretKey(), credentials.getAuthToken(),
                                credentials.getToken())), sendStatsReport, printRoutineMessages, baseCurrency);
        long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     * Constructor to init {@link AndroidCoinbaseAutoTrader}
     * @param apiKey               : your Coinbase's api key
     * @param apiSecret            : your Coinbase's secret key
     * @param passphrase           : your Coinbase's api passphrase
     * @param quoteCurrencies      : is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport      : flag to insert to send or not reports
     * @param printRoutineMessages : flag to insert to print or not routine messages
     * @param baseCurrency         : base currency to get all amount value of traders routine es. EUR
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     **/
    public AndroidCoinbaseAutoTrader(String apiKey, String apiSecret, String passphrase, ArrayList<String> quoteCurrencies,
                                     int refreshPricesTime, boolean sendStatsReport, boolean printRoutineMessages,
                                     String baseCurrency, Credentials credentials) throws Exception {
        super(apiKey, apiSecret, passphrase, quoteCurrencies, refreshPricesTime, new TraderAccount(
                        new ServerRequest(credentials.getIvSpec(), credentials.getSecretKey(), credentials.getAuthToken(),
                                credentials.getToken())), sendStatsReport, printRoutineMessages, baseCurrency);long timestamp = currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_AUTONOMOUS, RUNNING_TRADER_STATUS, COINBASE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials(credentials);
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        ServerRequest serverRequest = new ServerRequest(ivSpec, this.secretKey, authToken, token);
        androidWorkflow = new AndroidWorkflow(serverRequest, this, credentials, printRoutineMessages);
        cryptocurrencyTool = new CryptocurrencyTool();
        transactionDateFormat = getDateTimeInstance();
        walletList = traderAccount.getWalletCryptocurrencies();
        workflowHandler();
    }

    /**
     *
     * This method init all instances that {@link AndroidBinanceAutoTrader}'s routine need. <br>
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
        androidWorkflow.checkWalletList(walletList);
        androidWorkflow.startWorkflow();
    }

    /**
     * This method is used to check list of possible new cryptocurrencies to buy using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void checkCryptocurrencies() throws Exception {
        super.checkCryptocurrencies();
        JSONArray checkingList = new JSONArray();
        for (Cryptocurrency cryptocurrency : this.checkingList.values()){
            checkingList.put(new JSONObject().put(BASE_ASSET_KEY, cryptocurrency.getAssetIndex())
                    .put(SYMBOL_KEY, cryptocurrency.getSymbol())
                    .put(ASSET_NAME_KEY, cryptocurrency.getAssetName())
                    .put(TPTOP_INDEX_KEY, cryptocurrency.getTptopIndex(2))
                    .put(LAST_PRICE_KEY, cryptocurrency.getLastPrice(2))
                    .put(PRICE_CHANGE_PERCENT_KEY, cryptocurrency.getPriceChangePercent(2))
                    .put(MODEL_ID_KEY, cryptocurrency.getTradingConfig().getModelId())
                    .put(QUOTE_ASSET_KEY, cryptocurrency.getQuoteAsset())
            );
        }
        androidWorkflow.insertCheckingList(checkingList);
    }

    /**
     * This method is used to buy new cryptocurrencies from list loaded from {@link #checkCryptocurrencies()} routine
     * using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void buyCryptocurrencies() throws Exception {
        HashMap<String, Cryptocurrency> mCheckingList = new HashMap<>(checkingList);
        super.buyCryptocurrencies();
        JSONArray wallet = new JSONArray();
        for (Cryptocurrency cryptocurrency : walletList.values()){
            if(mCheckingList.containsKey(cryptocurrency.getAssetIndex())){
                wallet.put(new JSONObject(cryptocurrency.getCryptocurrency())
                        .put(TRANSACTION_KEY, androidWorkflow.assembleTransaction(cryptocurrency, BUY_SIDE,
                                transactionDateFormat).getTransaction()));
            }
        }
        androidWorkflow.insertWalletList(wallet);
    }

    /**
     * This method is used to routine of update wallet of cryptocurrencies bought by auto trader. If {@link Cryptocurrency}
     * respects {@link Cryptocurrency.TradingConfig} model that {@link Cryptocurrency} will be sold. <br>
     * Any params required
     * **/
    @Override
    public void updateWallet() throws Exception {
        super.updateWallet();
        refreshWalletList();
    }

    /**
     * This method is used to update account wallet list <br>
     * Any params required
     * **/
    @Override
    public void refreshWalletList() throws Exception {
        JSONArray wallet = new JSONArray();
        for (Cryptocurrency cryptocurrency : walletList.values()){
            wallet.put(new JSONObject().put(BASE_ASSET_KEY, cryptocurrency.getAssetIndex())
                    .put(LAST_PRICE_KEY, cryptocurrency.getLastPrice())
                    .put(PRICE_CHANGE_PERCENT_KEY, cryptocurrency.getPriceChangePercent(2))
                    .put(INCOME_PERCENT_KEY, cryptocurrency.getIncomePercent(2)));
        }
        androidWorkflow.insertRefreshedPrices(wallet);
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh) {
        if(runningTrader) {
            balance = super.getWalletBalance(baseCurrency, forceRefresh);
            androidWorkflow.insertWalletBalance(coinbaseCurrenciesManager.roundValue(balance, 2));
            return balance;
        }
        return -1;
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @implNote if {@link #runningTrader} is false will return -1
     * @return wallet balance in currency value
     * **/
    public double getWalletBalance(boolean forceRefresh, int decimals) throws Exception {
        if(runningTrader)
            return coinbaseCurrenciesManager.roundValue(getWalletBalance(forceRefresh), decimals);
        return -1;
    }

    /**
     * This method is used to get asset list of Coinbase's user wallet.<br>
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @implNote if {@link #runningTrader} is false will return null
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    public ArrayList<Asset> getAssetsList(boolean forceRefresh) {
        if(runningTrader)
            return super.getAssetsList(baseCurrency, forceRefresh);
        return null;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
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
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
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
     * This method is used to get all transactions for a Coinbase's account from a single symbol<br>
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
     * This method is used to get all transactions for a Coinbase's account from a single symbol<br>
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
     * This method is used to send a buy market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * @implNote if {@link #runningTrader} is false will do not the buy operation
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(runningTrader)
            super.buyMarket(symbol, quantity);
    }

    /**
     * This method is used to send a sell market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * @implNote if {@link #runningTrader} is false will do not the sell operation
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if(runningTrader) {
            super.sellMarket(symbol, quantity);
            androidWorkflow.removeCryptocurrency(cryptocurrencySold.getAssetIndex(),
                    androidWorkflow.assembleTransaction(cryptocurrencySold, SELL_SIDE, transactionDateFormat));
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
     * This method is used to disable running mode of trader
     * @implNote in Android's interfaces this method updates also
     * {@link #traderDetails} status instance to STOPPED_TRADER_STATUS
     * **/
    @Override
    public void disableTrader() {
        super.disableTrader();
        traderDetails.setTraderStatus(STOPPED_TRADER_STATUS);
    }

    /**
     * This method is used to enable running mode of trader
     * @implNote in Android's interfaces this method updates also
     * {@link #traderDetails} status instance to RUNNING_TRADER_STATUS
     * **/
    @Override
    public void enableTrader() {
        super.enableTrader();
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
            return super.getSalesAtLoss();
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
            return super.getSalesAtGain();
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
            return super.getSalesInPair();
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
            return super.getTotalSales();
        return -1;
    }

    /**
     * This method is used to get base currency for change amount value <br>
     * Any params required
     * @implNote if {@link #runningTrader} is false will return null
     * **/
    @Override
    public String getBaseCurrency() {
        if(runningTrader)
            return super.getBaseCurrency();
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
        super.setPrintRoutineMessages(printRoutineMessages);
        androidWorkflow.setPrintRoutineMessages(printRoutineMessages);
    }

}

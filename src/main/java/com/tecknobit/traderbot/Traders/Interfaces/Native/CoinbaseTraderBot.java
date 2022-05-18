package com.tecknobit.traderbot.Traders.Interfaces.Native;

import com.tecknobit.coinbasemanager.Managers.ExchangePro.Account.CoinbaseAccountManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Account.Records.CoinbaseAccount;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.CoinbaseManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Currencies.CoinbaseCurrenciesManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.CoinbaseOrdersManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Products.CoinbaseProductsManager;
import com.tecknobit.coinbasemanager.Managers.ExchangePro.Products.Records.TradingPair;
import com.tecknobit.traderbot.Records.Asset;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.TraderCoreRoutines;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.tecknobit.coinbasemanager.Managers.ExchangePro.Orders.Records.Order.*;

/**
 * The {@code CoinbaseTraderBot} class is trader for {@link CoinbaseManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Coinbase's account.<br>
 * Is derived class of {@code TraderCoreRoutines} class from inherit all core routines methods and instances.
 * @author Tecknobit N7ghtm4r3
 * **/

public class CoinbaseTraderBot extends TraderCoreRoutines {

    /**
     * {@code coinbaseAccountManager} is instance of {@link CoinbaseAccountManager} helpful to account operations
     * **/
    private final CoinbaseAccountManager coinbaseAccountManager;

    /**
     * {@code coinbaseProductsManager} is instance of {@link CoinbaseProductsManager} helpful to products requests
     * **/
    private final CoinbaseProductsManager coinbaseProductsManager;

    /**
     * {@code coinbaseCurrenciesManager} is instance of {@link CoinbaseCurrenciesManager} helpful to fetch details about
     * cryptocurrencies
     * **/
    private final CoinbaseCurrenciesManager coinbaseCurrenciesManager;

    /**
     * {@code coinbaseOrdersManager} is instance of {@link CoinbaseOrdersManager} helpful to order operations
     * **/
    private final CoinbaseOrdersManager coinbaseOrdersManager;

    /**
     * {@code tradingPairsList} is a map that contains symbols of trading pair list
     * @implSpec this map has as key the asset index (es. BTC) as {@link String} and has as value custom object
     * {@link TradingPair} give by {@link CoinbaseManager} library.
     * **/
    protected HashMap<String, TradingPair> tradingPairsList;

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             int timeout) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, timeout);
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase);
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage, int timeout,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, defaultErrorMessage, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, defaultErrorMessage, timeout);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, defaultErrorMessage, timeout);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, timeout);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase,
                             ArrayList<String> quoteCurrencies) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase);
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             int timeout, int refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout, int refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, timeout);
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             short refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, short refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase);
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param timeout: custom timeout for request
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage, int timeout,
                             ArrayList<String> quoteCurrencies, int refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, defaultErrorMessage, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, defaultErrorMessage, timeout);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, defaultErrorMessage, timeout);
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        this.quoteCurrencies = quoteCurrencies;
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param timeout: custom timeout for request
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, int timeout,
                             ArrayList<String> quoteCurrencies, int refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, timeout);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, timeout);
        this.quoteCurrencies = quoteCurrencies;
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param defaultErrorMessage: custom error to show when is not a request error
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase, String defaultErrorMessage,
                             ArrayList<String> quoteCurrencies, int refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase, defaultErrorMessage);
        this.quoteCurrencies = quoteCurrencies;
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /** Constructor to init CoinbaseTraderBot
     * @param apiKey: your Coinbase's api key
     * @param apiSecret: your Coinbase's secret key
     * @param passphrase: your Coinbase's api passphrase
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices.
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR).
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public CoinbaseTraderBot(String apiKey, String apiSecret, String passphrase,
                             ArrayList<String> quoteCurrencies, int refreshPricesTime) throws Exception {
        coinbaseAccountManager = new CoinbaseAccountManager(apiKey, apiSecret, passphrase);
        coinbaseProductsManager = new CoinbaseProductsManager(apiKey, apiSecret, passphrase);
        coinbaseCurrenciesManager = new CoinbaseCurrenciesManager(apiKey, apiSecret, passphrase);
        coinbaseOrdersManager = new CoinbaseOrdersManager(apiKey, apiSecret, passphrase);
        this.quoteCurrencies = quoteCurrencies;
        if(refreshPricesTime >= 5 && refreshPricesTime <= 3600)
            REFRESH_PRICES_TIME = refreshPricesTime * 1000L;
        else
            throw new IllegalArgumentException("Refresh prices time must be more than 5 (5s) and less than 3600 (1h)");
        initTrader();
    }

    /**
     * This method init all instances that {@link CoinbaseTraderBot}'s routine need. <br>
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
        for (CoinbaseAccount coin : coinbaseAccountManager.getCoinbaseWalletsList()){
            double balance = coin.getBalance();
            String symbol = coin.getCurrency();
            boolean isTradingEnabled = balance != 0;
            coins.put(symbol, new Coin(symbol,
                    coinbaseCurrenciesManager.getCurrencyObject(symbol).getName(),
                    balance,
                    isTradingEnabled
            ));
        }
        for (TradingPair tradingPair : coinbaseProductsManager.getAllTradingPairsList())
            tradingPairsList.put(tradingPair.getId(), tradingPair);
        refreshLatestPrice();
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
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
                    balance += coin.getQuantity() * lastPrices.get(coin.getAssetIndex());
            if(!lastBalanceCurrency.contains(USD_CURRENCY)){
                try {
                    balance *= coinbaseProductsManager.getProductTickerObject(getSymbol(currency)).getPrice();
                }catch (Exception ignored){
                }
            }
        }
        return balance;
    }

    /**
     * This method is used by traders to get user Coinbase's wallet balance. <br>
     * @param currency: currency of balance value es. EUR will return balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @param decimals: this indicates number of decimal number after comma es. 3 -> xx,yyy.
     * @return wallet balance in currency value
     * **/
    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        return coinbaseProductsManager.roundValue(getWalletBalance(currency, forceRefresh), decimals);
    }

    /**
     * This method is used to get asset list of Coinbase's user wallet.<br>
     * @param currency: currency of asset balance value es. EUR will return asset balance in EUR currency.
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Asset} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || !lastAssetCurrency.equals(currency) || forceRefresh){
            refreshLatestPrice();
            lastAssetCurrency = currency;
            assets.clear();
            for (Coin coin : coins.values()) {
                if(coin.isTradingEnabled()){
                    String index = coin.getAssetIndex();
                    double quantity = coin.getQuantity();
                    double balance = quantity * lastPrices.get(index);
                    if(!currency.contains(USD_CURRENCY)){
                        try {
                            balance *= coinbaseProductsManager.getProductTickerObject(getSymbol(currency)).getPrice();
                        }catch (Exception ignored){
                        }
                    }
                    assets.add(new Asset(index,
                            coin.getAssetName(),
                            quantity,
                            balance,
                            currency
                    ));
                }
            }
        }
        return assets;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
     * @param dateFormat: this indicates the format of date that you want to have es. HH:mm:ss -> 21:22:08
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if(isRefreshTime() || forceRefresh){
            allTransactions.clear();
            for (String quoteCurrency : quoteCurrencies)
                allTransactions.addAll(getTransactionsList(quoteCurrency, dateFormat, forceRefresh));
            transactions.clear();
        }
        return allTransactions;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from all {@link #quoteCurrencies} inserted.<br>
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
            transactions.clear();
            lastTransactionCurrency = quoteCurrency;
            ArrayList<Order> orders = coinbaseOrdersManager.getAllOrdersList(1000, CREATED_AT_SORTER, ASC_SORTING_ORDER,
                    STATUS_DONE);
            String date;
            for (Coin coin : coins.values()){
                if(coin.isTradingEnabled()){
                    String symbol = coin.getAssetIndex() + "-" + lastTransactionCurrency;
                    for (Order order : orders){
                        if(order.getProductId().equals(symbol)){
                            String createdAt = order.getCreatedAt();
                            if(dateFormat != null) {
                                long timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                        .parse((createdAt)).getTime();
                                date = new SimpleDateFormat(dateFormat).format(new Date(timestamp));
                            } else
                                date = createdAt;
                            transactions.add(new Transaction(symbol,
                                    order.getSide(),
                                    date,
                                    order.getExecutedValue(),
                                    order.getSize()
                            ));
                        }
                    }
                }
            }
        }
        return transactions;
    }

    /**
     * This method is used to get all transactions for a Coinbase's account from a single symbol<br>
     * @param quoteCurrency: this indicates the symbol from fetch details es. BTC will fetch all transactions on Bitcoin
     * @param forceRefresh: this flag when is set to true will refresh prices also if is not time to refresh it.
     * @return list of custom object {@link Transaction} as {@link ArrayList}
     * **/
    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        return getTransactionsList(quoteCurrency, null, forceRefresh);
    }

    /**
     * This method is used to send a buy market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(!symbol.contains("-"))
            symbol = getOrderSymbol(symbol);
        placeAnOrder(symbol, quantity, BUY_SIDE);
        int statusCode = coinbaseOrdersManager.getStatusResponse();
        if(statusCode == 200){
            String[] index = symbol.split("-");
            Coin coin = coins.get(index[0]);
            insertQuoteCurrency(index[1]);
            insertCoin(index[0], coin.getAssetName(), coin.getQuantity() + quantity);
        }else{
            throw new Exception("Error during buy order status code: [" + statusCode + "]" +
                    " error message: [" + coinbaseOrdersManager.getErrorResponse() + "]");
        }
    }

    /**
     * This method is used to send a sell market order<br>
     * @param symbol: this indicates the symbol for the order es. BTC-USDT
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if(!symbol.contains("-"))
            symbol = getOrderSymbol(symbol);
        String[] index = symbol.split("-");
        Coin coin = coins.get(index[0]);
        if(coin != null){
            placeAnOrder(symbol, quantity, SELL_SIDE);
            int statusCode = coinbaseOrdersManager.getStatusResponse();
            if(statusCode == 200){
                double newQuantity = coin.getQuantity() - quantity;
                if(newQuantity == 0)
                    coins.remove(index[0]);
                else
                    insertCoin(index[0], coin.getAssetName(), newQuantity);
            }else{
                throw new Exception("Error during sell order status code: [" + statusCode + "]" +
                        " error message: [" + coinbaseOrdersManager.getErrorResponse() + "]");
            }
        }else
            throw new Exception("Your wallet doesn't have this coin to sell [" + symbol + "]");
    }

    /**
     * This method is used to check and eventually correct symbol inserted for the order
     * @param actualSymbol: correct form would be base-quote, but if is basequote check algorithm start. es. BTC-USDT
     * @return if is correct return {@code actualSymbol}, if not symbol inserted in the right form es BTCUSDT -> BTC-USDT
     * @implSpec if symbol is wrong and not exists in Coinbase listing order request will throw an {@link Exception}
     * **/
    protected String getOrderSymbol(String actualSymbol){
        for (TradingPair tradingPair : tradingPairsList.values()) {
            String id = tradingPair.getId();
            if(id.replace("-", "").equals(actualSymbol))
                return id;
        }
        return actualSymbol;
    }

    /**
     * This method is used to place an order
     * @param symbol: this indicates the symbol for the order es. BTC-BUSD
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * @param side: this indicates the side of the order (BUY or SELL)
     * **/
    @Override
    public void placeAnOrder(String symbol, double quantity, String side) throws Exception {
        coinbaseOrdersManager.createMarketOrderSize(side, symbol, quantity);
    }

    /**
     * This method is used to insert or update a coin in {@link #coins} list.
     * @param symbol: symbol of the coin es. BTC
     * @param name: name of the coin es Bitcoin
     * @param quantity: quantity of that coin es. 0.28
     * **/
    @Override
    protected void insertCoin(String symbol, String name, double quantity) {
        coins.put(symbol, new Coin(symbol,
                name,
                quantity,
                true
        ));
    }

    /**
     * This method is used fetch details of a Coinbase's order request<br>
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
        return coinbaseAccountManager.getErrorResponse();
    }

    /**
     * This method is used to refresh latest prices<br>
     * Any params required
     * **/
    @Override
    protected void refreshLatestPrice() throws Exception {
        if(isRefreshTime()){
            lastPricesRefresh = System.currentTimeMillis();
            for (String productId : tradingPairsList.keySet()) {
                String[] productIds = productId.split("-");
                try {
                    if(productIds[1].equals(USD_CURRENCY) && coins.get(productIds[0]).isTradingEnabled())
                        lastPrices.put(productIds[0], coinbaseProductsManager.getProductTickerObject(productId).getPrice());
                }catch (JSONException ignored){
                }
            }
        }
    }

    /**
     * This method is used to assemble right symbol with {@link #USDT_CURRENCY} combination.
     * @param currency: currency for the request es. EUR
     * @return USDT-currency inserted es. USDT-EUR.
     * **/
    protected String getSymbol(String currency){
        return USDT_CURRENCY + "-" + currency;
    }

}

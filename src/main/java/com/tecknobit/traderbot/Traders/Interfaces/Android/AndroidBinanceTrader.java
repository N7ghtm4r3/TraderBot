package com.tecknobit.traderbot.Traders.Interfaces.Android;

import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Account.TraderDetails;
import com.tecknobit.traderbot.Records.Android.Routine;
import com.tecknobit.traderbot.Records.Portfolio.Asset;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.Android.AndroidCoreRoutines;
import com.tecknobit.traderbot.Routines.Android.ServerRequest;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;

import java.util.ArrayList;

import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static com.tecknobit.traderbot.Records.Account.TraderDetails.BINANCE_TRADER_PLATFORM;
import static com.tecknobit.traderbot.Routines.Android.ServerRequest.*;
import static java.lang.Integer.parseInt;
import static java.lang.Math.toIntExact;

public class AndroidBinanceTrader extends BinanceTraderBot implements AndroidCoreRoutines {

    private final TraderDetails traderDetails;
    private final TraderAccount traderAccount;
    private final Credentials credentials;
    private final String authToken;
    private final String token;
    private final String ivSpec;
    private final String secretKey;

    /**
     * {@code printRoutineMessages} is instance that memorize flag to insert to print or not routine messages
     * **/
    private boolean printRoutineMessages;

    /**
     * {@code runningTrader} is instance that memorize flag that indicates if the trader is running
     * **/
    private boolean runningTrader;

    /**
     * {@code baseCurrency} is instance that memorize base currency to get all amount value of traders routine es. EUR
     * **/
    private String baseCurrency;

    public AndroidBinanceTrader(String apiKey, String secretKey, Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey);
        this.credentials = credentials;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, Credentials credentials,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.credentials = credentials;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkCredentialsValidity(credentials);
        this.credentials = credentials;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.credentials = credentials;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.credentials = credentials;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, Credentials credentials, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.credentials = credentials;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.credentials = credentials;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    public AndroidBinanceTrader(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                Credentials credentials, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.credentials = credentials;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        long timestamp = System.currentTimeMillis();
        traderDetails = new TraderDetails(timestamp, TRADER_TYPE_MANUAL, RUNNING_TRADER_STATUS, BINANCE_TRADER_PLATFORM,
                toIntExact(REFRESH_PRICES_TIME), timestamp);
        initCredentials();
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        this.secretKey = credentials.getSecretKey();
        traderAccount = new TraderAccount(new ServerRequest(ivSpec, this.secretKey, authToken, token));
        workflowHandler();
    }

    @Override
    protected void initTrader() throws Exception {
        printAndroidDisclaimer();
        super.initTrader();
    }

    @Override
    public void initCredentials() throws Exception {
        checkCredentialsValidity(credentials);
        if(credentials.getToken() == null) {
            credentials.setTraderDetails(traderDetails);
            credentials.sendRegistrationRequest();
        }
    }

    @Override
    public void workflowHandler() {
        runningTrader = true;
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        performRoutines();
                        sleep(2000);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    @Override
    public void performRoutines() throws Exception {
        for (Routine routine : getRoutines(new ServerRequest(ivSpec, secretKey, authToken, token))){
            switch (routine.getRoutine()){
                case CHANGE_MAIL_OPE:
                    credentials.setMail(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_MAIL_OPE + "] Mail successfully changed", true);
                    break;
                case CHANGE_PASSWORD_OPE:
                    credentials.setPassword(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_PASSWORD_OPE + "] Password successfully changed", true);
                    break;
                case CHANGE_REFRESH_TIME_PRICES_OPE:
                    setRefreshPricesTime(parseInt(routine.getExtraValue()));
                    printOperationStatus("[" + CHANGE_REFRESH_TIME_PRICES_OPE + "] Refresh prices time successfully changed",
                            true);
                    break;
                case CHANGE_TRADER_STATUS_OPE:
                    printOperationStatus("[" + CHANGE_TRADER_STATUS_OPE + "] Trader status successfully changed",
                            true);
                    if(routine.getExtraValue().equals(STOPPED_TRADER_STATUS)){
                        disableTrader();
                        printOperationStatus("Trader status: [" + STOPPED_TRADER_STATUS + "]", false);
                    }else{
                        enableTrader();
                        printOperationStatus("Trader status: [" + RUNNING_TRADER_STATUS+ "]", true);
                    }
                    break;
                case CHANGE_CURRENCY_OPE:
                    setBaseCurrency(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_CURRENCY_OPE + "] Base currency successfully changed",
                            true);
                    break;
                case INSERT_QUOTE_OPE:
                    insertQuoteCurrency(routine.getExtraValue());
                    printOperationStatus("[" + INSERT_QUOTE_OPE + "] Base currency successfully inserted",
                            true);
                    break;
                case REMOVE_QUOTE_OPE:
                    removeQuoteCurrency(routine.getExtraValue());
                    printOperationStatus("[" + REMOVE_QUOTE_OPE + "] Base currency successfully removed",
                            true);
            }
        }
    }

    @Override
    public void printOperationStatus(String msg, boolean greenPrint) {
        if(printRoutineMessages){
            if(greenPrint)
                printGreen(msg);
            else
                printRed(msg);
        }
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getWalletBalance(currency, forceRefresh);
        return -1;
    }

    @Override
    public double getWalletBalance(String currency, boolean forceRefresh, int decimals) throws Exception {
        if(runningTrader)
            return super.getWalletBalance(currency, forceRefresh, decimals);
        return -1;
    }

    @Override
    public ArrayList<Asset> getAssetsList(String currency, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAssetsList(currency, forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(dateFormat, forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getAllTransactions(boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getAllTransactions(forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, String dateFormat, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, dateFormat, forceRefresh);
        return null;
    }

    @Override
    public ArrayList<Transaction> getTransactionsList(String quoteCurrency, boolean forceRefresh) throws Exception {
        if(runningTrader)
            return super.getTransactionsList(quoteCurrency, forceRefresh);
        return null;
    }

    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        if(runningTrader)
            super.buyMarket(symbol, quantity);
    }

    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        if(runningTrader)
            super.sellMarket(symbol, quantity);
    }

    @Override
    protected void insertCoin(String index, String name, double quantity) {
        super.insertCoin(index, name, quantity);
    }

    @Override
    public void setRefreshPricesTime(int refreshPricesTime) {
        super.setRefreshPricesTime(refreshPricesTime);
        traderDetails.setRefreshPricesTime(refreshPricesTime);
    }

    @Override
    public boolean isTraderRunning() {
        return runningTrader;
    }

    @Override
    public void disableTrader() {
        runningTrader = false;
        traderDetails.setTraderStatus(STOPPED_TRADER_STATUS);
    }

    @Override
    public void enableTrader() {
        runningTrader = true;
        traderDetails.setTraderStatus(RUNNING_TRADER_STATUS);
    }

    @Override
    public double getSalesAtLoss() {
        if(runningTrader)
            return traderAccount.getSalesAtLoss();
        return -1;
    }

    @Override
    public double getSalesAtGain() {
        if(runningTrader)
            return traderAccount.getSalesAtGain();
        return -1;
    }

    @Override
    public double getSalesInPair() {
        if(runningTrader)
            return traderAccount.getSalesAtPair();
        return -1;
    }

    @Override
    public double getTotalSales() {
        if(runningTrader)
            return traderAccount.getTotalSales();
        return -1;
    }

    @Override
    public void setBaseCurrency(String baseCurrency) {
        if(baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USDT");
        this.baseCurrency = baseCurrency;
    }

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
        return credentials;
    }

    /**
     * This method is used to set flag to print routine messages
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        this.printRoutineMessages = printRoutineMessages;
    }

    /**
     * This method is used to get flag to print or not routine messages
     * @return flag that indicates the possibility or not to print or not routine messages
     * **/
    @Override
    public boolean canPrintRoutineMessages() {
        return printRoutineMessages;
    }

}

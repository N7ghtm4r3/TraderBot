package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.binancemanager.Managers.BinanceManager;
import com.tecknobit.binancemanager.Managers.Market.Records.Filter;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.traderbot.Helpers.Orders.MarketOrder;
import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Portfolio.Coin;
import com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency;
import com.tecknobit.traderbot.Records.Portfolio.Transaction;
import com.tecknobit.traderbot.Routines.Autonomous.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.Candlestick.*;
import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.ExchangeInformation.Symbol;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.*;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;

/**
 * The {@code BinanceAutoTraderBot} class is trader for {@link BinanceManager} library.<br>
 * This trader bot allow to manage user wallet, get transactions and make orders (BUY and SELL side) for a Binance's account autonomously.<br>
 * Is derived class of {@code BinanceTraderBot} class from inherit all core routines methods and instances.
 * @implNote for autonomous operations use {@link AutoTraderCoreRoutines} and {@link MarketOrder} routines.
 * @author Tecknobit N7ghtm4r3
 * **/

public class BinanceAutoTraderBot extends BinanceTraderBot implements AutoTraderCoreRoutines, MarketOrder {

    /**
     * {@code TraderAccount} is instance that memorize and manage account information and trading reports of auto trader
     * account
     * **/
    protected final TraderAccount traderAccount = new TraderAccount(0,0,0,0,0);

    /**
     * {@code MIN_NOTIONAL_FILTER} is instance that contains key for {@code MIN_NOTIONAL} filter
     * **/
    public static final String MIN_NOTIONAL_FILTER = "MIN_NOTIONAL";

    /**
     * {@code LOT_SIZE_FILTER} is instance that contains key for {@code LOT_SIZE} filter
     * **/
    public static final String LOT_SIZE_FILTER = "LOT_SIZE";

    /**
     * {@code walletList} is a map that contains wallet list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * @implNote is used in {@link #buyCryptocurrencies()} and {@link #updateWallet()} routines
     * **/
    private ConcurrentHashMap<String, Cryptocurrency> walletList;

    /**
     * {@code checkingList} is a map that contains checking list assets and index (es. BTCBUSD) as key {@link String} and {@link Cryptocurrency}
     * as value of map.
     * @implNote is used in {@link #checkCryptocurrencies()} routine
     * **/
    private final HashMap<String, Cryptocurrency> checkingList;

    /**
     * {@code tradingConfig} is instance that memorize model of trading to use for trading routines
     * **/
    private TradingConfig tradingConfig;

    /**
     * {@code sendStatsReport} is instance that memorize flag to insert to send or not reports
     * **/
    private boolean sendStatsReport;

    /**
     * {@code printRoutineMessages} is instance that memorize flag to insert to print or not routine messages
     * **/
    private boolean printRoutineMessages;

    /**
     * {@code runningTrader} is instance that memorize flag that indicates if the trader is running
     * **/
    private boolean runningTrader;

    /**
     * {@code previousChecking} is instance that memorize previous timestamp when {@link #checkCryptocurrencies()} is called
     * **/
    private long previousChecking;

    /**
     * {@code previousBuying} is instance that memorize previous timestamp when {@link #buyCryptocurrencies()} is called
     * **/
    private long previousBuying;

    /**
     * {@code previousUpdating} is instance that memorize previous timestamp when {@link #updateWallet()} is called
     * **/
    private long previousUpdating;

    /**
     * {@code baseCurrency} is instance that memorize base currency to get all amount value of traders routine es. EUR
     * **/
    private String baseCurrency;

    /** Constructor to init {@link BinanceAutoTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, boolean sendStatsReport, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /** Constructor to init {@link BinanceAutoTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, boolean sendStatsReport,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, int refreshPricesTime, boolean sendStatsReport,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime,
                                boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param refreshPricesTime: is time in seconds to set for refresh the latest prices
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @throws IllegalArgumentException if {@code refreshPricesTime} value is less than 5(5s) and if is bigger than 3600(1h)
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, boolean sendStatsReport,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /** Constructor to init {@link BinanceTraderBot}
     * @param apiKey: your Binance's api key
     * @param secretKey: your Binance's secret key
     * @param baseEndpoint: base endpoint choose from BinanceManager.BASE_ENDPOINTS array
     * @param quoteCurrencies: is a list of quote currencies used in past orders es (USD or EUR)
     * @param sendStatsReport: flag to insert to send or not reports
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * @implNote these keys will NOT store by libray anywhere.
     * **/
    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
    }

    /**
     * This method is used to init wallet list when autonomous trader is started from second time onwards<br>
     * @param walletList: wallet list of your cryptocurrencies, stored by you <br>
     * Any return
     * **/
    @Override
    public void setWallet(ConcurrentHashMap<String, Cryptocurrency> walletList) {
        if(walletList == null)
            throw new IllegalArgumentException("Wallet cannot be null");
        this.walletList = walletList;
    }

    /**
     * This method is used to start {@link BinanceAutoTraderBot}<br>
     * Any params required
     * @implNote the running mode if is disabled (using {@link #disableTrader()}) trader will not do more trading operations, but trader will continue to listen for reactivation of the running mode
     * (using {@link #enableTrader()}) and trading operations will start again.
     * **/
    @Override
    public void start() {
        runningTrader = true;
        printDisclaimer();
        tradingConfig = fetchTradingConfig();
        previousBuying = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        while (runningTrader){
                            if(makeRoutine(previousChecking, CHECKING_GAP_TIME)) {
                                previousChecking = System.currentTimeMillis();
                                checkCryptocurrencies();
                            }
                            if(makeRoutine(previousBuying, BUYING_GAP_TIME)){
                                previousBuying = System.currentTimeMillis();
                                buyCryptocurrencies();
                            }
                            if(makeRoutine(previousUpdating, UPDATING_GAP_TIME)) {
                                previousUpdating = System.currentTimeMillis();
                                updateWallet();
                            }
                        }
                        System.out.println("Bot is stopped, waiting for reactivation");
                        Thread.sleep(5000);
                     }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * This method is used to check list of possible new cryptocurrencies to buy using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void checkCryptocurrencies() throws Exception {
        System.out.println("## CHECKING NEW CRYPTOCURRENCIES");
        tradingConfig = fetchTradingConfig();
        String candleInterval = INTERVAL_1d;
        int daysGap = tradingConfig.getDaysGap();
        if(daysGap > 2 && daysGap <= 6)
            candleInterval = INTERVAL_3d;
        else if(daysGap > 28)
            candleInterval = INTERVAL_1M;
        for (TickerPriceChange ticker : binanceMarketManager.getTickerPriceChangeList()){
            String symbol = ticker.getSymbol();
            Symbol tradingPair = tradingPairsList.get(symbol);
            String quoteAsset = tradingPair.getQuoteAsset();
            if(quoteCurrencies.isEmpty() || quoteContained(quoteAsset)){
                String baseAsset = tradingPair.getBaseAsset();
                Coin coin = coins.get(baseAsset);
                if(coin != null && !walletList.containsKey(baseAsset)){
                    double priceChangePercent = ticker.getPriceChangePercent();
                    double tptop = isTradable(symbol, tradingConfig, candleInterval, priceChangePercent);
                    if(tptop != ASSET_NOT_TRADABLE) {
                        checkingList.put(baseAsset, new Cryptocurrency(baseAsset,
                                coin.getAssetName(),
                                0,
                                symbol,
                                ticker.getLastPrice(),
                                tptop,
                                candleInterval,
                                priceChangePercent,
                                quoteAsset,
                                tradingConfig
                        ));
                    }else
                        checkingList.remove(baseAsset);
                }
            }
        }
    }

    /**
     * This method is used to check if a {@link Cryptocurrency} when this method is called is respecting correct range gap
     * to be bought using {@link TradingConfig} model.
     * @param symbol: symbol used in checking phase es. BTCBUSD or BTC-USD
     * @param tradingConfig: model of trading to use as {@link TradingConfig}
     * @param candleInterval: interval gap to make forecast
     * @param priceChangePercent: percent gap of the symbol from previous day and day when the symbol is checked
     * @return value of {@link #computeTPTOPIndex(String, TradingConfig, Object, double)} if is correct and return
     * {@link #ASSET_NOT_TRADABLE} if is not respect {@link TradingConfig} model.
     * **/
    @Override
    public double isTradable(String symbol, TradingConfig tradingConfig, Object candleInterval,
                             double priceChangePercent) throws IOException {
        double wasteRange = tradingConfig.getWasteRange();
        if((abs(priceChangePercent - tradingConfig.getMarketPhase()) <= abs(wasteRange)) &&
                (priceChangePercent >= tradingConfig.getMaxLoss() && priceChangePercent <= tradingConfig.getMaxGain())){
            double tptop = computeTPTOPIndex(symbol, tradingConfig, candleInterval, wasteRange);
            if(tptop >= tradingConfig.getMinGainForOrder())
                return tptop;
        }
        return ASSET_NOT_TRADABLE;
    }

    /**
     * This method is used to check if a {@link Cryptocurrency} when this method is called is respecting correct range gap
     * to be bought using {@link TradingConfig} model.
     * @param symbol: symbol used in checking phase es. BTCBUSD or BTC-USD
     * @param tradingConfig: model of trading to use as {@link TradingConfig}
     * @param candleInterval: interval gap to make forecast
     * @return value of tptop index if is correct and return {@link #ASSET_NOT_TRADABLE} if is not respect {@link TradingConfig} model.
     * **/
    @Override
    public double computeTPTOPIndex(String symbol, TradingConfig tradingConfig, Object candleInterval,
                                    double wasteRange) throws IOException {
        return binanceMarketManager.getSymbolForecast(symbol, (String) candleInterval, tradingConfig.getDaysGap(),
                (int) wasteRange);
    }

    /**
     * This method is used to buy new cryptocurrencies from list loaded from {@link #checkCryptocurrencies()} routine
     * using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    @Override
    public void buyCryptocurrencies() throws Exception {
        System.out.println("## BUYING NEW CRYPTOCURRENCIES");
        for (Cryptocurrency cryptocurrency : checkingList.values()){
            String symbol = cryptocurrency.getSymbol();
            double quantity = getMarketOrderQuantity(cryptocurrency);
            if(quantity != -1) {
                try {
                    buyMarket(symbol, quantity);
                    cryptocurrency.setQuantity(quantity);
                    cryptocurrency.addFirstPrice(cryptocurrency.getLastPrice());
                    walletList.put(cryptocurrency.getAssetIndex(), cryptocurrency);
                    if(printRoutineMessages)
                        System.out.println("Buying [" + symbol + "], quantity: " + quantity);
                }catch (Exception e){
                    printError(symbol, e);
                }
            }
        }
        checkingList.clear();
        if(printRoutineMessages) {
            System.out.println("### Transactions");
            for (Transaction transaction : getAllTransactions(true))
                transaction.printDetails();
        }
    }

    /**
     * This method is used to routine of update wallet of cryptocurrencies bought by auto trader. If {@link Cryptocurrency}
     * respects {@link TradingConfig} model that {@link Cryptocurrency} will be sold. <br>
     * Any params required
     * **/
    @Override
    public void updateWallet() throws Exception {
        System.out.println("## UPDATING WALLET CRYPTOCURRENCIES");
        if(walletList.size() > 0){
            refreshLatestPrice();
            for (Cryptocurrency cryptocurrency : walletList.values()) {
                String symbol = cryptocurrency.getSymbol();
                TradingConfig tradingConfig = cryptocurrency.getTradingConfig();
                TickerPriceChange tickerPriceChange = lastPrices.get(symbol);
                double lastPrice = tickerPriceChange.getLastPrice();
                double incomePercent = binanceMarketManager.getTrendPercent(cryptocurrency.getFirstPrice(), lastPrice);
                double minGainOrder = tradingConfig.getMinGainForOrder();
                double tptopIndex = cryptocurrency.getTptopIndex();
                refreshCryptoDetails(cryptocurrency, incomePercent, lastPrice, tickerPriceChange.getPriceChangePercent());
                try {
                    if(incomePercent < minGainOrder && incomePercent < tptopIndex){
                        if(printRoutineMessages)
                            System.out.println("Refreshing [" + symbol + "]");
                    }else if(incomePercent <= tradingConfig.getMaxLoss())
                        incrementSalesSale(cryptocurrency, LOSS_SELL);
                    else if(incomePercent >= minGainOrder || incomePercent >= tptopIndex)
                        incrementSalesSale(cryptocurrency, GAIN_SELL);
                    else
                        incrementSalesSale(cryptocurrency, PAIR_SELL);
                }catch (Exception e){
                    printError(symbol, e);
                }
            }
        }
        if(printRoutineMessages){
            System.out.println("### Wallet");
            for (Cryptocurrency cryptocurrency : walletList.values())
                cryptocurrency.printDetails();
            System.out.println("## Balance amount: " + getWalletBalance(baseCurrency, false, 2)
                    + " " + baseCurrency);
        }
    }

    /**
     * This method is used to increment sales detail
     * @param cryptocurrency: cryptocurrency used in the order
     * @param codeOpe: code of type of sell to increment
     * **/
    @Override
    public void incrementSalesSale(Cryptocurrency cryptocurrency, int codeOpe) throws Exception {
        sellMarket(cryptocurrency.getSymbol(), cryptocurrency.getQuantity());
        walletList.remove(cryptocurrency.getAssetIndex());
        switch (codeOpe){
            case LOSS_SELL:
                traderAccount.addLoss();
                if(printRoutineMessages) {
                    System.out.println(ANSI_RED + "## Selling at loss [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextIncomePercent(2) +  "]" + ANSI_RESET);
                }
                break;
            case GAIN_SELL:
                traderAccount.addGain();
                if(printRoutineMessages) {
                    System.out.println(ANSI_GREEN + "## Selling at gain [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextIncomePercent(2) +  "]" + ANSI_RESET);
                }
                break;
            default:
                traderAccount.addPair();
                if(printRoutineMessages) {
                    System.out.println("## Selling at pair [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextIncomePercent() +  "]");
                }
        }
        traderAccount.addIncome(cryptocurrency.getIncomePercent(2));
        if(printRoutineMessages)
            traderAccount.printDetails();
    }

    /**
     * This method is used to send a buy market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to buy es. 10
     * **/
    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        super.buyMarket(symbol, quantity);
        if(sendStatsReport)
            sendStatsReport(/*params for report*/);
    }

    /**
     * This method is used to send a sell market order from a Binance SPOT account.<br>
     * @param symbol: this indicates the symbol for the order es. BTCBUSD
     * @param quantity: this indicates quantity of that symbol is wanted to sell es. 10
     * **/
    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        super.sellMarket(symbol, quantity);
        if(sendStatsReport)
            sendStatsReport(/*params for report*/);
    }

    /**
     * This method is used to set flag to send stats report with {@link #sendStatsReport()} method
     * @param sendStatsReport: flag to insert to send or not reports
     * **/
    @Override
    public void setSendStatsReport(boolean sendStatsReport) {
        this.sendStatsReport = sendStatsReport;
    }

    /**
     * This method is used to get flag to send stats report with {@link #sendStatsReport()} method <br>
     * @return flag that indicates the possibility or not to send stats reports
     * **/
    @Override
    public boolean canSendStatsReport() {
        return sendStatsReport;
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

    /**
     * This method is used to get if bot is in running mode
     * @return flag that indicates if the bot is running
     * **/
    @Override
    public boolean isTraderRunning() {
        return runningTrader;
    }

    /**
     * This method is used to disable running mode of bot
     * **/
    @Override
    public void disableTrader() {
        runningTrader = false;
    }

    /**
     * This method is used to enable running mode of bot
     * **/
    @Override
    public void enableTrader(){
        runningTrader = true;
    }

    /**
     * This method is used to get quantity for market order type <br>
     * @param cryptocurrency: cryptocurrency as {@link Cryptocurrency} used in the order
     * @return quantity for the market order es. 1
     * **/
    @Override
    public double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception {
        String symbol = cryptocurrency.getSymbol();
        Symbol exchangeInformation = binanceMarketManager.getObjectExchangeInformation(symbol).getSymbols().get(0);
        double stepSize = 0, maxQty = 0, minQty = 0, minNotional = 0, quantity = -1;
        double coinBalance = getCoinBalance(cryptocurrency.getQuoteAsset());
        if(coinBalance != -1){
            for (Filter filter : exchangeInformation.getFiltersList()) {
                if(filter.getFilterType().equals(LOT_SIZE_FILTER)){
                    JSONObject lotSize = filter.getFilterDetails().getJSONObject(LOT_SIZE_FILTER);
                    stepSize = lotSize.getDouble("stepSize");
                    maxQty = lotSize.getDouble("maxQty");
                    minQty = lotSize.getDouble("minQty");
                }else if(filter.getFilterType().equals(MIN_NOTIONAL_FILTER)) {
                    minNotional = filter.getFilterDetails().getJSONObject(MIN_NOTIONAL_FILTER)
                            .getDouble("minNotional");
                    break;
                }
            }
            double minNotionalQty = minNotional / cryptocurrency.getLastPrice();
            if(coinBalance == minNotional)
                quantity = ceil(minNotional);
            else if(coinBalance > minNotional){
                double difference = coinBalance - minNotional;
                quantity = difference * cryptocurrency.getTptopIndex() / 100;
                if(quantity < minQty)
                    quantity = minQty;
                else if(quantity > maxQty)
                    quantity = maxQty;
                else {
                    if ((quantity - minQty) % stepSize != 0) {
                        quantity = ceil(quantity);
                    }
                    if(quantity < (minNotionalQty)) {
                        quantity = ceil(minNotionalQty + 1);
                    }
                }
            }
        }
        return quantity;
    }

    /**
     * This method is used to get coin balance
     * @param quote: string of quote currency to return amount value of balance
     * @return balance of coin inserted
     * **/
    @Override
    public double getCoinBalance(String quote) {
        Coin coin = coins.get(quote);
        String assetIndex = coin.getAssetIndex();
        double quantity = coin.getQuantity();
        if(assetIndex.equals(USDT_CURRENCY)){
            try {
                return binanceMarketManager.roundValue(quantity *
                        binanceMarketManager.getCurrentAveragePriceValue(BUSD_CURRENCY + USDT_CURRENCY), 8);
            } catch (IOException e) {
                return -1;
            }
        }
        return binanceMarketManager.roundValue(quantity *
                lastPrices.get(assetIndex + USDT_CURRENCY).getLastPrice(), 8);
    }

    /**
     * This method is used to get sales at loss
     * @return sales at loss
     * **/
    @Override
    public double getSalesAtLoss() {
        return traderAccount.getSalesAtLoss();
    }

    /**
     * This method is used to get sales at gain
     * @return sales at gain
     * **/
    @Override
    public double getSalesAtGain() {
        return traderAccount.getSalesAtGain();
    }

    /**
     * This method is used to get sales at pair
     * @return sales at pair
     * **/
    @Override
    public double getSalesInPair() {
        return traderAccount.getSalesAtPair();
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * **/
    @Override
    public double getTotalSales() {
        return traderAccount.getTotalSales();
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
     * **/
    @Override
    public String getBaseCurrency() {
        return baseCurrency;
    }

}

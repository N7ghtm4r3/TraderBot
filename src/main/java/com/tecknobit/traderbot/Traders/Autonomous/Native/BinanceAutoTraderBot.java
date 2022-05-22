package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.binancemanager.Managers.Market.Records.Filter;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.traderbot.Helpers.Orders.MarketOrder;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Cryptocurrency;
import com.tecknobit.traderbot.Records.Transaction;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Autonomous.Utils.AutoTraderBotAccount;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.Candlestick.*;
import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.ExchangeInformation.Symbol;
import static com.tecknobit.traderbot.Routines.RoutineMessages.*;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;

public class BinanceAutoTraderBot extends BinanceTraderBot implements AutoTraderCoreRoutines, MarketOrder {

    protected final AutoTraderBotAccount autoTraderBotAccount = new AutoTraderBotAccount();
    public static final String MIN_NOTIONAL_FILTER = "MIN_NOTIONAL";
    public static final String LOT_SIZE_FILTER = "LOT_SIZE";
    private final ConcurrentHashMap<String, Cryptocurrency> walletList;
    private final HashMap<String, Cryptocurrency> checkingList;
    private TradingConfig tradingConfig;
    private boolean sendStatsReport;
    private boolean printRoutineMessages;
    private boolean runningBot;
    private long previousChecking;
    private long previousBuying;
    private long previousUpdating;
    private String baseCurrency;

    public BinanceAutoTraderBot(String apiKey, String secretKey, boolean sendStatsReport, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, boolean sendStatsReport,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, int refreshPricesTime, boolean sendStatsReport,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, int refreshPricesTime,
                                boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, int refreshPricesTime,
                                boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport, boolean printRoutineMessages,
                                String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies, boolean sendStatsReport,
                                boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                boolean sendStatsReport, boolean printRoutineMessages, String baseCurrency) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        this.printRoutineMessages = printRoutineMessages;
        this.baseCurrency = baseCurrency;
        checkingList = new HashMap<>();
        walletList = new ConcurrentHashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    @Override
    public void start() {
        tradingConfig = fetchTradingConfig();
        previousBuying = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        while (runningBot){
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
                    double lastPrice = ticker.getLastPrice();
                    double priceChangePercent = ticker.getPriceChangePercent();
                    double tptop = isTradable(symbol, tradingConfig, candleInterval, lastPrice, priceChangePercent);
                    if(tptop != ASSET_NOT_TRADABLE) {
                        checkingList.put(baseAsset, new Cryptocurrency(baseAsset,
                                coin.getAssetName(),
                                0,
                                symbol,
                                lastPrice,
                                tptop,
                                candleInterval,
                                priceChangePercent,
                                quoteAsset,
                                tradingConfig)
                        );
                    }
                }
            }
        }
    }

    @Override
    public void buyCryptocurrencies() throws Exception {
        System.out.println("## BUYING NEW CRYPTOCURRENCIES");
        for (Cryptocurrency cryptocurrency : checkingList.values()){
            String symbol = cryptocurrency.getSymbol();
            double lastPrice = cryptocurrency.getLastPrice();
            double tptop = isTradable(symbol, cryptocurrency.getTradingConfig(), cryptocurrency.getCandleGap(),
                    lastPrice, cryptocurrency.getPriceChangePercent());
            if(tptop != ASSET_NOT_TRADABLE) {
                double quantity = getMarketOrderQuantity(cryptocurrency);
                if(quantity != -1) {
                    try {
                        buyMarket(symbol, quantity);
                        cryptocurrency.setQuantity(quantity);
                        cryptocurrency.setFirstPrice(lastPrice);
                        walletList.put(cryptocurrency.getAssetIndex(), cryptocurrency);
                        if(printRoutineMessages)
                            System.out.println("Buying [" + symbol + "], quantity: " + quantity);
                    }catch (Exception e){
                        printError(symbol, e);
                    }
                }
            }
        }
        checkingList.clear();
        if(printRoutineMessages)
            for (Transaction transaction : getAllTransactions(true))
                transaction.printDetails();
    }

    @Override
    public double isTradable(String index, TradingConfig tradingConfig, Object candleInterval, double lastPrice,
                             double priceChangePercent) throws IOException {
        double wasteRange = tradingConfig.getWasteRange();
        if((abs(priceChangePercent - tradingConfig.getMarketPhase()) <= abs(wasteRange)) ||
                (priceChangePercent >= tradingConfig.getMaxLoss() && priceChangePercent <= tradingConfig.getMaxGain())){
            double tptop = computeTPTOPIndex(index, tradingConfig, candleInterval, wasteRange);
            if(tptop >= tradingConfig.getGainForOrder())
                return tptop;
        }
        return ASSET_NOT_TRADABLE;
    }

    @Override
    public double computeTPTOPIndex(String index, TradingConfig tradingConfig, Object candleInterval,
                                    double wasteRange) throws IOException {
        return binanceMarketManager.getSymbolForecast(index, (String) candleInterval, tradingConfig.getDaysGap(),
                (int) wasteRange);
    }

    @Override
    public void updateWallet() throws Exception {
        System.out.println("## UPDATING WALLET CRYPTOCURRENCIES");
        refreshLatestPrice();
        for (Cryptocurrency cryptocurrency : walletList.values()) {
            String symbol = cryptocurrency.getSymbol();
            TradingConfig tradingConfig = cryptocurrency.getTradingConfig();
            double lastPrice = lastPrices.get(symbol);
            double trendPercent = binanceMarketManager.getTrendPercent(cryptocurrency.getFirstPrice(), lastPrice);
            refreshCryptoDetails(cryptocurrency, trendPercent, lastPrice);
            try {
                if(trendPercent < tradingConfig.getGainForOrder() && trendPercent < cryptocurrency.getTptopIndex()){
                    if(printRoutineMessages)
                        System.out.println("Refreshing [" + symbol + "]");
                }else if(trendPercent <= tradingConfig.getMaxLoss())
                    incrementSellsSale(cryptocurrency, LOSS_SELL);
                else if(trendPercent >= tradingConfig.getGainForOrder() || trendPercent >= cryptocurrency.getTptopIndex())
                    incrementSellsSale(cryptocurrency, GAIN_SELL);
                else
                    incrementSellsSale(cryptocurrency, PAIR_SELL);
            }catch (Exception e){
                printError(symbol, e);
            }
        }
        if(printRoutineMessages){
            for (Cryptocurrency cryptocurrency : walletList.values())
                cryptocurrency.printDetails();
            System.out.println("## Balance amount: " + getWalletBalance(baseCurrency, true, 2)
                    + " " + baseCurrency);
        }
    }

    @Override
    public void incrementSellsSale(Cryptocurrency cryptocurrency, int codeOpe) throws Exception {
        sellMarket(cryptocurrency.getSymbol(), cryptocurrency.getQuantity());
        walletList.remove(cryptocurrency.getAssetIndex());
        switch (codeOpe){
            case LOSS_SELL:
                autoTraderBotAccount.addLoss();
                if(printRoutineMessages) {
                    System.out.println(ANSI_RED + "## Selling at loss [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextTrendPercent(2) +  "]" + ANSI_RESET);
                }
                break;
            case GAIN_SELL:
                autoTraderBotAccount.addGain();
                if(printRoutineMessages) {
                    System.out.println(ANSI_GREEN + "## Selling at gain [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextTrendPercent(2) +  "]" + ANSI_RESET);
                }
                break;
            default:
                autoTraderBotAccount.addPair();
                if(printRoutineMessages) {
                    System.out.println("## Selling at pair [" + cryptocurrency.getSymbol() + "], " +
                            "income: [" + cryptocurrency.getTextTrendPercent() +  "]");
                }
        }
        if(printRoutineMessages)
            autoTraderBotAccount.printDetails();
    }

    @Override
    public void buyMarket(String symbol, double quantity) throws Exception {
        super.buyMarket(symbol, quantity);
        if(sendStatsReport)
            sendStatsReport(/*params for report*/);
    }

    @Override
    public void sellMarket(String symbol, double quantity) throws Exception {
        super.sellMarket(symbol, quantity);
        if(sendStatsReport)
            sendStatsReport(/*params for report*/);
    }

    @Override
    public void setSendStatsReport(boolean sendStatsReport) {
        this.sendStatsReport = sendStatsReport;
    }

    @Override
    public boolean canSendStatsReport() {
        return sendStatsReport;
    }

    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        this.printRoutineMessages = printRoutineMessages;
    }

    @Override
    public boolean canPrintRoutineMessages() {
        return printRoutineMessages;
    }

    @Override
    public boolean isRunningBot() {
        return runningBot;
    }

    @Override
    public void disableBot() {
        runningBot = false;
    }

    @Override
    public void enableBot(){
        runningBot = true;
    }

    @Override
    public double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception {
        String symbol = cryptocurrency.getSymbol();
        Symbol exchangeInformation = binanceMarketManager.getObjectExchangeInformation(symbol).getSymbols().get(0);
        double stepSize = 0, maxQty = 0, minQty = 0, minNotional = 0, quantity = -1;
        double lastPrice = cryptocurrency.getLastPrice();
        double coinBalance = getCoinBalance(lastPrice, cryptocurrency.getQuoteAsset());
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
        double minNotionalQty = minNotional / lastPrice;
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
        return quantity;
    }

    // TODO: 21/05/2022 SET NORMAL METHOD ROUTINE
    @Override
    public double getCoinBalance(double lastPrice, String quote) {
        /*Coin coin = coins.get(quote);
        return binanceMarketManager.roundValue(coin.getQuantity() *
                lastPrices.get(coin.getAssetIndex() + USDT_CURRENCY), 8);*/
        return 100;
    }

    @Override
    public double getSellsAtLoss() {
        return autoTraderBotAccount.getSellsAtLoss();
    }

    @Override
    public double getSellsAtGain() {
        return autoTraderBotAccount.getSellsAtGain();
    }

    @Override
    public double getSellsInPair() {
        return autoTraderBotAccount.getSellsAtPair();
    }

    @Override
    public double getTotalSells() {
        return autoTraderBotAccount.getTotalSells();
    }

    @Override
    public void setBaseCurrency(String baseCurrency) {
        if(baseCurrency == null || baseCurrency.isEmpty())
            throw new IllegalArgumentException("Currency cannot be null or empty, but for example EUR or USDT");
        this.baseCurrency = baseCurrency;
    }

    @Override
    public String getBaseCurrency() {
        return baseCurrency;
    }

    private void printError(String symbol, Exception e){
        if(binanceSpotManager.getStatusResponse() != 200)
            System.out.println(getErrorResponse() + " on [" + symbol + "]");
        else
            e.printStackTrace();
    }

}

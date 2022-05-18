package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Cryptocurrency;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.Candlestick.*;
import static java.lang.Math.abs;

public final class BinanceAutoTraderBot extends BinanceTraderBot implements AutoTraderCoreRoutines {

    private final HashMap<String, Cryptocurrency> checkingList;
    private boolean sendStatsReport;
    private TradingConfig tradingConfig;
    private boolean runningBot;

    public BinanceAutoTraderBot(String apiKey, String secretKey, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, int refreshPricesTime,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                ArrayList<String> quoteCurrencies, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    @Override
    public void start() {
        tradingConfig = fetchTradingConfig();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        while (runningBot){
                            checkCryptocurrencies();
                            buyCryptocurrencies();
                            updateCryptocurrencies();
                        }
                        System.out.println("Bot is stopped, waiting for reactivation");
                        Thread.sleep(5000);
                     }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    @Override
    public void checkCryptocurrencies() throws IOException {
        tradingConfig = fetchTradingConfig();
        String candleInterval = INTERVAL_1d;
        int daysGap = tradingConfig.getDaysGap();
        if(daysGap > 2 && daysGap <= 6)
            candleInterval = INTERVAL_3d;
        else if(daysGap > 28)
            candleInterval = INTERVAL_1M;
        for (TickerPriceChange ticker : binanceMarketManager.getTickerPriceChangeList()){
            String index = ticker.getSymbol();
            Coin coin = coins.get(tradingPairsList.get(index).getBaseAsset());
            if(coin != null && !coins.containsKey(index)){
                double tptop = isTradable(index, tradingConfig, candleInterval, ticker.getPriceChangePercent());
                if(tptop != -1) {
                    checkingList.put(index, new Cryptocurrency(index,
                            ticker.getLastPrice(),
                            tptop,
                            0,
                            candleInterval,
                            tradingConfig)
                    );
                }
            }
        }
    }

    @Override
    public void buyCryptocurrencies() throws Exception {
        for (Cryptocurrency cryptocurrency : checkingList.values()){
            String symbol = cryptocurrency.getSymbol();
            double tptop = isTradable(symbol, cryptocurrency.getTradingConfig(), cryptocurrency.getCandleGap(),
                    cryptocurrency.getLastPrice());
            if(tptop != -1)
                buyMarket(symbol, 0);
            checkingList.remove(symbol);
        }
    }

    @Override
    public void updateCryptocurrencies() {

    }

    @Override
    public double isTradable(String index, TradingConfig tradingConfig, Object candleInterval,
                              double lastPricePercent) throws IOException {
        double wasteRange = tradingConfig.getWasteRange();
        double tptop = binanceMarketManager.getSymbolForecast(index, (String) candleInterval, tradingConfig.getDaysGap(),
                (int) wasteRange);
        if(tptop >= tradingConfig.getGainForOrder() && abs(lastPricePercent - tradingConfig.getMarketPhase()) <= abs(wasteRange))
            return tptop;
        return -1;
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

    public void setSendStatsReport(boolean sendStatsReport) {
        this.sendStatsReport = sendStatsReport;
    }

    public boolean canSendStatsReport() {
        return sendStatsReport;
    }

    public boolean isRunningBot() {
        return runningBot;
    }

    public void disableBot() {
        runningBot = false;
    }

    public void enableBot(){
        runningBot = true;
    }

}

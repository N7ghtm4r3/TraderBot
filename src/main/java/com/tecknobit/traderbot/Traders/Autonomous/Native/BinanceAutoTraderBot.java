package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.Ticker;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class BinanceAutoTraderBot extends BinanceTraderBot implements AutoTraderCoreRoutines {

    private final HashMap<String, Ticker> checkingList;
    private boolean sendStatsReport;
    private boolean runningBot;

    public BinanceAutoTraderBot(String apiKey, String secretKey, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, int refreshPricesTime,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                ArrayList<String> quoteCurrencies, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        runningBot = true;
        checkingList = new HashMap<>();
        printDisclaimer();
    }

    @Override
    public void start() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        while (runningBot){

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

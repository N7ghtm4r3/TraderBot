package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;

import java.util.ArrayList;

public final class BinanceAutoTraderBot extends BinanceTraderBot implements AutoTraderCoreRoutines {

    public BinanceAutoTraderBot(String apiKey, String secretKey) throws Exception {
        super(apiKey, secretKey);
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                ArrayList<String> quoteCurrencies) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        printDisclaimer();
    }

    @Override
    public void start() {
        new Thread(){
            @Override
            public void run() {
                super.run();
            }
        }.start();
    }

}

package com.tecknobit.traderbot.Traders.Autonomous.Native;

import com.tecknobit.binancemanager.Managers.Market.Records.Filter;
import com.tecknobit.binancemanager.Managers.Market.Records.Tickers.TickerPriceChange;
import com.tecknobit.traderbot.Helpers.Orders.MarketOrder;
import com.tecknobit.traderbot.Records.Coin;
import com.tecknobit.traderbot.Records.Cryptocurrency;
import com.tecknobit.traderbot.Routines.AutoTraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Interfaces.Native.BinanceTraderBot;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.Candlestick.*;
import static com.tecknobit.binancemanager.Managers.Market.Records.Stats.ExchangeInformation.Symbol;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;

public class BinanceAutoTraderBot extends BinanceTraderBot implements AutoTraderCoreRoutines, MarketOrder {

    public static final String LOT_SIZE_FILTER = "LOT_SIZE";
    public static final String MIN_NOTIONAL_FILTER = "MIN_NOTIONAL";
    private final HashMap<String, Cryptocurrency> checkingList;
    private final HashMap<String, Cryptocurrency> walletList;
    private boolean sendStatsReport;
    private TradingConfig tradingConfig;
    private boolean runningBot;

    public BinanceAutoTraderBot(String apiKey, String secretKey, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, int refreshPricesTime,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint, ArrayList<String> quoteCurrencies,
                                int refreshPricesTime, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies, refreshPricesTime);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, ArrayList<String> quoteCurrencies,
                                boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
        runningBot = true;
        printDisclaimer();
    }

    public BinanceAutoTraderBot(String apiKey, String secretKey, String baseEndpoint,
                                ArrayList<String> quoteCurrencies, boolean sendStatsReport) throws Exception {
        super(apiKey, secretKey, baseEndpoint, quoteCurrencies);
        this.sendStatsReport = sendStatsReport;
        checkingList = new HashMap<>();
        walletList = new HashMap<>();
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
    public void checkCryptocurrencies() throws Exception {
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
                if(coin != null && !walletList.containsKey(symbol)){
                    double lastPrice = ticker.getLastPrice();
                    double priceChangePercent = ticker.getPriceChangePercent();
                    double tptop = isTradable(symbol, tradingConfig, candleInterval, lastPrice, priceChangePercent);
                    if(tptop != NOT_ASSET_TRADABLE) {
                        checkingList.put(symbol, new Cryptocurrency(baseAsset,
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
        for (Cryptocurrency cryptocurrency : checkingList.values()){
            String symbol = cryptocurrency.getSymbol();
            double tptop = isTradable(symbol, cryptocurrency.getTradingConfig(), cryptocurrency.getCandleGap(),
                    cryptocurrency.getLastPrice(), cryptocurrency.getPriceChangePercent());
            if(tptop != NOT_ASSET_TRADABLE) {
                double quantity = getMarketOrderQuantity(cryptocurrency);
                if(quantity != -1) {
                    buyMarket(symbol, quantity);
                    cryptocurrency.setQuantity(quantity);
                    walletList.put(symbol, cryptocurrency);
                }
            }
        }
        checkingList.clear();
    }

    @Override
    public void updateCryptocurrencies() {

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
        return NOT_ASSET_TRADABLE;
    }

    @Override
    public double computeTPTOPIndex(String index, TradingConfig tradingConfig, Object candleInterval,
                                    double wasteRange) throws IOException {
        return binanceMarketManager.getSymbolForecast(index, (String) candleInterval, tradingConfig.getDaysGap(),
                (int) wasteRange);
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
        for (Filter filter : exchangeInformation.getFiltersList()) {;
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
        if(coinBalance == minNotional)
            quantity = ceil(minNotional / lastPrice);
        else if(coinBalance > minNotional){
            double difference = coinBalance - minNotional;
            quantity = difference * cryptocurrency.getTptopIndex() / 100;
            if(quantity < minQty)
                quantity = minQty;
            else if(quantity > maxQty)
                quantity = maxQty;
            else
                if((quantity - minQty) % stepSize != 0)
                    quantity = ceil(quantity);
        }
        return quantity;
    }

    @Override
    public double getCoinBalance(double lastPrice, String quote) {
        Coin coin = coins.get(quote);
        return binanceMarketManager.roundValue(coin.getQuantity() *
                lastPrices.get(coin.getAssetIndex() + USDT_CURRENCY), 8);
    }

}

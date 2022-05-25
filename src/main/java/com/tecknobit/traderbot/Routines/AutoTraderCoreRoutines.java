package com.tecknobit.traderbot.Routines;

import com.tecknobit.traderbot.Records.Cryptocurrency;

/**
 * The {@code AutoTraderCoreRoutines} interface defines base routines methods for auto traders bot.<br>
 * These routines allow the different auto traders to do some operations about wallet info and make trading operations autonomously.
 * @author Tecknobit N7ghtm4r3
 * **/

public interface AutoTraderCoreRoutines {

    /**
     * {@code CHECKING_GAP_TIME} is instance that memorize time gap for checking routine
     * **/
    long CHECKING_GAP_TIME = 10000L;//60000*5L;

    /**
     * {@code BUYING_GAP_TIME} is instance that memorize time gap for buying routine
     * **/
    long BUYING_GAP_TIME = 1800*1000L;

    /**
     * {@code UPDATING_GAP_TIME} is instance that memorize time gap for updating routine
     * **/
    long UPDATING_GAP_TIME = 10000L;

    /**
     * {@code ASSET_NOT_TRADABLE} is code to indicate an asset that is not tradable because is not in parameters range
     * **/
    int ASSET_NOT_TRADABLE = -999;

    /**
     * {@code LOSS_SELL} is code to indicate a sell in losing
     * **/
    int LOSS_SELL = 0;

    /**
     * {@code GAIN_SELL} is code to indicate a sell in gaining
     * **/
    int GAIN_SELL = 1;

    /**
     * {@code GAIN_SELL} is code to indicate a sell in pairing
     * **/
    int PAIR_SELL = 2;

    /**
     * The {@code TradingConfig} class is useful for trading operation.<br>
     * Represent model to use for a {@link Cryptocurrency} in trading phases. (BUY and SELL)
     * @author Tecknobit N7ghtm4r3
     * **/
    final class TradingConfig{

        /**
         * {@code marketPhase} is instance that memorize market phase when buy a {@link Cryptocurrency}
         * **/
        private final double marketPhase;

        /**
         * {@code wasteRange} is instance that memorize waste range gap to buy and to make forecast for {@link Cryptocurrency}
         * **/
        private final double wasteRange;

        /**
         * {@code daysGap} is instance that memorize days gap to make forecast for {@link Cryptocurrency}
         * **/
        private final int daysGap;

        /**
         * {@code minGainForOrder} is instance that memorize minimum gain to obtain by an order. This is used in sell phase.
         * **/
        private final double minGainForOrder;

        /**
         * {@code maxLoss} is instance that memorize maximum loss for a {@link Cryptocurrency} and is used in buy phase to check <br>
         * if a cryptocurrency is in correct range to be bought, and is used in sell phase to sell when cryptocurrency is dropping.
         * **/
        private final double maxLoss;

        /**
         * {@code maxGain} is instance that memorize maximum gain for a {@link Cryptocurrency} and is used in buy phase to check
         * if a cryptocurrency is in correct range to be bought.
         * **/
        private final double maxGain;

        /** Constructor to init {@link TradingConfig}
         * @param marketPhase: market phase when buy a {@link Cryptocurrency}
         * @param wasteRange: waste range gap to buy and to make forecast for {@link Cryptocurrency}
         * @param daysGap: days gap to make forecast for {@link Cryptocurrency}
         * @param minGainForOrder: minimum gain to obtain by an order. This is used in sell phase.
         * @param maxLoss: maximum loss for a {@link Cryptocurrency}
         * @param maxGain: maximum gain for a {@link Cryptocurrency} in checking phase
         * **/
        public TradingConfig(double marketPhase, double wasteRange, int daysGap, double minGainForOrder, double maxLoss,
                             double maxGain) {
            this.marketPhase = marketPhase;
            this.wasteRange = wasteRange;
            this.daysGap = daysGap;
            this.minGainForOrder = minGainForOrder;
            this.maxLoss = maxLoss;
            this.maxGain = maxGain;
        }

        public double getMarketPhase() {
            return marketPhase;
        }

        public double getWasteRange() {
            return wasteRange;
        }

        public int getDaysGap() {
            return daysGap;
        }

        public double getMinGainForOrder() {
            return minGainForOrder;
        }

        public double getMaxLoss() {
            return maxLoss;
        }

        public double getMaxGain() {
            return maxGain;
        }

    }

    /**
     * This method is used to print disclaimer alert to warn user of terms and conditions using our services.
     * Is used in autonomous traders and in Android's interfaces. <br>
     * Any params required
     * **/
    default void printDisclaimer(){
        System.out.println("""
                ############################### DISCLAIMER ALERT #################################\s
                ## This is an auto trader bot made by Tecknobit and all rights are deserved.    ##\s
                ## By deciding to use this auto trader bot, you declare that you are aware of   ##\s
                ## the possible risks associated with the world of crypto investments and how   ##\s
                ## there may be possible losses, large or small, due to market trends. You also ##\s
                ## declare that you are aware that the bot developed by us may not reach your   ##\s
                ## expectations in terms of earnings and that you are solely responsible for    ##\s
                ## the decision to use this bot for your investments and that, therefore,       ##\s
                ## Tecknobit and the product that you have choose to use, are excluded from all ##\s
                ## liability arising from such investments. If you do not want to accept these  ##\s
                ## terms, even in the case of a long time from its start, you are free to       ##\s
                ## terminate the operation of the bot without being attributed any fee and      ##\s
                ## that your personal data entered will not be saved by us at any stage of      ##\s
                ## operation of that bot, neither from launch neither to its termination.       ##\s
                ## If you accept to send results data of investments you will contribute to     ##\s
                ## make grew up this A.I for automatic investments and make it more efficiently ##\s
                ## The data that will be sent will be only about trend percentage, time gap     ##\s
                ## from buy to sell and gain or loss percentage when that asset sold and some   ##\s
                ## market indexes, but ANY personal data that you inserted and ANY data about   ##\s
                ## assets that bot bought during his operations will be sent.                   ##\s
                ## You will be able to enable or disable this stats report anytime during all   ##\s
                ## lifecycle of the bot like as you want.                                       ##\s
                ##                                 Good use!                                    ##\s
                ##                         Copyright © 2022 Tecknobit                           ##\s
                ##################################################################################
                """);
    }

    /**
     * This method is used to obtain model of trading from server.
     * Any params required
     * @return model of trading as {@link TradingConfig} object
     * **/
    default TradingConfig fetchTradingConfig(){
        // TODO: 22/05/2022 request to fetch trading
        return new TradingConfig(1,
                3,
                29,
                0.00001,
                -0.01,
                2
        );
    }

    /**
     * This method is used to check if is time to do some routine. <br>
     * Routines that have to check time are: {@link #checkCryptocurrencies()}, {@link #buyCryptocurrencies()}
     * and {@link #updateWallet()}
     * @param previousGap: past timestamp when routine is made
     * @param gap: time gap of routine
     * @return true if is time to make a routine of autonomous trader.
     * **/
    default boolean makeRoutine(long previousGap, long gap){
        return (System.currentTimeMillis() - previousGap) >= gap;
    }

    /**
     * This method is used to start autonomous trader<br>
     * Any params required
     * **/
    void start();

    /**
     * This method is used to check list of possible new cryptocurrencies to buy using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    void checkCryptocurrencies() throws Exception;

    /**
     * This method is used to buy new cryptocurrencies from list loaded from {@link #checkCryptocurrencies()} routine
     * using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    void buyCryptocurrencies() throws Exception;

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
    double isTradable(String symbol, TradingConfig tradingConfig, Object candleInterval,
                      double priceChangePercent) throws Exception;

    /**
     * This method is used to check if a {@link Cryptocurrency} when this method is called is respecting correct range gap
     * to be bought using {@link TradingConfig} model.
     * @param symbol: symbol used in checking phase es. BTCBUSD or BTC-USD
     * @param tradingConfig: model of trading to use as {@link TradingConfig}
     * @param candleInterval: interval gap to make forecast
     * @return value of tptop index if is correct and return {@link #ASSET_NOT_TRADABLE} if is not respect {@link TradingConfig} model.
     * **/
    double computeTPTOPIndex(String symbol, TradingConfig tradingConfig, Object candleInterval, double wasteRange)
            throws Exception;

    /**
     * This method is used to routine of update wallet of cryptocurrencies bought by auto trader. If {@link Cryptocurrency}
     * respect {@link TradingConfig} model that {@link Cryptocurrency} will be sold. <br>
     * Any params required
     * **/
    void updateWallet() throws Exception;

    /**
     * This method is used to increment sells detail
     * @param cryptocurrency: cryptocurrency used in the order
     * @param codeOpe: code of type of sell to increment
     * **/
    void incrementSellsSale(Cryptocurrency cryptocurrency, int codeOpe) throws Exception;

    /**
     * This method is used to refresh details of {@link Cryptocurrency}
     * @param cryptocurrency: cryptocurrency to refresh
     * @param trendPercent: percent that cryptocurrency is having
     * @param lastPrice: last price of cryptocurrency
     * **/
    default void refreshCryptoDetails(Cryptocurrency cryptocurrency, double trendPercent, double lastPrice){
        cryptocurrency.setTrendPercent(trendPercent);
        cryptocurrency.setLastPrice(lastPrice);
    }

    /**
     * This method is used to send stats report of trading using {@link TradingConfig} fetched.
     * This is very helpful to make A.I. for automatic trader better and more efficiently.
     * @implNote any personal data will be sent in this method and any personal data will be saved in our systems anywhere. Data that will be passed are
     * only data contained in {@link TradingConfig} model.
     * **/
    default void sendStatsReport(/*params*/){
        // TODO: 22/05/2022 request to send report and add to docu
        System.out.println("gagagagag");
        //send data in some methods
    }

    /**
     * This method is used to set flag to send stats report with {@link #sendStatsReport()} method
     * @param sendStatsReport: flag to insert to send or not reports
     * **/
    void setSendStatsReport(boolean sendStatsReport);

    /**
     * This method is used to get flag to send stats report with {@link #sendStatsReport()} method <br>
     * @return flag that indicates the possibility or not to send stats reports
     * **/
    boolean canSendStatsReport();

    /**
     * This method is used to set flag to print routine messages
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    void setPrintRoutineMessages(boolean printRoutineMessages);

    /**
     * This method is used to get flag to print or not routine messages
     * @return flag that indicates the possibility or not to print or not routine messages
     * **/
    boolean canPrintRoutineMessages();

    /**
     * This method is used to get if bot is in running mode
     * @return flag that indicates if the bot is running
     * **/
    boolean isRunningBot();

    /**
     * This method is used to disable running mode of bot
     * **/
    void disableBot();

    /**
     * This method is used to enable running mode of bot
     * **/
    void enableBot();

    /**
     * This method is used to get coin balance
     * @param lastPrice: last price of cryptocurrency
     * @param quote: string of quote currency to return amount value of balance
     * @return balance of coin inserted
     * **/
    double getCoinBalance(double lastPrice, String quote);

    /**
     * This method is used to get sells at loss
     * @return sells at loss
     * **/
    double getSellsAtLoss();

    /**
     * This method is used to get sells at gain
     * @return sells at gain
     * **/
    double getSellsAtGain();

    /**
     * This method is used to get sells at pair
     * @return sells at pair
     * **/
    double getSellsInPair();

    /**
     * This method is used to get total sells
     * @return total sells
     * **/
    double getTotalSells();

    /**
     * This method is used to set base currency for change amount value
     * @param baseCurrency: base currency to get all amount value of traders routine es. EUR
     * **/
    void setBaseCurrency(String baseCurrency);

    /**
     * This method is used to get base currency for change amount value <br>
     * Any params required
     * **/
    String getBaseCurrency();

}

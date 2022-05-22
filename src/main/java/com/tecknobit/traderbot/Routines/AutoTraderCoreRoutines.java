package com.tecknobit.traderbot.Routines;

import com.tecknobit.traderbot.Records.Cryptocurrency;

import java.io.IOException;

public interface AutoTraderCoreRoutines {

    long CHECKING_GAP_TIME = 60000*5L;
    long BUYING_GAP_TIME = 1800*1000L;
    long UPDATING_GAP_TIME = 10000L;
    int ASSET_NOT_TRADABLE = -999;
    int LOSS_SELL = 0;
    int GAIN_SELL = 1;
    int PAIR_SELL = 2;

    final class TradingConfig{

        private final double marketPhase;
        private final double wasteRange;
        private final int daysGap;
        private final double gainForOrder;
        private final double maxLoss;
        private final double maxGain;

        public TradingConfig(double marketPhase, double wasteRange, int daysGap, double gainForOrder, double maxLoss,
                             double maxGain) {
            this.marketPhase = marketPhase;
            this.wasteRange = wasteRange;
            this.daysGap = daysGap;
            this.gainForOrder = gainForOrder;
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

        public double getGainForOrder() {
            return gainForOrder;
        }

        public double getMaxLoss() {
            return maxLoss;
        }

        public double getMaxGain() {
            return maxGain;
        }

    }

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

    default TradingConfig fetchTradingConfig(){
        //request to server for trading confing
        return new TradingConfig(1,
                3,
                29,
                1,
                -4,
                2
        );
    }

    default void sendStatsReport(){
        System.out.println("gagagagag");
        //send data in some methods
    }

    default boolean makeRoutine(long previousGap, long gap){
        return (System.currentTimeMillis() - previousGap) >= gap;
    }

    void start();

    void checkCryptocurrencies() throws Exception;

    void buyCryptocurrencies() throws Exception;

    double isTradable(String index, TradingConfig tradingConfig, Object candleInterval, double lastPrice,
                      double priceChangePercent) throws IOException;

    double computeTPTOPIndex(String index, TradingConfig tradingConfig, Object candleInterval, double wasteRange)
            throws IOException;

    void updateWallet() throws Exception;

    void incrementSellsSale(Cryptocurrency cryptocurrency, int codeOpe) throws Exception;

    void setSendStatsReport(boolean sendStatsReport);

    boolean canSendStatsReport();

    boolean isRunningBot();

    void disableBot();

    void enableBot();

    double getCoinBalance(double lastPrice, String quote);

    double getSellsAtLoss();

    double getSellsAtGain();

    double getSellsInPair();

    double getTotalSells();

}

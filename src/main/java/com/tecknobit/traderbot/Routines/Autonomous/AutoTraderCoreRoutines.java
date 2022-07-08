package com.tecknobit.traderbot.Routines.Autonomous;

import com.tecknobit.traderbot.Records.Account.TraderAccount;
import com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency;
import com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.TradingConfig;
import com.tecknobit.traderbot.Routines.Android.ServerRequest;
import com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

import static com.tecknobit.traderbot.Records.Account.Trader.TraderManager;
import static com.tecknobit.traderbot.Records.Portfolio.Cryptocurrency.TradingConfig.*;
import static com.tecknobit.traderbot.Routines.Android.ServerRequest.*;

/**
 * The {@code AutoTraderCoreRoutines} interface defines base routines methods for auto traders bot.<br>
 * These routines allow the different auto traders to do some operations about wallet info and make trading operations autonomously.
 * @author Tecknobit N7ghtm4r3
 * **/

public interface AutoTraderCoreRoutines extends TraderManager, RoutineMessages {

    /**
     * {@code CHECKING_GAP_TIME} is instance that memorize time gap for checking routine
     * **/
    long CHECKING_GAP_TIME = 60000*5L;

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
     * This method is used to print disclaimer alert to warn user of terms and conditions using our services. <br>
     * It is printed in autonomous traders. <br>
     * Any params required
     * **/
    default void printDisclaimer(){
        System.out.println("############################### DISCLAIMER ALERT #################################\n" +
                           "## This is an auto trader bot made by Tecknobit and all rights are reserved.    ##\n" +
                           "## By deciding to use this auto trader bot, you declare that you are aware of   ##\n" +
                           "## the possible risks associated with the world of crypto investments and how   ##\n" +
                           "## there may be possible losses, large or small, due to market trends. You also ##\n" +
                           "## expectations in terms of earnings and that you are solely responsible for    ##\n" +
                           "## the decision to use this bot for your investments and that, therefore,       ##\n" +
                           "## Tecknobit and the product that you have choose to use, are excluded from all ##\n" +
                           "## liability arising from such investments. If you do not want to accept these  ##\n" +
                           "## terms, even in the case of a long time from its start, you are free to       ##\n" +
                           "## terminate the operation of the bot without being attributed any fee and      ##\n" +
                           "## that your personal data entered will not be saved by us at any stage of      ##\n" +
                           "## operation of that bot, neither from launch neither to its termination.       ##\n" +
                           "## If you accept to send results data of investments you will contribute to     ##\n" +
                           "## make grew up this A.I for automatic investments and make it more efficiently ##\n" +
                           "## The data that will be sent will be only about trend percentage, time gap     ##\n" +
                           "## from buy to sell and gain or loss percentage when that asset sold and some   ##\n" +
                           "## market indexes, but ANY personal data that you inserted and ANY data about   ##\n" +
                           "## assets that bot bought during his operations will be sent.                   ##\n" +
                           "## You will be able to enable or disable this stats report anytime during all   ##\n" +
                           "## lifecycle of the bot like as you want.                                       ##\n" +
                           "##                                 Good use!                                    ##\n" +
                           "##                         Copyright © 2022 Tecknobit                           ##\n" +
                           "##################################################################################");
    }

    /**
     * This method is used to obtain model of trading from server.
     * Any params required
     * @return model of trading as {@link TradingConfig} object
     * **/
    default TradingConfig fetchTradingConfig(TradingConfig actualModel){
        try {
            ServerRequest serverRequest = getPublicRequest();
            assert serverRequest != null;
            serverRequest.sendRequest(new JSONObject(), GET_TRADING_CONFIGS_OPE);
            response = serverRequest.readResponse();
            if(response != null) {
                return new TradingConfig(response.getLong(MODEL_ID_KEY),
                        response.getDouble(MARKET_PHASE_KEY),
                        response.getDouble(WASTE_RANGE_KEY),
                        response.getInt(DAIS_GAP_KEY),
                        response.getDouble(MIN_GAIN_FOR_ORDER_KEY),
                        response.getDouble(MAX_LOSS_KEY),
                        response.getDouble(MAX_GAIN_KEY)
                );
            }else
                return actualModel;
        } catch (Exception e) {
            printRed("Error during fetching new Trading Config model, will be use the old model");
            return actualModel;
        }
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
     * This method is used to init wallet list when autonomous trader is started from second time onwards<br>
     * @param walletList: wallet list of your cryptocurrencies, stored by you <br>
     * Any return
     * **/
    void setWallet(ConcurrentHashMap<String, Cryptocurrency> walletList);

    /**
     * This method is used to start autonomous trader<br>
     * Any params required
     * **/
    void start() throws Exception;

    /**
     * This method is used to check list of possible new cryptocurrencies to buy using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    void checkCryptocurrencies() throws Exception;

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
     * This method is used to buy new cryptocurrencies from list loaded from {@link #checkCryptocurrencies()} routine
     * using {@link TradingConfig} model. <br>
     * Any params required
     * **/
    void buyCryptocurrencies() throws Exception;

    /**
     * This method is used to routine of update wallet of cryptocurrencies bought by auto trader. If {@link Cryptocurrency}
     * respect {@link TradingConfig} model that {@link Cryptocurrency} will be sold. <br>
     * Any params required
     * **/
    void updateWallet() throws Exception;

    /**
     * This method is used to increment sales detail
     * @param cryptocurrency: cryptocurrency used in the order
     * @param codeOpe: code of type of sell to increment
     * **/
    void incrementSalesSale(Cryptocurrency cryptocurrency, String codeOpe) throws Exception;

    /**
     * This method is used to refresh details of {@link Cryptocurrency}
     * @param cryptocurrency: cryptocurrency to refresh
     * @param incomePercent: percent that cryptocurrency is having
     * @param lastPrice: last price of cryptocurrency
     * **/
    default void refreshCryptoDetails(Cryptocurrency cryptocurrency, double incomePercent, double lastPrice,
                                      double priceChangePercent){
        cryptocurrency.setLastPrice(lastPrice);
        cryptocurrency.setPriceChangePercent(priceChangePercent);
    }

    /**
     * This method is used to get coin balance
     * @param quote: string of quote currency to return amount value of balance
     * @return balance of coin inserted
     * **/
    double getCoinBalance(String quote);

    /**
     * This method is used to send stats report of trading using {@link TradingConfig} fetched.
     * This is very helpful to make A.I. for automatic trader better and more efficiently.
     * @implNote any personal data will be sent in this method and any personal data will be saved in our systems anywhere. Data that will be passed are
     * only data contained in {@link TradingConfig} model.
     * **/
    default void sendStatsReport(long modelId, double percent){
        try {
            ServerRequest serverRequest = getPublicRequest();
            assert serverRequest != null;
            serverRequest.sendRequest(new JSONObject().put(MODEL_ID_KEY, modelId).put(MODEL_FAILED_KEY, percent <= 0),
                    SEND_STATS_REPORT_OPE);
            if(response != null){
                if(response.getInt(STATUS_CODE) == SUCCESSFUL_RESPONSE)
                    printGreen("Stats report successfully sent");
                else
                    printRed("Error during stats report");
            }
        }catch (Exception e){
            printRed("Error during stats report");
        }
    }

    /**
     * This method is used to set flag to send stats report with {@link #sendStatsReport(long, double)} method
     * @param sendStatsReport: flag to insert to send or not reports
     * **/
    void setSendStatsReport(boolean sendStatsReport);

    /**
     * This method is used to get flag to send stats report with {@link #sendStatsReport(long, double)} method <br>
     * @return flag that indicates the possibility or not to send stats reports
     * **/
    boolean canSendStatsReport();

    /**
     * This method is used to get trader account object
     * @return trader account as {@link TraderAccount} object
     * **/
    TraderAccount getTraderAccount();

    /**
     * This method is used to get wallet list
     * @return wallet list as {@link ConcurrentHashMap} object of {@link Cryptocurrency}
     * **/
    ConcurrentHashMap<String, Cryptocurrency> getWallet();

}
package com.tecknobit.traderbot.Traders;

import com.tecknobit.binancemanager.Exceptions.SystemException;
import com.tecknobit.binancemanager.Managers.Market.BinanceMarketManager;
import com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.BinanceWalletManager;
import com.tecknobit.traderbot.Routines.TradersCoreRoutines;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.Records.AccountSnapshots.AccountSnapshot.*;

public class BinanceTraderBot extends TradersCoreRoutines {

    private final BinanceWalletManager binanceWalletManager;
    private final BinanceMarketManager binanceMarketManager;

    public BinanceTraderBot(String apiKey, String secretKey) throws SystemException, IOException {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey);
        binanceMarketManager = new BinanceMarketManager();
    }

    public BinanceTraderBot(String apiKey, String secretKey, String baseEndpoint) throws SystemException, IOException {
        binanceWalletManager = new BinanceWalletManager(apiKey, secretKey, baseEndpoint);
        binanceMarketManager = new BinanceMarketManager();
    }

    public double getWalletBalance(String typeAccount, String currency) throws Exception {
        if(typeAccount != null && (typeAccount.equals(SPOT) || typeAccount.equals(MARGIN) || typeAccount.equals(FUTURES))){
            JSONObject balance = getLastBalance(typeAccount).getJSONObject("data");
            return balance.getDouble("totalAssetOfBtc") * 
                    binanceMarketManager.getCurrentAveragePriceValue("BTC" + currency);
        }else
            throw new IllegalArgumentException("Type of account must be SPOT, MARGIN or FUTURES account type");
    }

    public double getWalletBalance(String typeAccount, String currency, int decimals) throws Exception {
        return binanceMarketManager.roundValue(getWalletBalance(typeAccount, currency), decimals);
    }

    /*public <T> ArrayList<T> getAssetsList() throws Exception {
        System.out.println(binanceWalletManager.getObjectConvertibleBNBAssets().assetsDetails().get(0).asset());
        return null;
    }

    public <T> ArrayList<T> getTransactionsList() {
        return null;
    }

    public <T> T getAccountDetails() {
        return null;
    }*/

    @Override
    public String lastBalanceUpdate() throws Exception {
        return new Date(getLastBalance(SPOT).getLong("updateTime")).toString();
    }

    @Override
    public String getErrorResponse() {
        return binanceWalletManager.getErrorResponse();
    }

    private JSONObject getLastBalance(String typeAccount) throws Exception {
        JSONArray balances = binanceWalletManager.getJSONAccountSnapshot(typeAccount).getJSONArray("snapshotVos");
        return balances.getJSONObject(balances.length()-1);
    }

}

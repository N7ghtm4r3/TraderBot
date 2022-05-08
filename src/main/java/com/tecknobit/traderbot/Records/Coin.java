package com.tecknobit.traderbot.Records;

import static com.tecknobit.binancemanager.Managers.SignedManagers.Wallet.Records.AccountSnapshots.AccountSnapshotSpot.BalancesSpot;

public final class Coin extends BalancesSpot {

    private final String name;
    private final boolean tradingEnabled;

    public Coin(boolean tradingEnabled, String asset, double free, double locked, String name) {
        super(asset, free, locked);
        this.tradingEnabled = tradingEnabled;
        this.name = name;
    }

    public boolean isTradingEnabled() {
        return tradingEnabled;
    }

    public String getName() {
        return name;
    }

}

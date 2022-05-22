package com.tecknobit.traderbot.Traders.Autonomous.Utils;

import java.util.Date;

public class AutoTraderBotAccount {

    private int sellsAtLoss;
    private int sellsAtGain;
    private int sellsAtPair;
    private final String activationDate;

    public AutoTraderBotAccount(int sellsAtLoss, int sellsAtGain, int sellsAtPair) {
        this.sellsAtLoss = sellsAtLoss;
        this.sellsAtGain = sellsAtGain;
        this.sellsAtPair = sellsAtPair;
        this.activationDate = fetchActivationDate();
    }

    public AutoTraderBotAccount() {
        this.activationDate = fetchActivationDate();
    }

    public int getSellsAtLoss() {
        return sellsAtLoss;
    }

    public void addLoss(){
        sellsAtLoss++;
    }

    public int getSellsAtGain() {
        return sellsAtGain;
    }

    public void addGain(){
        sellsAtGain++;
    }

    public int getSellsAtPair() {
        return sellsAtPair;
    }

    public void addPair(){
        sellsAtPair++;
    }

    public int getTotalSells(){
        return sellsAtLoss + sellsAtGain + sellsAtPair;
    }

    public String getActivationDate() {
        return activationDate;
    }

    private String fetchActivationDate(){
        // TODO: 22/05/2022 REQUEST TO GET ACTIVATION DATE
        return new Date(System.currentTimeMillis()).toString();
    }

}

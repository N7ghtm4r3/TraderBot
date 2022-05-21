package com.tecknobit.traderbot.Traders.Autonomous.Utils;

public class AutoTraderBotAccount {

    private int sellsAtLoss;
    private int sellsAtGain;
    private int sellsAtPair;

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

}

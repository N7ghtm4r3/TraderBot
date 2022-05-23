package com.tecknobit.traderbot.Traders.Autonomous.Utils;

import com.tecknobit.traderbot.Routines.RoutineMessages;

import java.util.ArrayList;
import java.util.Date;

import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

public final class AutoTraderBotAccount implements RoutineMessages {

    private int sellsAtLoss;
    private int sellsAtGain;
    private int sellsAtPair;
    private final String activationDate;
    private final ArrayList<Double> incomes;

    public AutoTraderBotAccount(int sellsAtLoss, int sellsAtGain, int sellsAtPair) {
        this.sellsAtLoss = sellsAtLoss;
        this.sellsAtGain = sellsAtGain;
        this.sellsAtPair = sellsAtPair;
        this.activationDate = fetchActivationDate();
        incomes = new ArrayList<>();
    }

    public AutoTraderBotAccount() {
        this.activationDate = fetchActivationDate();
        incomes = new ArrayList<>();
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

    public ArrayList<Double> getIncomes() {
        return incomes;
    }

    public void addIncome(double newIncome){
        if(newIncome < -100)
            throw new IllegalArgumentException("Income value cannot be less than -100");
        incomes.add(newIncome);
    }

    public void printIncomes() {
        for (double income : incomes)
            out.print(getIncomeColorLine("## Income: ", income));
    }

    public double getTotalIncome(){
        double totalIncome = 0;
        for (double income : incomes)
            totalIncome += income;
        return tradingTools.roundValue(totalIncome / incomes.size(), 2);
    }

    public String textualizeTotalIncome(){
        return tradingTools.textualizeAssetPercent(getTotalIncome());
    }

    @Override
    public void printDetails() {
        out.println("## [ ACCOUNT STATUS ]\n" +
                "## Total: " + getTotalSells() + "\n" +
                "## At loss: " + ANSI_RED + sellsAtLoss + ANSI_RESET + "\n" +
                "## At gain: " + ANSI_GREEN + sellsAtGain + ANSI_RESET +"\n" +
                "## At pair: " + sellsAtPair + "\n" +
                getIncomeColorLine("## Total income: ", getTotalIncome()) +
                "######################");
    }

    private String getIncomeColorLine(String tail, double income){
        String textIncome = tradingTools.textualizeAssetPercent(income);
        if(textIncome.contains("+"))
            return tail + ANSI_GREEN + textIncome + ANSI_RESET + "\n";
        else if(textIncome.contains("-"))
            return tail + ANSI_RED + textIncome + ANSI_RESET + "\n";
        else
            return tail + textIncome + "\n";
    }

}

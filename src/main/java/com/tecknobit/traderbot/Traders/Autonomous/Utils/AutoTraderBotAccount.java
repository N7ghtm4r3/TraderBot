package com.tecknobit.traderbot.Traders.Autonomous.Utils;

import com.tecknobit.traderbot.Routines.RoutineMessages;

import java.util.ArrayList;
import java.util.Date;

import static com.tecknobit.traderbot.Routines.TraderCoreRoutines.tradingTools;
import static java.lang.System.out;

/**
 * The {@code AutoTraderBotAccount} class is useful to contains reports of trading. <br>
 * Is useful for autonomous traders and Android's traders.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class AutoTraderBotAccount implements RoutineMessages {

    /**
     * {@code sellsAtLoss} is instance that memorize sells at loss for account
     * **/
    private int sellsAtLoss;

    /**
     * {@code sellsAtGain} is instance that memorize sells at gain for account
     * **/
    private int sellsAtGain;

    /**
     * {@code sellsAtGain} is instance that memorize sells at pair for account
     * **/
    private int sellsAtPair;

    /**
     * {@code activationDate} is instance that memorize activation date for account
     * **/
    private final String activationDate;

    /**
     * {@code incomes} is instance that memorize list of incomes from orders
     * **/
    private final ArrayList<Double> incomes;

    /** Constructor to init {@link AutoTraderBotAccount}
     * @param sellsAtLoss: sells at loss for account
     * @param sellsAtGain: sells at gain for account
     * @param sellsAtPair: sells at pair for account
     * **/
    public AutoTraderBotAccount(int sellsAtLoss, int sellsAtGain, int sellsAtPair) {
        this.sellsAtLoss = sellsAtLoss;
        this.sellsAtGain = sellsAtGain;
        this.sellsAtPair = sellsAtPair;
        this.activationDate = fetchActivationDate();
        incomes = new ArrayList<>();
    }

    /** Constructor to init {@link AutoTraderBotAccount} <br>
     * Any params required
     * **/
    public AutoTraderBotAccount() {
        this.activationDate = fetchActivationDate();
        incomes = new ArrayList<>();
    }

    /**
     * This method is used to get sells at loss
     * @return sells at loss
     * **/
    public int getSellsAtLoss() {
        return sellsAtLoss;
    }

    /**
     * This method is used to increment sells at loss<br>
     * Any params required
     * **/
    public void addLoss(){
        sellsAtLoss++;
    }

    /**
     * This method is used to get sells at gain
     * @return sells at gain
     * **/
    public int getSellsAtGain() {
        return sellsAtGain;
    }

    /**
     * This method is used to add increment sells at gain <br>
     * Any params required
     * **/
    public void addGain(){
        sellsAtGain++;
    }

    /**
     * This method is used to get sells at pair
     * @return sells at loss
     * **/
    public int getSellsAtPair() {
        return sellsAtPair;
    }

    /**
     * This method is used to increment sells at pair <br>
     * Any params required
     * **/
    public void addPair(){
        sellsAtPair++;
    }

    /**
     * This method is used to get total sells
     * @return total sells
     * **/
    public int getTotalSells(){
        return sellsAtLoss + sellsAtGain + sellsAtPair;
    }

    /**
     * This method is used to get activation date of account <br>
     * Any params required
     * @return {@link #activationDate} as activation date of account as {@link String}
     * **/
    public String getActivationDate() {
        return activationDate;
    }

    /**
     * This method is used to fetch and init activation date of account <br>
     * Any params required
     * @return activation date of account as {@link String}
     * **/
    private String fetchActivationDate(){
        // TODO: 22/05/2022 REQUEST TO GET ACTIVATION DATE
        return new Date(System.currentTimeMillis()).toString();
    }

    public ArrayList<Double> getIncomes() {
        return incomes;
    }

    /**
     * This method is used to add increment in {@link #incomes} list
     * @param newIncome: new income to insert
     * **/
    public void addIncome(double newIncome){
        if(newIncome < -100)
            throw new IllegalArgumentException("Income value cannot be less than -100");
        incomes.add(newIncome);
    }

    /**
     * This method is used to print all income from {@link #incomes} list <br>
     * Any params required
     * **/
    public void printIncomes() {
        for (double income : incomes)
            out.print(getIncomeColorLine("## Income: ", income));
    }

    /**
     * This method is used to calculate general profit of account
     * Any params required
     * @return general income of account
     * **/
    public double getTotalIncome(){
        double totalIncome = 0;
        for (double income : incomes)
            totalIncome += income;
        return tradingTools.roundValue(totalIncome / incomes.size(), 2);
    }

    /**
     * This method is used to calculate general profit of account
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted with decimals digits
     * **/
    public double getTotalIncome(int decimals){
        return tradingTools.roundValue(getTotalIncome(), decimals);
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #getTotalIncome()}
     * Any params required
     * @return general income of account formatted as {@link String} es. +1.674% or -1.756%
     * **/
    public String textualizeTotalIncome(){
        return tradingTools.textualizeAssetPercent(getTotalIncome());
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #getTotalIncome()}
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted as {@link String} es. +1.67% or -1.75%
     * **/
    public String textualizeTotalIncome(int decimals){
        return tradingTools.textualizeAssetPercent(getTotalIncome(decimals));
    }

    /**
     * This method is used to print details of {@link AutoTraderBotAccount} object <br>
     * Any params required
     * **/
    @Override
    public void printDetails() {
        out.println("### [ ACCOUNT STATUS ]\n" +
                "## Total: " + getTotalSells() + "\n" +
                "## At loss: " + ANSI_RED + sellsAtLoss + ANSI_RESET + "\n" +
                "## At gain: " + ANSI_GREEN + sellsAtGain + ANSI_RESET +"\n" +
                "## At pair: " + sellsAtPair + "\n" +
                getIncomeColorLine("## Total income: ", getTotalIncome()) +
                "######################");
    }

    /**
     * This method is used to format colored string for income detail line.<br>
     * @param tail: text to indicate the info es. Income:
     * @param income: value of income es. +8%
     * @return percent string colored and formatted as {@link String}
     * **/
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

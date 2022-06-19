package com.tecknobit.traderbot.Records.Account;

import com.tecknobit.traderbot.Routines.Android.ServerRequest;
import com.tecknobit.traderbot.Routines.Interfaces.RecordDetails;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.tecknobit.traderbot.Routines.Android.ServerRequest.*;
import static com.tecknobit.traderbot.Routines.Autonomous.AutoTraderCoreRoutines.ASSET_NOT_TRADABLE;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.*;
import static com.tecknobit.traderbot.Routines.Interfaces.TraderCoreRoutines.tradingTools;
import static java.lang.System.*;
import static java.lang.System.out;

/**
 * The {@code TraderAccount} class is useful to contains reports of trading. <br>
 * Is useful for autonomous traders and Android's traders.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class TraderAccount extends Trader implements RecordDetails {

    /**
     * {@code ACTIVATION_DATE_KEY} is instance that memorize activation date key
     * **/
    public static final String ACTIVATION_DATE_KEY = "activation_date";

    /**
     * {@code GAINS_KEY} is instance that memorize sales at gain key
     * **/
    public static final String GAINS_KEY = "sales_at_gain";

    /**
     * {@code LOSSES_KEY} is instance that memorize sales at loss key
     * **/
    public static final String LOSSES_KEY = "sales_at_loss";

    /**
     * {@code PAIRS_KEY} is instance that memorize sales at pair key
     * **/
    public static final String PAIRS_KEY = "sales_at_pair";

    /**
     * {@code TOTAL_INCOME_KEY} is instance that memorize total income key
     * **/
    public static final String TOTAL_INCOME_KEY = "total_income";

    /**
     * {@code INCOMES_KEY} is instance that memorize incomes key
     * **/
    public static final String INCOMES_KEY = "incomes";

    /**
     * {@code salesAtLoss} is instance that memorize sales at loss for account
     * **/
    private int salesAtLoss;

    /**
     * {@code salesAtGain} is instance that memorize sales at gain for account
     * **/
    private int salesAtGain;

    /**
     * {@code salesAtPair} is instance that memorize sales at pair for account
     * **/
    private int salesAtPair;

    /**
     * {@code activationDate} is instance that memorize activation date for account
     * **/
    private long activationDate;

    /**
     * {@code incomes} is instance that memorize list of incomes from orders
     * **/
    private ArrayList<Double> incomes;

    /**
     * {@code totalIncome} is instance that memorize total income for account
     * **/
    private double totalIncome;

    /**
     * {@code serverRequest} is instance that memorize server request for {@code Android's interfaces}
     * **/
    private final ServerRequest serverRequest;

    /** Constructor to init {@link TraderAccount}
     * @param salesAtLoss: sales at loss for account
     * @param salesAtGain: sales at gain for account
     * @param salesAtPair: sales at pair for account
     * @param activationDate: date when trader has been activated
     * @param totalIncome: total income gained for account
     * **/
    public TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, long activationDate, double totalIncome) {
        this.salesAtLoss = salesAtLoss;
        this.salesAtGain = salesAtGain;
        this.salesAtPair = salesAtPair;
        this.activationDate = activationDate;
        this.totalIncome = totalIncome;
        serverRequest = null;
        initTimeFormatters();
    }

    /** Constructor to init {@link TraderAccount}
     * @param salesAtLoss: sales at loss for account
     * @param salesAtGain: sales at gain for account
     * @param salesAtPair: sales at pair for account
     * @param activationDate: date when trader has been activated
     * @param incomes: list of past incomes
     * **/
    public TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, long activationDate,
                         ArrayList<Double> incomes) {
        this.salesAtLoss = salesAtLoss;
        this.salesAtGain = salesAtGain;
        this.salesAtPair = salesAtPair;
        this.activationDate = activationDate;
        this.incomes = incomes;
        serverRequest = null;
        initTimeFormatters();
    }

    /** Constructor to init {@link TraderAccount} <br>
     * Any params required
     * **/
    public TraderAccount(ServerRequest serverRequest) throws Exception {
        this.serverRequest = serverRequest;
        serverRequest.sendTokenRequest(new JSONObject(), GET_TRADER_ACCOUNT_OPE);
        response = serverRequest.readResponse();
        if(response != null) {
            if(response.getInt(STATUS_CODE) == SUCCESSFUL_RESPONSE){
                salesAtLoss = response.getInt(LOSSES_KEY);
                salesAtGain = response.getInt(GAINS_KEY);
                salesAtPair = response.getInt(PAIRS_KEY);
                activationDate = response.getLong(ACTIVATION_DATE_KEY);
                incomes = new ArrayList<>();
                JSONArray incomesList = response.getJSONArray(INCOMES_KEY);
                for (int j=0; j < incomesList.length(); j++)
                    incomes.add(incomesList.getDouble(j));
                totalIncome = ASSET_NOT_TRADABLE;
                initTimeFormatters();
            }else
                throw new IllegalAccessException("Operation failed");
        }
    }

    /** Constructor to init {@link TraderAccount} <br>
     * @implNote this constructor must call to init autonomous traders at their first start, in next starts you will
     * have to call {@link #TraderAccount(int salesAtLoss, int salesAtGain, int salesAtPair, long activationDate, ArrayList incomes)}
     * constructor with your data from you stored to start normal flow of autonomous traders
     * **/
    public TraderAccount() {
        salesAtLoss = 0;
        salesAtGain = 0;
        salesAtPair = 0;
        activationDate = currentTimeMillis();
        totalIncome = 0;
        serverRequest = null;
        incomes = new ArrayList<>();
        initTimeFormatters();
    }

    /**
     * This method is used to get sales at loss
     * @return sales at loss
     * **/
    public int getSalesAtLoss() {
        return salesAtLoss;
    }

    /**
     * This method is used to increment sales at loss<br>
     * Any params required
     * **/
    public void addLoss(){
        salesAtLoss++;
        if(serverRequest != null){
            // TODO: 19/06/2022  request to add loss
        }
    }

    /**
     * This method is used to get sales at gain
     * @return sales at gain
     * **/
    public int getSalesAtGain() {
        return salesAtGain;
    }

    /**
     * This method is used to add increment sales at gain <br>
     * Any params required
     * **/
    public void addGain(){
        salesAtGain++;
        if(serverRequest != null){
            // TODO: 19/06/2022  request to add gain
        }
    }

    /**
     * This method is used to get sales at pair
     * @return sales at loss
     * **/
    public int getSalesAtPair() {
        return salesAtPair;
    }

    /**
     * This method is used to increment sales at pair <br>
     * Any params required
     * **/
    public void addPair(){
        salesAtPair++;
        if(serverRequest != null){
            // TODO: 19/06/2022  request to add pair
        }
    }

    /**
     * This method is used to get total sales
     * @return total sales
     * **/
    public int getTotalSales(){
        return salesAtLoss + salesAtGain + salesAtPair;
    }

    /**
     * This method is used to get activation date of account <br>
     * Any params required
     * @return {@link #activationDate} as activation date of account as {@link String}
     * **/
    public long getActivationDateTimestamp() {
        return activationDate;
    }

    /**
     * This method is used to get activation date of account <br>
     * Any params required
     * @return {@link #activationDate} as activation date of account as {@link String}
     * **/
    public String getActivationDate() {
        return timeFormat.format(new Date(activationDate));
    }

    public ArrayList<Double> getIncomes() {
        return incomes;
    }

    /**
     * This method is used to add increment in {@link #incomes} list
     * @param newIncome: new income to insert
     * **/
    public void addIncome(double newIncome){
        if(serverRequest != null){
            // TODO: 19/06/2022  request to add income
        }
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
     * @return general profit of account
     * **/
    public double computeTotalIncome(){
        if(totalIncome == ASSET_NOT_TRADABLE || incomes != null){
            double totalIncome = 0;
            for (double income : incomes)
                totalIncome += income;
            return totalIncome / incomes.size();
        }
        return totalIncome;
    }

    /**
     * This method is used to calculate general profit of account
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted with decimals digits
     * **/
    public double computeTotalIncome(int decimals){
        return tradingTools.roundValue(computeTotalIncome(), decimals);
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #computeTotalIncome()}
     * Any params required
     * @return general income of account formatted as {@link String} es. +1.674% or -1.756%
     * **/
    public String textualizeTotalIncome(){
        return tradingTools.textualizeAssetPercent(computeTotalIncome());
    }

    /**
     * This method is used to format like a {@link String} general income from {@link #computeTotalIncome()}
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted as {@link String} es. +1.67% or -1.75%
     * **/
    public String textualizeTotalIncome(int decimals){
        return tradingTools.textualizeAssetPercent(computeTotalIncome(decimals));
    }

    /**
     * This method is used to get total income for account <br>
     * Any params required
     * @return general profit of account
     * **/
    public double getTotalIncome() {
        return computeTotalIncome();
    }

    /**
     * This method is used to get total income for account
     * @param decimals: number of decimal digits es. 2
     * @return general income of account formatted with decimals digits
     * **/
    public double getTotalIncome(int decimals) {
        return computeTotalIncome(2);
    }

    /**
     * This method is used to print details of {@link TraderAccount} object <br>
     * Any params required
     * **/
    @Override
    public void printDetails() {
        out.println("### [ ACCOUNT STATUS ]\n" +
                "## Total: " + getTotalSales() + "\n" +
                "## At loss: " + ANSI_RED + salesAtLoss + ANSI_RESET + "\n" +
                "## At gain: " + ANSI_GREEN + salesAtGain + ANSI_RESET +"\n" +
                "## At pair: " + salesAtPair + "\n" +
                getIncomeColorLine("## Total income: ", computeTotalIncome(2)) +
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

    /**
     * This method is used to get account details <br>
     * Any params required
     * @return account details as {@link HashMap} of {@link Object}
     * **/
    public HashMap<String, Object> getAccount() {
        HashMap<String, Object> trader = new HashMap<>();
        trader.put(ACTIVATION_DATE_KEY, activationDate);
        trader.put(GAINS_KEY, salesAtGain);
        trader.put(LOSSES_KEY, salesAtLoss);
        trader.put(PAIRS_KEY, salesAtPair);
        trader.put(TOTAL_INCOME_KEY, totalIncome);
        return trader;
    }

}

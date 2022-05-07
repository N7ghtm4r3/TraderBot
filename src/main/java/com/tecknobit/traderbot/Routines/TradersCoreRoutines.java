package com.tecknobit.traderbot.Routines;

public abstract class TradersCoreRoutines {

    public static final String COMPARE_CURRENCY = "USDT";

    public abstract double getWalletBalance(String currency) throws Exception;

    public abstract double getWalletBalance(String currency, int decimals) throws Exception;

    public abstract String getErrorResponse();

}

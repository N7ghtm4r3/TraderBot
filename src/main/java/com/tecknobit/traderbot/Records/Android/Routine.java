package com.tecknobit.traderbot.Records.Android;

/**
 * The {@code Routine} class is useful for Android's traders <br>
 * {@code Routine} defines what trader have to change or do when user from
 * <a href="https://play.google.com/store/apps/details?id=com.tecknobit.traderbot">TraderBot</a>'s app make a request
 * @implNote this class is developed for Android's traders
 * @author Tecknobit N7ghtm4r3
 * **/

public final class Routine {

    /**
     * {@code ROUTINES_KEY} is instance that memorize routines key
     * **/
    public static final String ROUTINES_KEY = "routines";

    /**
     * {@code ROUTINE_KEY} is instance that memorize routine key
     * **/
    public static final String ROUTINE_KEY = "routine";

    /**
     * {@code ROUTINE_EXTRA_VALUE_KEY} is instance that memorize extra value of routine key
     * **/
    public static final String ROUTINE_EXTRA_VALUE_KEY = "extra_value";

    /**
     * {@code routine} is instance that memorize routine that trader has to do
     * **/
    private final String routine;

    /**
     * {@code extraValue} is instance that memorize extra value that trader has to compute
     * **/
    private final String extraValue;

    /** Constructor to init {@link Routine}
     * @param routine: routine that trader has to do
     * @param extraValue: extra value that trader has to compute
     * **/
    public Routine(String routine, String extraValue) {
        this.routine = routine;
        this.extraValue = extraValue;
    }

    public String getRoutine() {
        return routine;
    }

    public String getExtraValue() {
        return extraValue;
    }

}

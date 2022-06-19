package com.tecknobit.traderbot.Records.Android;

public class Routine {

    public static final String ROUTINES_KEY = "routines";
    public static final String ROUTINE_KEY = "routine";
    public static final String ROUTINE_EXTRA_VALUE_KEY = "extra_value";
    private final String routine;
    private final String extraValue;

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

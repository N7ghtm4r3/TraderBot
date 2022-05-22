package com.tecknobit.traderbot.Routines;

public interface RoutineMessages {

    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\033[0;31m";
    String ANSI_GREEN = "\033[0;32m";

    void printDetails();

}

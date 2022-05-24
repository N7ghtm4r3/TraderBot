package com.tecknobit.traderbot.Routines;

/**
 * The {@code RoutineMessages} interface defines base routine methods to print routine messages.<br>
 * @author Tecknobit N7ghtm4r3
 * **/

public interface RoutineMessages {

    /**
     * {@code ANSI_RESET} is instance to reset default color of command line
     * **/
    String ANSI_RESET = "\u001B[0m";

    /**
     * {@code ANSI_RED} is instance to set red color of text in command line
     * **/
    String ANSI_RED = "\033[0;31m";

    /**
     * {@code ANSI_GREEN} is instance to set green color of text in command line
     * **/
    String ANSI_GREEN = "\033[0;32m";

    /**
     * This method is used to print details of an object <br>
     * Any params required
     * **/
    void printDetails();

}

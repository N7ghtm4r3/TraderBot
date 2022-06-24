package com.tecknobit.traderbot.Routines.Interfaces;

/**
 * The {@code RoutineMessages} interface defines base methods to print routine messages.<br>
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
     * This method is used to set flag to print routine messages
     * @param printRoutineMessages: flag to insert to print or not routine messages
     * **/
    void setPrintRoutineMessages(boolean printRoutineMessages);

    /**
     * This method is used to get flag to print or not routine messages
     * @return flag that indicates the possibility or not to print or not routine messages
     * **/
    boolean canPrintRoutineMessages();

    /**
     * This method is used to print red output<br>
     * @param message: message to print of red
     * **/
    default void printRed(String message){
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    /**
     * This method is used to print green output<br>
     * @param message: message to print of green
     * **/
    default void printGreen(String message){
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

}

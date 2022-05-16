package com.tecknobit.traderbot.Routines;

public interface AutoTraderCoreRoutines {

    void start();

    default void printDisclaimer(){
        System.out.println("""
                ############################### DISCLAIMER ALERT #################################\s
                ## This is an auto trader bot made by Tecknobit and all rights are deserved.    ##\s
                ## By deciding to use this auto trader bot, you declare that you are aware of   ##\s
                ## the possible risks associated with the world of crypto investments and how   ##\s
                ## there may be possible losses, large or small, due to market trends. You also ##\s
                ## declare that you are aware that the bot developed by us may not reach your   ##\s
                ## expectations in terms of earnings and that you are solely responsible for    ##\s
                ## the decision to use this bot for your investments and that, therefore,       ##\s
                ## Tecknobit is the product of your choice to use, are excluded from all        ##\s
                ## liability arising from such investments. If you do not want to accept these  ##\s
                ## terms, even in the case of a long time from its start, you are free to       ##\s
                ## terminate the operation of the bot without being attributed any fee and      ##\s
                ## that your personal data entered will not be saved by us at any stage of      ##\s
                ## operation of that bot, neither from launch neither to its termination.       ##\s
                ## If you accept to send results data of investments you will contribute to     ##\s
                ## make grew up this A.I for automatic investments and make it more efficiently ##\s
                ## The data that will be sent will be only about the price, trend percentage,   ##\s
                ## time gap from buy to sell and gain or loss percentage when that asset sold,  ##\s
                ## but ANY personal data that you inserted and ANY data about assets that bot   ##\s
                ## bought during his operations will be sent.                                   ##\s
                ## You will be able to enable or disable this stats report anytime during all   ##\s
                ## lifecycle of the bot like as you want.                                       ##\s
                ##                                 Good use!                                    ##\s
                ##                         Copyright © 2022 Tecknobit                           ##\s
                ##################################################################################
                """);
    }

}

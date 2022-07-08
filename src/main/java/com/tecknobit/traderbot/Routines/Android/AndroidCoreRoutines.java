package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.traderbot.Routines.Android.AndroidWorkflow.Credentials;
import com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages;

import static com.tecknobit.traderbot.Records.Account.TraderDetails.TraderManager;

/**
 * The {@code RoutineMessages} interface defines base methods to Android's trader<br>
 * @author Tecknobit N7ghtm4r3
 * **/

public interface AndroidCoreRoutines extends TraderManager, RoutineMessages {

    /**
     * This method is used to check validity of {@link Credentials} object
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * @throws IllegalArgumentException if {@code credentials} is null
     * **/
    default void checkCredentialsValidity(Credentials credentials){
        if(credentials == null)
            throw new IllegalArgumentException("Credentials object cannot be null");
    }

    /**
     * This method is used to print Android disclaimer alert to warn user of responsibility of storage and manage
     * api keys create on exchange platforms. <br>
     * It is printed in Android's traders. <br>
     * Any params required
     * **/
    default void printAndroidDisclaimer(){
        System.out.println("############################### DISCLAIMER ALERT #################################\n" +
                           "## Note: The credentials regarding the registration and login for the Android   ##\n" +
                           "## interfaces will be requested only at the time of starting this service, but  ## \n" +
                           "## on other occasions Tecknobit will not ask you to enter any data so do not    ##\n" +
                           "## share the credentials for the Tecknobit account. The storing and managing    ##\n" +
                           "## about the credentials to be entered is your responsibility.                  ##\n" +
                           "##################################################################################");
    }

    /**
     * This method is used to init a {@link Credentials} object to start {@link AndroidWorkflow}
     * @param credentials: is object that contains your Tecknobit's account credentials, not your private exchange keys
     * **/
    void initCredentials(Credentials credentials) throws Exception;

    /**
     * This method is used to handle {@link AndroidWorkflow} <br>
     * Any params required
     * **/
    void workflowHandler();

    /**
     * This method is used to update account wallet list <br>
     * Any params required
     * **/
    void refreshWalletList() throws Exception;

    /**
     * This method is used to get credentials inserted for trader login
     * @return trader credentials as {@link Credentials} object
     * **/
    Credentials getCredentials();

}

package com.tecknobit.traderbot.Exceptions;

import org.json.JSONObject;

public class SaveData extends Exception{

    public SaveData(JSONObject credentials) {
        super("""
                Note: is not an error, but is an alert!
                Before restart service save credentials data of Tecknobit's account anywhere that you retain safe,
                they will be need in login operations here for traders and also for your Android's device.
                Next restart you will have to insert these credentials in Credentials object to start\040
                normal flow of trader.
                ###################################### CREDENTIALS ######################################
                """ +
                credentials.toString(4));
    }

}

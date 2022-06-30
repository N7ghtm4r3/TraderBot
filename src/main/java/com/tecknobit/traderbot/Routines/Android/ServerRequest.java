package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.aesHelper.Client.ClientCipher;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RED;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RESET;

/**
 * The {@code ServerRequest} class is useful to make server request for Android's traders<br>
 * Is useful for Android's type traders.
 * @author Tecknobit N7ghtm4r3
 * **/

public final class ServerRequest {

    /**
     * {@code SERVICE_UNAVAILABLE} is instance to indicate that service is not available to perform requests
     * **/
    public static final String SERVICE_UNAVAILABLE = ANSI_RED + "Service is not available for serve your request, wait"
            + ANSI_RESET;

    /**
     * {@code GET_KEYS_OPE} request
     * **/
    public static final String GET_KEYS_OPE = "get_keys_ope";

    /**
     * {@code REGISTRATION_OPE} request
     * **/
    public static final String REGISTRATION_OPE = "registration_ope";

    /**
     * {@code LOGIN_OPE} request
     * **/
    public static final String LOGIN_OPE = "login_ope";

    /**
     * {@code GET_TRADER_ACCOUNT_OPE} request
     * **/
    public static final String GET_TRADER_ACCOUNT_OPE = "get_trader_account_ope";

    /**
     * {@code GET_ROUTINES_TRADER_OPE} request
     * **/
    public static final String GET_ROUTINES_TRADER_OPE = "get_routines_trader_ope";

    /**
     * {@code CHANGE_MAIL_OPE} request
     * **/
    public static final String CHANGE_MAIL_OPE = "change_mail_ope";

    /**
     * {@code CHANGE_PASSWORD_OPE} request
     * **/
    public static final String CHANGE_PASSWORD_OPE = "change_password_ope";

    /**
     * {@code CHANGE_REFRESH_TIME_PRICES_OPE} request
     * **/
    public static final String CHANGE_REFRESH_TIME_PRICES_OPE = "change_refresh_prices_time_ope";

    /**
     * {@code CHANGE_TRADER_STATUS_OPE} request
     * **/
    public static final String CHANGE_TRADER_STATUS_OPE = "change_trader_status_ope";

    /**
     * {@code CHANGE_LANGUAGE_OPE} request
     * **/
    public static final String CHANGE_LANGUAGE_OPE = "change_language_ope";

    /**
     * {@code CHANGE_CURRENCY_OPE} request
     * **/
    public static final String CHANGE_CURRENCY_OPE = "change_currency_ope";

    /**
     * {@code CHANGE_ACCOUNT_TIME_DELETION_OPE} request
     * **/
    public static final String CHANGE_ACCOUNT_TIME_DELETION_OPE = "change_account_time_deletion_ope";

    /**
     * {@code INSERT_WALLET_BALANCE_OPE} request
     * **/
    public static final String INSERT_WALLET_BALANCE_OPE = "insert_wallet_balance_ope";

    /**
     * {@code INSERT_CRYPTOCURRENCY_OPE} request
     * **/
    public static final String INSERT_CRYPTOCURRENCY_OPE = "insert_cryptocurrency_ope";

    /**
     * {@code DELETE_CRYPTOCURRENCY_OPE} request
     * **/
    public static final String DELETE_CRYPTOCURRENCY_OPE = "delete_cryptocurrency_ope";

    /**
     * {@code INSERT_REFRESHED_PRICES} request
     * **/
    public static final String INSERT_REFRESHED_PRICES = "insert_refreshed_prices_ope";

    /**
     * {@code INSERT_QUOTE_OPE} request
     * **/
    public static final String INSERT_QUOTE_OPE = "insert_quote_ope";

    /**
     * {@code REMOVE_QUOTE_OPE} request
     * **/
    public static final String REMOVE_QUOTE_OPE = "remove_quote_ope";

    /**
     * {@code INSERT_CHECKING_LIST_OPE} request
     * **/
    public static final String INSERT_CHECKING_LIST_OPE = "insert_checking_list_ope";

    /**
     * {@code LOGOUT_ACCOUNT_OPE} request
     * **/
    public static final String LOGOUT_ACCOUNT_OPE = "logout_account_ope";

    /**
     * {@code DELETE_ACCOUNT_OPE} request
     * **/
    public static final String DELETE_ACCOUNT_OPE = "delete_account_ope";

    /**
     * {@code CREDENTIALS_ERROR_RESPONSE} response
     * **/
    public static final int CREDENTIALS_ERROR_RESPONSE = 400;

    /**
     * {@code GENERIC_ERROR_RESPONSE} response
     * **/
    public static final int GENERIC_ERROR_RESPONSE = 300;

    /**
     * {@code SUCCESSFUL_RESPONSE} response
     * **/
    public static final int SUCCESSFUL_RESPONSE = 200;

    /**
     * {@code ERROR_RESPONSE} response
     * **/
    public static final int ERROR_RESPONSE = 500;

    /**
     * {@code STATUS_CODE} key
     * **/
    public static final String STATUS_CODE = "status_code";

    /**
     * {@code TOKEN_KEY} key
     * **/
    public static final String TOKEN_KEY = "token";

    /**
     * {@code MAIL_KEY} key
     * **/
    public static final String MAIL_KEY = "mail";

    /**
     * {@code NEW_MAIL_KEY} key
     * **/
    public static final String NEW_MAIL_KEY = "new_mail";

    /**
     * {@code PASSWORD_KEY} key
     * **/
    public static final String PASSWORD_KEY = "password";

    /**
     * {@code NEW_PASSWORD_KEY} key
     * **/
    public static final String NEW_PASSWORD_KEY = "new_password";

    /**
     * {@code IV_SPEC_KEY} key
     * **/
    public static final String IV_SPEC_KEY = "iv_spec";

    /**
     * {@code SECRET_KEY} key
     * **/
    public static final String SECRET_KEY = "secret_key";

    /**
     * {@code DEVICE_TOKEN_KEY} key
     * **/
    public static final String DEVICE_TOKEN_KEY = "device_token";

    /**
     * {@code AUTH_TOKEN_KEY} key
     * **/
    public static final String AUTH_TOKEN_KEY = "auth_token";

    /**
     * {@code LANGUAGE_KEY} key
     * **/
    public static final String LANGUAGE_KEY = "language";

    /**
     * {@code CURRENCY_KEY} key
     * **/
    public static final String CURRENCY_KEY = "currency";

    /**
     * {@code TIME_DELETION_KEY} key
     * **/
    public static final String TIME_DELETION_KEY = "time_deletion";

    /**
     * {@code QUOTE_KEY} key
     * **/
    public static final String QUOTE_KEY = "quote";

    /**
     * {@code BALANCE_KEY} key
     * **/
    public static final String BALANCE_KEY = "balance";

    /**
     * {@code response} object for requests response
     * **/
    public static JSONObject response;

    /**
     * {@code clientCipher} object to cipher requests
     * **/
    private final ClientCipher clientCipher;

    /**
     * {@code clientCipher} object to write requests
     * **/
    private PrintWriter printWriter;

    /**
     * {@code ciphered} flag to cipher or not requests
     * **/
    private final boolean ciphered;

    /**
     * {@code socket} object to transport request
     * **/
    private volatile Socket socket;

    /**
     * {@code authToken} is instance that memorize identifier of server trader to log in and requests operations
     * **/
    private String authToken;

    /**
     * {@code token} is instance that memorize identifier of user to log in and requests operations
     * **/
    private String token;

    /** Constructor to init {@link ServerRequest}
     * @param ivSpec: initialization vector used in server requests
     * @param secretKey: secret key used in server requests
     * @param authToken : identifier of server trader to log in and requests operations
     * @param token: identifier of user to log in and requests operations
     * **/
    public ServerRequest(String ivSpec, String secretKey, String authToken, String token) throws Exception {
        clientCipher = new ClientCipher(ivSpec, secretKey);
        ciphered = true;
        this.authToken = authToken;
        this.token = token;
    }

    /** Constructor to init {@link ServerRequest}
     * @param ivSpec: initialization vector used in server requests
     * @param secretKey: secret key used in server requests
     * **/
    public ServerRequest(String ivSpec, String secretKey) throws Exception {
        clientCipher = new ClientCipher(ivSpec, secretKey);
        ciphered = true;
        authToken = null;
        token = null;
    }

    /** Constructor to init {@link ServerRequest} **/
    public ServerRequest() {
        clientCipher = null;
        ciphered = false;
    }

    /**
     * This method is used to send request without tokens
     * @param message: message for the request
     * @param operation: operation for the server
     * **/
    public void sendRequest(JSONObject message, String operation) throws Exception {
        try {
            createSocket();
            message.put("ope", operation);
            String messageToSent = message.toString();
            if (ciphered) {
                assert clientCipher != null;
                messageToSent = clientCipher.encryptRequest(messageToSent);
            }
            printWriter.println(messageToSent);
            printWriter.flush();
        }catch (NullPointerException ignored){
        }
    }

    /**
     * This method is used to send request with tokens
     * @param message: message for the request
     * @param operation: operation for the server
     * **/
    public void sendTokenRequest(JSONObject message, String operation) throws Exception {
        try {
            createSocket();
            message.put("ope", operation);
            message.put(AUTH_TOKEN_KEY, authToken);
            assert clientCipher != null;
            String messageToSent = clientCipher.encrypt(message.toString()) + "#" + token;
            printWriter.println(messageToSent);
            printWriter.flush();
        }catch (NullPointerException ignored){
        }
    }

    /**
     * This method is used to read request <br>
     * Any params required
     * @return response of the server as {@link JSONObject}
     * **/
    public JSONObject readResponse() {
        try {
            String response = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if(ciphered) {
                assert clientCipher != null;
                try{
                    response = clientCipher.decryptResponse(response);
                }catch (BadPaddingException e){
                    response = "{\"status_code\": -1}";
                }
            }
            socket = null;
            return new JSONObject(response);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * This method is used to create or recreate {@link #socket} for requests <br>
     * Any params required
     * **/
    private void createSocket() throws IOException {
        try {
            while (socket != null) Thread.onSpinWait();
            socket = new Socket("localhost", 7898);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        }catch (ConnectException ignored){
        }
    }

}

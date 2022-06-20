package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.aesHelper.Client.ClientCipher;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RED;
import static com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages.ANSI_RESET;

public final class ServerRequest {

    public static final String SERVICE_UNAVAILABLE = ANSI_RED + "Service is not available for serve your request, wait"
            + ANSI_RESET;
    public static final String GET_KEYS_OPE = "get_keys_ope";
    public static final String REGISTRATION_OPE = "registration_ope";
    public static final String LOGIN_OPE = "login_ope";
    public static final String GET_TRADER_ACCOUNT_OPE = "get_trader_account_ope";
    public static final String GET_ROUTINES_TRADER_OPE = "get_routines_trader_ope";
    public static final String CHANGE_MAIL_OPE = "change_mail_ope";
    public static final String CHANGE_PASSWORD_OPE = "change_password_ope";
    public static final String CHANGE_REFRESH_TIME_PRICES_OPE = "change_refresh_prices_time_ope";
    public static final String CHANGE_TRADER_STATUS_OPE = "change_trader_status_ope";
    public static final String CHANGE_LANGUAGE_OPE = "change_language_ope";
    public static final String CHANGE_CURRENCY_OPE = "change_currency_ope";
    public static final String CHANGE_ACCOUNT_TIME_DELETION_OPE = "change_account_time_deletion_ope";
    public static final String INSERT_WALLET_BALANCE_OPE = "insert_wallet_balance_ope";
    public static final String INSERT_CRYPTOCURRENCY_OPE = "insert_cryptocurrency_ope";
    public static final String DELETE_CRYPTOCURRENCY_OPE = "delete_cryptocurrency_ope";
    public static final String INSERT_QUOTE_OPE = "insert_quote_ope";
    public static final String REMOVE_QUOTE_OPE = "remove_quote_ope";
    public static final String LOGOUT_ACCOUNT_OPE = "logout_account_ope";
    public static final String DELETE_ACCOUNT_OPE = "delete_account_ope";
    public static final String STATUS_CODE = "status_code";
    public static final int CREDENTIALS_ERROR_RESPONSE = 400;
    public static final int GENERIC_ERROR_RESPONSE = 300;
    public static final int SUCCESSFUL_RESPONSE = 200;
    public static final int ERROR_RESPONSE = 500;
    public static final String TOKEN_KEY = "token";
    public static final String MAIL_KEY = "mail";
    public static final String NEW_MAIL_KEY = "new_mail";
    public static final String PASSWORD_KEY = "password";
    public static final String NEW_PASSWORD_KEY = "new_password";
    public static final String IV_SPEC_KEY = "iv_spec";
    public static final String SECRET_KEY = "secret_key";
    public static final String DEVICE_TOKEN_KEY = "device_token";
    public static final String AUTH_TOKEN_KEY = "auth_token";
    public static final String LANGUAGE_KEY = "language";
    public static final String CURRENCY_KEY = "currency";
    public static final String TIME_DELETION_KEY = "time_deletion";
    public static final String QUOTE_KEY = "quote";
    public static final String BALANCE_KEY = "balance";
    public static JSONObject response;
    private final ClientCipher clientCipher;
    private PrintWriter printWriter;
    private final boolean ciphered;
    private volatile Socket socket;
    private String authToken;
    private String tokenId;
    private String token;
    private String secretKey;

    public ServerRequest(String ivSpec, String secretKey, String authToken, String tokenId) throws Exception {
        clientCipher = new ClientCipher(ivSpec, secretKey);
        ciphered = true;
        this.authToken = authToken;
        this.tokenId = tokenId;
    }

    public ServerRequest(String ivSpec, String secretKey) throws Exception {
        clientCipher = new ClientCipher(ivSpec, secretKey);
        ciphered = true;
        authToken = null;
        tokenId = null;
    }

    public ServerRequest() {
        clientCipher = null;
        ciphered = false;
    }

    public void sendRequest(JSONObject message, String operation) throws Exception {
        try {
            createSocket();
            message.put("ope", operation);
            String messageToSent = message.toString();
            if (ciphered) {
                assert clientCipher != null;
                messageToSent = clientCipher.encryptRequest(messageToSent);
            }
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(messageToSent);
            printWriter.flush();
        }catch (NullPointerException ignored){
        }
    }

    public void sendTokenRequest(JSONObject message, String operation) throws Exception {
        try {
            createSocket();
            message.put("ope", operation);
            message.put(AUTH_TOKEN_KEY, authToken);
            assert clientCipher != null;
            String messageToSent = clientCipher.encrypt(message.toString()) + "#" + tokenId;
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(messageToSent);
            printWriter.flush();
        }catch (NullPointerException ignored){
        }
    }

    public JSONObject readResponse() {
        try {
            String response = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if(ciphered) {
                assert clientCipher != null;
                response = clientCipher.decryptResponse(response);
            }
            socket = null;
            return new JSONObject(response);
        }catch (Exception e){
            return null;
        }
    }

    private void createSocket() throws IOException {
        try {
            while (socket != null) Thread.onSpinWait();
            socket = new Socket("localhost", 7898);
        }catch (ConnectException ignored){
        }
    }

}

package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.aesHelper.Client.ClientCipher;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public final class ServerRequest {

    public static final String GET_KEYS_OPE = "get_keys_ope";
    public static final String REGISTRATION_OPE = "registration_ope";
    public static final String LOGIN_OPE = "login_ope";
    public static final String CHANGE_MAIL_OPE = "change_mail_ope";
    public static final String CHANGE_PASSWORD_OPE = "change_password_ope";
    public static final String LOGOUT_ACCOUNT_OPE = "logout_account_ope";
    public static final String DELETE_ACCOUNT_OPE = "delete_account_ope";
    public static final String STATUS_CODE = "status_code";
    public static final int GENERIC_ERROR_RESPONSE = 300;
    public static final int SUCCESSFUL_RESPONSE = 200;
    public static final String TOKEN_KEY = "token";
    public static final String MAIL_KEY = "mail";
    public static final String PASSWORD_KEY = "password";
    public static final String IV_SPEC_KEY = "iv_spec";
    public static final String SECRET_KEY = "secret_key";
    public static final String DEVICE_TOKEN_KEY = "device_token";
    public static final String AUTH_TOKEN_KEY = "auth_token";
    public static JSONObject response;
    private final ClientCipher clientCipher;
    private final PrintWriter printWriter;
    private final boolean ciphered;
    private final Socket socket;
    private String authToken;
    private String tokenId;

    public ServerRequest(String ivSpec, String secretKey, String authToken, String tokenId) throws Exception {
        clientCipher = new ClientCipher(ivSpec, secretKey);
        socket = new Socket("localhost", 7898);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        ciphered = true;
        this.authToken = authToken;
        this.tokenId = tokenId;
    }

    public ServerRequest(String ivSpec, String secretKey) throws Exception {
        clientCipher = new ClientCipher(ivSpec, secretKey);
        socket = new Socket("localhost", 7898);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        ciphered = true;
        authToken = null;
        tokenId = null;
    }

    public ServerRequest() throws IOException {
        clientCipher = null;
        socket = new Socket("localhost", 7898);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        ciphered = false;
    }

    public void sendRequest(JSONObject message, String operation) throws Exception {
        message.put("ope", operation);
        String messageToSent = message.toString();
        if(ciphered)
            messageToSent = clientCipher.encryptRequest(messageToSent);
        printWriter.println(messageToSent);
        printWriter.flush();
    }

    public void sendTokenRequest(JSONObject message, String operation) throws Exception {
        message.put("ope", operation);
        message.put(AUTH_TOKEN_KEY, authToken);
        String messageToSent = clientCipher.encrypt(message.toString()) + "#" + tokenId;
        printWriter.println(messageToSent);
        printWriter.flush();
    }

    public JSONObject readRequest() throws Exception {
        String response = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        if(ciphered)
            response = clientCipher.decryptResponse(response);
        return new JSONObject(response);
    }

}

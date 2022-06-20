package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.traderbot.Exceptions.SaveData;
import com.tecknobit.traderbot.Records.Account.TraderDetails;
import com.tecknobit.traderbot.Records.Android.Routine;
import com.tecknobit.traderbot.Routines.Interfaces.RoutineMessages;
import com.tecknobit.traderbot.Routines.Interfaces.TraderCoreRoutines;
import com.tecknobit.traderbot.Traders.Interfaces.Android.AndroidBinanceTrader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import java.util.ArrayList;

import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static com.tecknobit.traderbot.Records.Account.TraderDetails.TRADER_TYPE_KEY;
import static com.tecknobit.traderbot.Records.Android.Routine.*;
import static com.tecknobit.traderbot.Routines.Android.ServerRequest.*;
import static java.lang.Integer.parseInt;
import static org.apache.commons.validator.routines.EmailValidator.getInstance;

public final class AndroidWorkflow implements RoutineMessages {

    private final TraderCoreRoutines trader;
    private final Credentials credentials;
    private boolean printRoutineMessages;
    private boolean workflowStarted;
    private final String authToken;
    private final String token;
    private final String ivSpec;
    private final String secretKey;

    public AndroidWorkflow(TraderCoreRoutines traderCoreRoutines, Credentials credentials, boolean printRoutineMessages) {
        this.printRoutineMessages = printRoutineMessages;
        this.credentials = credentials;
        this.trader = traderCoreRoutines;
        authToken = credentials.getAuthToken();
        token = credentials.getToken();
        ivSpec = credentials.getIvSpec();
        secretKey = credentials.getSecretKey();
    }

    public void startWorkflow(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        performRoutines();
                        sleep(2000);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    private void performRoutines() throws Exception {
        for (Routine routine : getRoutines()){
            switch (routine.getRoutine()){
                case CHANGE_MAIL_OPE:
                    credentials.setMail(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_MAIL_OPE + "] Mail successfully changed", true);
                    break;
                case CHANGE_PASSWORD_OPE:
                    credentials.setPassword(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_PASSWORD_OPE + "] Password successfully changed", true);
                    break;
                case CHANGE_REFRESH_TIME_PRICES_OPE:
                    trader.setRefreshPricesTime(parseInt(routine.getExtraValue()));
                    printOperationStatus("[" + CHANGE_REFRESH_TIME_PRICES_OPE + "] Refresh prices time successfully changed",
                            true);
                    break;
                case CHANGE_TRADER_STATUS_OPE:
                    printOperationStatus("[" + CHANGE_TRADER_STATUS_OPE + "] Trader status successfully changed",
                            true);
                    if(routine.getExtraValue().equals(STOPPED_TRADER_STATUS)){
                        if(trader instanceof AndroidBinanceTrader)
                            ((AndroidBinanceTrader)trader).disableTrader();
                        printOperationStatus("Trader status: [" + STOPPED_TRADER_STATUS + "]", false);
                    }else{
                        if(trader instanceof AndroidBinanceTrader)
                            ((AndroidBinanceTrader)trader).enableTrader();
                        printOperationStatus("Trader status: [" + RUNNING_TRADER_STATUS+ "]", true);
                    }
                    break;
                case CHANGE_CURRENCY_OPE:
                    if(trader instanceof AndroidBinanceTrader)
                        ((AndroidBinanceTrader)trader).setBaseCurrency(routine.getExtraValue());
                    printOperationStatus("[" + CHANGE_CURRENCY_OPE + "] Base currency successfully changed",
                            true);
                    break;
                case INSERT_QUOTE_OPE:
                    trader.insertQuoteCurrency(routine.getExtraValue());
                    printOperationStatus("[" + INSERT_QUOTE_OPE + "] Base currency successfully inserted",
                            true);
                    break;
                case REMOVE_QUOTE_OPE:
                    trader.removeQuoteCurrency(routine.getExtraValue());
                    printOperationStatus("[" + REMOVE_QUOTE_OPE + "] Base currency successfully removed",
                            true);
            }
        }
    }

    private ArrayList<Routine> getRoutines() throws Exception {
        ServerRequest serverRequest = getServerRequest();
        ArrayList<Routine> routines = new ArrayList<>();
        try {
            try {
                serverRequest.sendTokenRequest(new JSONObject(), GET_ROUTINES_TRADER_OPE);
                response = serverRequest.readResponse();
                assert response != null;
                JSONArray jsonRoutines = response.getJSONArray(ROUTINES_KEY);
                for (int j = 0; j < jsonRoutines.length(); j++) {
                    JSONObject routine = jsonRoutines.getJSONObject(j);
                    routines.add(new Routine(routine.getString(ROUTINE_KEY), routine.getString(ROUTINE_EXTRA_VALUE_KEY)));
                }
            }catch (BadPaddingException e){
                printRed("[ACCOUNT DELETED] You deleted account for trader, we hope to see you again soon!");
                System.exit(0);
            }
        } catch (Exception e) {
            printOperationFailed("ROUTINE REQUEST");
        }
        return routines;
    }

    public void insertWalletBalance(double balance) throws Exception {
        String ope = "WALLET BALANCE";
        ServerRequest serverRequest = getServerRequest();
        try {
            serverRequest.sendTokenRequest(new JSONObject().put(BALANCE_KEY, balance), INSERT_WALLET_BALANCE_OPE);
            response = serverRequest.readResponse();
            if(response != null){
                switch (response.getInt(STATUS_CODE)){
                    case SUCCESSFUL_RESPONSE:
                        if(printRoutineMessages)
                            printOperationSuccess(ope);
                        break;
                    case GENERIC_ERROR_RESPONSE:
                        if(printRoutineMessages)
                            printRed("[" + ope + "] Wallet balance cannot be less than 0");
                        break;
                    default:
                        printOperationFailed(ope);
                }
            }else
                printOperationFailed(ope);
        } catch (Exception e) {
            printOperationFailed(ope);
        }
    }

    private void printOperationStatus(String msg, boolean greenPrint) {
        if(printRoutineMessages){
            if(greenPrint)
                printGreen(msg);
            else
                printRed(msg);
        }
    }

    private void printOperationFailed(String ope){
        if(printRoutineMessages)
            printRed("[" + ope + "] Operation failed");
    }

    private void printOperationSuccess(String ope){
        if(printRoutineMessages)
            printGreen("[" + ope + "] Operation ended successfully");
    }

    @Override
    public void setPrintRoutineMessages(boolean printRoutineMessages) {
        this.printRoutineMessages = printRoutineMessages;
    }

    @Override
    public boolean canPrintRoutineMessages() {
        return printRoutineMessages;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * This method is used to get object for make a server request
     *
     * @return request as {@link ServerRequest} object
     **/
    public ServerRequest getServerRequest() throws Exception {
        return new ServerRequest(ivSpec, secretKey, authToken, token);
    }

    public static final class Credentials{

        public static final int MAX_TOKEN_LENGTH = 32;
        public static final int MIN_TOKEN_LENGTH = 8;
        public static boolean yetINmeroray = false; // TODO: 20/06/2022 LOCK MULTIPLE INSTANTIATION OF CLASS
        private ServerRequest serverRequest;
        private final String authToken;
        private String mail;
        private String password;
        private final String token;
        private final String ivSpec;
        private final String secretKey;
        private TraderDetails traderDetails;

        /**
         * {
         *     "secret_key": "I+IGUYuPGADJuzQqKjhB+fnyzrbsT6rqRB29jH8LN/c=",
         *     "password": "gagagagaga",
         *     "mail": "aaaaaA@gmail.com",
         *     "iv_spec": "l47t+O1gG6tCUug20BhTvg==",
         *     "auth_token": "92bd8bab915a4de5a0e5bf1d8d893162",
         *     "token": "a9d2b1b92330434cbb8242ab619e913b"
         * }
         * **/

        public Credentials(String mail, String password) {
            if(!getInstance().isValid(mail))
                throw new IllegalArgumentException("Mail must be a valid mail");
            else
                this.mail = mail;
            if(wrongPasswordValidity(password))
                throw new IllegalArgumentException("Password must be 8 - 32 characters length");
            else
                this.password = password;
            authToken = null;
            token = null;
            ivSpec = null;
            secretKey = null;
        }

        public void sendRegistrationRequest() throws Exception {
            if(traderDetails != null && token == null){
                getPublicKeys();
                serverRequest.sendRequest(new JSONObject().put(MAIL_KEY, mail).put(PASSWORD_KEY, password)
                        .put(TRADER_STATUS_KEY, traderDetails.getTraderStatus())
                        .put(REFRESH_PRICES_TIME_KEY, traderDetails.getRefreshPricesTime())
                        .put(TRADER_PLATFORM_KEY, traderDetails.getTraderPlatform())
                        .put(LAST_TRADER_ACTIVITY_KEY, traderDetails.getLastTraderActivity())
                        .put(RUNNING_FROM_DATE_KEY, traderDetails.getRunningFromDate())
                        .put(TRADER_TYPE_KEY, traderDetails.getTraderType()), REGISTRATION_OPE);
                response = serverRequest.readResponse();
                if(response != null){
                    switch (response.getInt(STATUS_CODE)){
                        case SUCCESSFUL_RESPONSE:
                            throw new SaveData(new JSONObject().put(TOKEN_KEY, response.getString(TOKEN_KEY))
                                    .put(AUTH_TOKEN_KEY, response.getString(AUTH_TOKEN_KEY))
                                    .put(IV_SPEC_KEY, response.getString(IV_SPEC_KEY))
                                    .put(SECRET_KEY, response.getString(SECRET_KEY))
                                    .put(MAIL_KEY, mail)
                                    .put(PASSWORD_KEY, password));
                        case GENERIC_ERROR_RESPONSE: throw new IllegalAccessException("Mail not available");
                        default: throw new IllegalAccessException("Operation failed");
                    }
                }else
                    throw new IllegalStateException(ANSI_RED + "Service is not available for serve your request, wait" + ANSI_RESET);
            }
        }

        public Credentials(String authToken, String mail, String password, String token, String ivSpec,
                           String secretKey) throws Exception {
            this.authToken = authToken;
            this.mail = mail;
            this.password = password;
            this.token = token;
            this.ivSpec = ivSpec;
            this.secretKey = secretKey;
            getPublicKeys();
            serverRequest.sendRequest(new JSONObject().put(MAIL_KEY, mail).put(PASSWORD_KEY, password)
                    .put(AUTH_TOKEN_KEY, authToken), LOGIN_OPE);
            response = serverRequest.readResponse();
            if(response != null) {
                switch (response.getInt(STATUS_CODE)){
                    case SUCCESSFUL_RESPONSE:
                        if(!token.equals(response.getString(TOKEN_KEY)) || !ivSpec.equals(response.getString(IV_SPEC_KEY))
                                || !secretKey.equals(response.getString(SECRET_KEY))) {
                            throw new IllegalAccessException("Wrong credentials inserted");
                        }
                        break;
                    case GENERIC_ERROR_RESPONSE: throw new IllegalAccessException("Wrong credentials inserted");
                    default: throw new IllegalAccessException("Operation failed");
                }
            }else
                throw new IllegalStateException(ANSI_RED + "Service is not available for serve your request, wait" + ANSI_RESET);
        }

        private void getPublicKeys() {
            try {
                serverRequest = new ServerRequest();
                serverRequest.sendRequest(new JSONObject(), GET_KEYS_OPE);
                response = serverRequest.readResponse();
                if(response != null)
                    serverRequest = new ServerRequest(response.getString(IV_SPEC_KEY), response.getString(SECRET_KEY));
            }catch (Exception e){
                throw new IllegalStateException(ANSI_RED + "Service is not available for serve your request, wait" + ANSI_RESET);
            }
        }

        private boolean wrongPasswordValidity(String password){
            if(password == null)
                return true;
            int length = password.length();
            return length < MIN_TOKEN_LENGTH || length > MAX_TOKEN_LENGTH;
        }

        public ServerRequest getServerRequest() {
            return serverRequest;
        }

        public String getAuthToken() {
            return authToken;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            if(!getInstance().isValid(mail))
                throw new IllegalArgumentException("Mail must be a valid mail");
            this.mail = mail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            if(wrongPasswordValidity(password))
                throw new IllegalArgumentException("Password must be 8 - 32 characters length");
            this.password = password;
        }

        public String getToken() {
            return token;
        }

        public String getIvSpec() {
            return ivSpec;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setTraderDetails(TraderDetails traderDetails){
            this.traderDetails = traderDetails;
        }

    }

}

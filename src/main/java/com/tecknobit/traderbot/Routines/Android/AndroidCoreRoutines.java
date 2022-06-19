package com.tecknobit.traderbot.Routines.Android;

import com.tecknobit.traderbot.Exceptions.SaveData;
import com.tecknobit.traderbot.Records.Account.TraderDetails;
import org.json.JSONObject;

import static com.tecknobit.traderbot.Records.Account.TraderDetails.*;
import static com.tecknobit.traderbot.Routines.Android.ServerRequest.*;
import static org.apache.commons.validator.routines.EmailValidator.getInstance;

public interface AndroidCoreRoutines extends TraderManager{

    default void checkCredentialsValidity(Credentials credentials){
        if(credentials == null)
            throw new IllegalArgumentException("Credentials object cannot be null");
    }

    default void printAndroidDisclaimer(){
        System.out.println("""
                ############################### DISCLAIMER ALERT #################################\s
                ## Note: The credentials regarding the registration and login for the Android   ##\s
                ## interfaces will be requested only at the time of starting this service, but  ##\s   
                ## on other occasions Tecknobit will not ask you to enter any data so do not    ##\s
                ## share the credentials for the Tecknobit account. The storing and managing    ##\s
                ## about the credentials to be entered is your responsibility.                  ##\s
                ##################################################################################
                """);
    }

    void initCredentials() throws Exception;

    final class Credentials{

        public static final int MAX_TOKEN_LENGTH = 32;
        public static final int MIN_TOKEN_LENGTH = 8;
        private ServerRequest serverRequest;
        private final String authToken;
        private final String mail;
        private final String password;
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
        }

        private void getPublicKeys() throws Exception {
            serverRequest = new ServerRequest();
            serverRequest.sendRequest(new JSONObject(), GET_KEYS_OPE);
            response = serverRequest.readResponse();
            serverRequest = new ServerRequest(response.getString(IV_SPEC_KEY), response.getString(SECRET_KEY));
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

        public String getPassword() {
            return password;
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

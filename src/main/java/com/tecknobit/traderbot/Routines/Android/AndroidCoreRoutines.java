package com.tecknobit.traderbot.Routines.Android;

public interface AndroidCoreRoutines {

    /*
     The credentials regarding the registration and login for the Android
    interfaces will be requested only at the time of starting this service, but on other occasions Tecknobit will not
    ask you to enter any data so do not even share the credentials for the Tecknobit account. storing and managing
    the credentials to be entered is your responsibility.*/

    final class Credentials{

        private String authToken;
        private String mail;
        private String password;
        private String token;
        private String ivSpec;
        private String secretKey;

        //registration
        public Credentials(String mail, String password) {
            this.mail = mail;
            this.password = password;
        }

        //login
        public Credentials(String authToken, String mail, String password) {
            this.authToken = authToken;
            this.mail = mail;
            this.password = password;
            //pass device token to null
        }

        //default
        public Credentials(String authToken, String mail, String password, String token, String ivSpec, String secretKey) {
            this.authToken = authToken;
            this.mail = mail;
            this.password = password;
            this.token = token;
            this.ivSpec = ivSpec;
            this.secretKey = secretKey;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getIvSpec() {
            return ivSpec;
        }

        public void setIvSpec(String ivSpec) {
            this.ivSpec = ivSpec;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

}

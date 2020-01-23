package org.zafritech.zscode.administrator.core.api.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;
import org.zafritech.zscode.administrator.core.utils.Preferences;

public class AuthHelper {

    private Preferences prefs;

    public static String KEY_SECRET = "secretKey";
    public static String KEY_ISSUER = "issuerKey";
    public static String KEY_TOKEN = "tokenKey";
    public static String KEY_USERNAME = "usernameKey";
    public static String KEY_PASSWORD = "passwordKey";
//    public static String KEY_ENCRYPTED_UNAME = "EncrypedUsernameKey";
//    public static String KEY_ENCRYPTED_UNAME_IV = "EncrypedUsernameIV";
//    public static String KEY_ENCRYPTED_PWORD = "EncrypedPasswordKey";
//    public static String KEY_ENCRYPTED_PWORD_IV = "EncrypedPasswordIV";

    public AuthHelper(Context context) {

        this.prefs = new Preferences(context);
    }

    public boolean validAuthToken() {

        // Reference: https://github.com/auth0/JWTDecode.Android

        String authJwtSecret = prefs.getItem(KEY_SECRET);
        String authJwtIssuer = prefs.getItem(KEY_ISSUER);
        String authToken = prefs.getItem(KEY_TOKEN);

        // DEBUG code!
        System.out.println("");
        System.out.println("==============================================================================");
        System.out.println("KEY_SECRET: " + authJwtSecret);
        System.out.println("KEY_ISSUER: " + authJwtIssuer);
        System.out.println("KEY_TOKEN: " + authToken);
        System.out.println("==============================================================================");
        System.out.println("");

        if (authJwtSecret != null && !authJwtSecret.isEmpty() && authToken != null && !authToken.isEmpty()) {

            JWT jwt = new JWT(authToken);

            // 10 seconds leeway after expiry
            if (!jwt.isExpired(10) && authJwtIssuer.equals(jwt.getIssuer())) {

                return true;

            } else {

                return false;
            }

        } else {

            return false;
        }
    }

    public void storeTokenIssuer(String tokenKey) {

        prefs.setItem(KEY_ISSUER, tokenKey);
    }
    public void storeTokenKey(String tokenKey) {

        prefs.setItem(KEY_TOKEN, tokenKey);
    }

    public String getTokenKey() {

        return prefs.getItem(KEY_TOKEN);
    }

    public void removeTokenKey() {

        prefs.deleteItem(KEY_TOKEN);
    }

    public void storeUserName(String userName) {

        prefs.setItem(KEY_USERNAME, userName);
    }

    public String getUserName() {

        return prefs.getItem(KEY_USERNAME);
    }

    public void removeUserName() {

        prefs.deleteItem(KEY_USERNAME);
    }

    public void storePassword(String userName) {

        prefs.setItem(KEY_PASSWORD, userName);
    }

    public String getPassword() {

        return prefs.getItem(KEY_PASSWORD);
    }

    public void removePassword() {

        prefs.deleteItem(KEY_PASSWORD);
    }

    public boolean logout() {

        removeAuthenticationItem(KEY_TOKEN);

        return true;
    }

    public void saveAuthenticationItem(String key, String value) {

        prefs.setItem(key, value);
    }

    public String fetchAuthenticationString(String key) {

        return prefs.getItem(key);
    }

    public void removeAuthenticationItem(String key) {

        prefs.deleteItem(key);
    }
}

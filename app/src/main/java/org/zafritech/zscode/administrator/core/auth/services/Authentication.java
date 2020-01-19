package org.zafritech.zscode.administrator.core.auth.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;
import org.zafritech.zscode.administrator.core.utils.Preferences;

public class Authentication {

    private static Preferences prefs;
    private String accessToken;

    public static String KEY_SECRET = "secretKey";
    public static String KEY_ISSUER = "issuerKey";
    public static String KEY_TOKEN = "tokenKey";
    public static String KEY_USERNAME = "usernameKey";
    public static String KEY_PASSWORD = "passwordKey";
    public static String KEY_ENCRYPTED_UNAME = "EncrypedUsernameKey";
    public static String KEY_ENCRYPTED_UNAME_IV = "EncrypedUsernameIV";
    public static String KEY_ENCRYPTED_PWORD = "EncrypedPasswordKey";
    public static String KEY_ENCRYPTED_PWORD_IV = "EncrypedPasswordIV";

    public Authentication(Context context) {

        this.prefs = new Preferences(context);
    }

    public boolean validAuthToken() {

        // Reference: https://github.com/auth0/JWTDecode.Android

        String authJwtSecret = prefs.getItem(KEY_SECRET);
        String authJwtIssuer = prefs.getItem(KEY_ISSUER);
        String authToken = prefs.getItem(KEY_TOKEN);

        // DEBUG code!
//        System.out.println("KEY_SECRET: " + authJwtSecret);
//        System.out.println("KEY_ISSUER: " + authJwtIssuer);
//        System.out.println("KEY_TOKEN: " + authToken);

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

    public static void storeTokenKey(String tokenKey) {

        prefs.setItem("API_KEY", tokenKey);
    }

    public String getTokenKey() {

        return prefs.getItem("API_KEY");
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

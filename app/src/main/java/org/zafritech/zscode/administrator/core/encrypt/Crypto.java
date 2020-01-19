package org.zafritech.zscode.administrator.core.encrypt;

import android.content.Context;

public class Crypto implements CryptoService {

    // REF: https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3

    public static String RSA_MODE = "RSA/ECB/PKCS1Padding";
    public static Integer ANDROID_API_VERSION = android.os.Build.VERSION.SDK_INT;

    private Context context;
    private CryptoAPI22 api22;
    private CryptoAPI23 api23;

    public Crypto(Context context) {

        this.context = context;
        this.api22 = new CryptoAPI22(context);
        this.api23 = new CryptoAPI23(context);
    }

    @Override
    public String encrypt(String plainText) throws Exception {

        if (ANDROID_API_VERSION >= 23) {

            return  api23.encrypt(plainText);

        }else {

            return  api22.encrypt(plainText);

        }
    }

    @Override
    public String decrypt(String encrypted) throws Exception {

        if (ANDROID_API_VERSION >= 23) {

            return  api23.decrypt(encrypted);

        }else {

            return  api22.decrypt(encrypted);

        }
    }

}
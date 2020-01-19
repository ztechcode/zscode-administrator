package org.zafritech.zscode.administrator.core.encrypt;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.RequiresApi;

public class CryptoAPI23 {

    private static String KEY_STORE = "AndroidKeyStore";
    public static String ZSCODE_KEY_ALIAS = "ZTSAdminKey";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    public static String FIXED_INITIALISATION_VECTOR = "encryptionIntVec";
    public static final Charset CHAR_ENCODING = Charset.forName("UTF-8");

    private Context context;
    private KeyStore keyStore;

    public CryptoAPI23(Context context) {

        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String encrypt(String plaintext) throws Exception {

        // If it is the first run
        generateKeys();

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, FIXED_INITIALISATION_VECTOR.getBytes("UTF-8")));

        byte[] encodedBytes = cipher.doFinal(plaintext.getBytes(CHAR_ENCODING));

        String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);

        return  encryptedBase64Encoded;

    }

    public String decrypt(String encrypted) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(context), new GCMParameterSpec(128, FIXED_INITIALISATION_VECTOR.getBytes("UTF-8")));

        byte[] decodedBytes = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));

        return Base64.encodeToString(decodedBytes, Base64.DEFAULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKeys() throws KeyStoreException,
                                     CertificateException,
                                     NoSuchAlgorithmException,
                                     IOException,
                                     NoSuchProviderException,
                                     InvalidAlgorithmParameterException {

        keyStore = KeyStore.getInstance(KEY_STORE);
        keyStore.load(null);

        if (!keyStore.containsAlias(ZSCODE_KEY_ALIAS)) {

            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE);

            keyGenerator.init(new KeyGenParameterSpec.Builder(ZSCODE_KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            .build());

            keyGenerator.generateKey();
        }

    }

    private Key getSecretKey(Context context) throws Exception {

        keyStore = KeyStore.getInstance(KEY_STORE);
        keyStore.load(null);

        return keyStore.getKey(ZSCODE_KEY_ALIAS, null);
    }
}

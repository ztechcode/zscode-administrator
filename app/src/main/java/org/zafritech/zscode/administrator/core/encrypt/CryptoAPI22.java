package org.zafritech.zscode.administrator.core.encrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static org.zafritech.zscode.administrator.core.encrypt.Crypto.RSA_MODE;

public class CryptoAPI22 {

    public static final  String ZSCODE_KEY_ALIAS = "ZTSAdminKey";
    public static final String ENCRYPTED_KEY = "EncyptedKey";
    private static final String AES_MODE = "AES/ECB/PKCS7Padding";
    public static final Charset CHAR_ENCODING = Charset.forName("UTF-8");

    private Context context;
    private KeyStore keyStore;

    public CryptoAPI22(Context context) {

        this.context = context;
    }

    public String encrypt(String plainText) throws Exception {

        // If it is the first run
        generateKeys();

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(context));

        byte[] encodedBytes = cipher.doFinal(plainText.getBytes(CHAR_ENCODING));
        String encryptedBase64Encoded =  Base64.encodeToString(encodedBytes, Base64.DEFAULT);

        return encryptedBase64Encoded;
    }

    public String decrypt(String cipherText) throws Exception {

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(context));

        byte[] decodedBytes = cipher.doFinal(cipherText.getBytes(CHAR_ENCODING));

        return new String(decodedBytes, CHAR_ENCODING);

    }

    private Key getSecretKey(Context context) throws Exception{

        SharedPreferences pref = context.getSharedPreferences(ZSCODE_KEY_ALIAS, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null);

        // Need to check null, omitted here
        byte[] encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(encryptedKey);

        return new SecretKeySpec(key, "AES");
    }

    private void generateKeys() throws Exception {

        SharedPreferences pref = context.getSharedPreferences(ZSCODE_KEY_ALIAS, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null);

        if (enryptedKeyB64 == null) {

            byte[] key = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(key);

            byte[] encryptedKey = rsaEncrypt(key);
            enryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);

            SharedPreferences.Editor edit = pref.edit();
            edit.putString(ENCRYPTED_KEY, enryptedKeyB64);
            edit.commit();

        }
    }

    private byte[] rsaEncrypt(byte[] secret) throws InvalidKeyException,
                                                        NoSuchPaddingException,
                                                        NoSuchAlgorithmException,
                                                        NoSuchProviderException,
                                                        IOException,
                                                        KeyStoreException,
                                                        UnrecoverableEntryException {

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(ZSCODE_KEY_ALIAS, null);

        // Encrypt the text
        Cipher inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        byte[] bytes = outputStream.toByteArray();

        return bytes;
    }

    private byte[] rsaDecrypt(byte[] encrypted) throws UnrecoverableEntryException,
                                                        NoSuchAlgorithmException,
                                                        KeyStoreException,
                                                        NoSuchProviderException,
                                                        NoSuchPaddingException,
                                                        InvalidKeyException,
                                                        IOException {

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(ZSCODE_KEY_ALIAS, null);
        Cipher output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL");
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

        CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();

        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {

            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];

        for(int i = 0; i < bytes.length; i++) {

            bytes[i] = values.get(i).byteValue();
        }

        return bytes;
    }

}

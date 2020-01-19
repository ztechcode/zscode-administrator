package org.zafritech.zscode.administrator.core.encrypt;

public interface CryptoService {

    String encrypt(String plain) throws Exception;

    String decrypt(String encrypted) throws Exception;
}

package com.boruomi.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    // 设置密钥
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static String secretKey = "HMBrIP7kR6sx5ikKQzrfNw==";

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        AESUtil.secretKey = secretKey;
    }

    // 加密
    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // 解密
    public static String decrypt(String encryptedData, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }

    // 随机生成密钥（可选）
    public static String generateKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGen.init(keySize);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static void main(String[] args) {
        try {
            String s = generateKey(128);
            System.out.println("密钥: " + s);
            String key = "HMBrIP7kR6sx5ikKQzrfNw=="; // 必须为16字节长度
            String originalData = "Hello, AES!";

            // 加密
            String encryptedData = AESUtil.encrypt(originalData, key);
            System.out.println("加密后的数据: " + encryptedData);

            // 解密
            String decryptedData = AESUtil.decrypt(encryptedData, key);
            System.out.println("解密后的数据: " + decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

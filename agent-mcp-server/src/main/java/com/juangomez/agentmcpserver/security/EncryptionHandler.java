package com.juangomez.agentmcpserver.security;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionHandler {

    private final static String secretKey = "Vbck1Jflt/jAoJKT8JzjsvOVDDqHqki8kJgdPxt5o2g=";//System.getenv("ENCRYPTION_KEY");

    /**
     * Returns the private key value
     */
    private static SecretKey getKey() {
        byte[] keyBytes = secretKey.getBytes();
        byte[] key16 = new byte[16]; // AES-128
        System.arraycopy(keyBytes, 0, key16, 0, Math.min(keyBytes.length, 16));
        return new SecretKeySpec(key16, "AES");
    }

    private String generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecretKey key = keyGenerator.generateKey();
            System.out.println("Secret key generated: " + Base64.getEncoder().encodeToString(key.getEncoded()));
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key: " + e);
        }
    }

    public static String encrypt(String plain) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        byte[] encrypted = cipher.doFinal(plain.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, getKey());
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}

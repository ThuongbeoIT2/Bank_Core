package com.example.secumix.security.Encode;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class AESEncryptionExample {
    public static void main(String[] args) throws Exception {
        // Generate AES key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // 128, 192, or 256
        SecretKey secretKey = keyGenerator.generateKey();

        // Encrypt
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal("Hello, AES!".getBytes());
        String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedData);
        System.out.println("Encrypted: " + encryptedBase64);

        // Decrypt
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));
        System.out.println("Decrypted: " + new String(decryptedData));
    }
}

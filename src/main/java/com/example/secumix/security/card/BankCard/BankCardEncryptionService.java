package com.example.secumix.security.card.BankCard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

@Service
public class BankCardEncryptionService {

    private final ObjectMapper objectMapper;
    private final KeyPair keyPair;

    public BankCardEncryptionService() throws Exception {
        this.objectMapper = new ObjectMapper();
        this.keyPair = generateKeyPair();
    }

    // Phương thức này tạo một cặp khóa RSA (public key và private key)
    private KeyPair generateKeyPair() throws Exception {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new AssertionError(e);
        }
    }

    // Phương thức này sử dụng public key để mã hóa dữ liệu
    public String encryptBankCard(BankCard bankCard) throws Exception {
        String jsonData = objectMapper.writeValueAsString(bankCard);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encryptedBytes = cipher.doFinal(jsonData.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Phương thức này sử dụng private key để giải mã dữ liệu đã được mã hóa
    public BankCard decryptBankCard(String encryptedData) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedJson = new String(decryptedBytes);
        return objectMapper.readValue(decryptedJson, BankCard.class);
    }
}

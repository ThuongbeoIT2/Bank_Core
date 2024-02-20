package com.example.secumix.security.Encode;

import java.security.MessageDigest;
import java.util.Base64;

public class SHA256HashingExample {
    public static void main(String[] args) throws Exception {
        // Hashing with SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedData = digest.digest("Hello, SHA-256!".getBytes());
        String hashedBase64 = Base64.getEncoder().encodeToString(hashedData);
        System.out.println("SHA-256 Hash: " + hashedBase64);
    }
}

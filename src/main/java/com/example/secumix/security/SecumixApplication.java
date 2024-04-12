package com.example.secumix.security;


import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.card.BankCard.BankCard;
import com.example.secumix.security.card.BankCard.BankCardEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;


@SpringBootApplication

public class SecumixApplication implements CommandLineRunner {




    public static void main(String[] args) {
        SpringApplication.run(SecumixApplication.class, args);
//        "C:\Program Files\Google\Chrome\Application\chrome.exe" --disable-web-security --disable-gpu --user-data-dir=%LOCALAPPDATA%\Google\chromeTemp"


    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Runnnn...");
    }
}

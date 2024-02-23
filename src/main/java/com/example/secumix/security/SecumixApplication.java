package com.example.secumix.security;


import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.card.BankCard.BankCard;
import com.example.secumix.security.card.BankCard.BankCardEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class SecumixApplication implements CommandLineRunner {

    @Autowired
    private BankCardEncryptionService bankCardEncryptionService;


    public static void main(String[] args) {
        SpringApplication.run(SecumixApplication.class, args);
//        "C:\Program Files\Google\Chrome\Application\chrome.exe" --disable-web-security --disable-gpu --user-data-dir=%LOCALAPPDATA%\Google\chromeTemp"


    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Runnnnn");
        BankCard bankCard = new BankCard();
        bankCard.setBankCardID(123);
        bankCard.setCode("1234567890123456");
        bankCard.setCustomerName("John Doe");
        bankCard.setPinCode("123456");
        bankCard.setSurplus(1000L);
        bankCard.setCardStatus(true);
        bankCard.setCreatedAt(UserUtils.getCurrentDay());
        bankCard.setUpdatedAt(UserUtils.getCurrentDay());
        String encryptedData = bankCardEncryptionService.encryptBankCard(bankCard);
        System.out.println(encryptedData);
        BankCard decryptedBankCard = bankCardEncryptionService.decryptBankCard(encryptedData);
        System.out.println(decryptedBankCard);
    }
}

package com.example.secumix.security.bankentity.ATMBank;

import lombok.Data;

@Data
public class ATMBankRequest {
    private String Province;
    private String District;
    private String Ward;
    private int BankID;
}

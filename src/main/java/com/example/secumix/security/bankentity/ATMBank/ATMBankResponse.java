package com.example.secumix.security.bankentity.ATMBank;

import com.google.gson.annotations.SerializedName;

public class ATMBankResponse {
    @SerializedName("ATMCode")
    private String ATMCode;
    private String Province;
    private String District;
    private String Ward;
    private int BankID;

}

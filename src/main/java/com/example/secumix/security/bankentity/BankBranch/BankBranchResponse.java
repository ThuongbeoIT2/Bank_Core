package com.example.secumix.security.bankentity.BankBranch;

import lombok.Data;

@Data
public class BankBranchResponse {
    private String BankCode;
    private String BankBranchName;
    private String Province;
    private String District;
    private String Ward;
    private int BankID;
}

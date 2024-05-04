package com.example.secumix.security.modelapp.response;


import lombok.Data;
@Data
public class SpendingLimitResponse {

    private int spendId;
    private long remainingAmount;
    private long spendingLimit;

    private long expenditure;

    private String date;

    private String cateName;

    public void setRemainingAmount(long remainingAmount) {
        this.remainingAmount=this.spendingLimit-this.expenditure;
    }
}

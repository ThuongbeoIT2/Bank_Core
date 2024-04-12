package com.example.secumix.security.card.BankCard;

import lombok.Data;

@Data

public class BankCardResponse {
    private String Code;
    private String CustomerName;
    private Boolean CardStatus;
    private Long Surplus;
    private String BankBranchName;
    private String BankName;
}

package com.example.secumix.security.bankentity.BankIntroduce;

import lombok.Data;

@Data
public class BankIntroduceResponse {
    private String TitleIntroduce;

    private String AvatarIntroduce;

    private String Description;
    private int bankID;
}

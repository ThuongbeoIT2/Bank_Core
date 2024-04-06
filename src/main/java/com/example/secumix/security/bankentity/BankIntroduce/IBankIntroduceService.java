package com.example.secumix.security.bankentity.BankIntroduce;

import java.util.Optional;

public interface IBankIntroduceService {
    BankIntroduceResponse findBankIntroduceByBankID(int bankID);
    void Save(BankIntroduce bankIntroduce);
    void Insert(BankIntroduceRequest bankIntroduceRequest);

}

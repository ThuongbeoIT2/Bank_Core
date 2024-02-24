package com.example.secumix.security.bankentity.BankIntroduce;

import java.util.Optional;

public interface IBankIntroduceService {
    Optional<BankIntroduce> findBankIntroduceByBankID(int bankID);
    void Save(BankIntroduce bankIntroduce);
    void Insert(BankIntroduceRequest bankIntroduceRequest);
    void Update(BankIntroduceRequest bankIntroduceRequest);
}

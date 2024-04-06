package com.example.secumix.security.bankentity.ATMBank;

import java.util.List;
import java.util.Optional;

public interface IATMBankService {
    List<ATMBankResponse> getAll();
    List<ATMBankResponse> GetAllATMByBank(String Represent);


    Optional<ATMBankResponse> findATMBankByATMCode(String ATMCode);
    void Save(ATMBank atmBank);
    void Insert(ATMBankRequest atmBankRequest);
    void Update(ATMBank atmBank);
}

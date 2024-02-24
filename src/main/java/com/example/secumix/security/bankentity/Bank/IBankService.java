package com.example.secumix.security.bankentity.Bank;

import java.util.List;
import java.util.Optional;

public interface IBankService {
    List<Bank> getAllBank();
    Optional<Bank> findBankByBankID(int bankID);
    Optional<Bank> findBankByName(String BankName);
    Optional<Bank> findBankByRepresent(String Represent);
    void Save(Bank bank);
    void Insert(BankRequest bankRequest);
    void Update(BankRequest bankRequest, int id);
}

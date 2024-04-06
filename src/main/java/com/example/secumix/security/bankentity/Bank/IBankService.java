package com.example.secumix.security.bankentity.Bank;

import java.util.List;
import java.util.Optional;

public interface IBankService {
    List<BankRequest_Response> getAllBank();
    Optional<BankRequest_Response> findBankByBankID(int bankID);
    Optional<BankRequest_Response> findBankByName(String BankName);
    Optional<BankRequest_Response> findBankByRepresent(String Represent);
    void Save(Bank bank);
    void Insert(BankRequest_Response bankRequest);
    void Update(BankRequest_Response bankRequest, int id);
}

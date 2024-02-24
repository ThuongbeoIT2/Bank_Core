package com.example.secumix.security.bankentity.Bank;

import com.example.secumix.security.Utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BankService implements IBankService{
    @Autowired
    private BankRepository repository;
    @Override
    public List<Bank> getAllBank() {
        return repository.findAll();
    }

    @Override
    public Optional<Bank> findBankByBankID(int bankID) {
        return repository.findById(bankID);
    }

    @Override
    public Optional<Bank> findBankByName(String BankName) {
        return repository.findBankByName(BankName);
    }

    @Override
    public Optional<Bank> findBankByRepresent(String Represent) {
        return repository.findBankByRepresent(Represent);
    }

    @Override
    public void Save(Bank bank) {
        repository.save(bank);
    }

    @Override
    public void Insert(BankRequest bankRequest) {

        var newObj=Bank.builder()
                .Represent(bankRequest.getRepresent())
                .BankName(bankRequest.getBankName())
                .Logo(bankRequest.getLogo())
                .CreatedAt(UserUtils.getCurrentDay())
                .UpdatedAt(UserUtils.getCurrentDay())
                .build();
        repository.save(newObj);
    }

    @Override
    public void Update(BankRequest bankRequest,int id) {
        Optional<Bank> bank= repository.findById(id);
        bank.get().setBankName(bankRequest.getBankName());
        bank.get().setRepresent(bankRequest.getRepresent());
        bank.get().setUpdatedAt(UserUtils.getCurrentDay());
        repository.save(bank.get());
    }
}

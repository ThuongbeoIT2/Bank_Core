package com.example.secumix.security.bankentity.Bank;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankService implements IBankService{
    @Autowired
    private BankRepository repository;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<BankRequest_Response> getAllBank() {
        return repository.findAll().stream().map(bank -> {
            BankRequest_Response bankRequestResponse=modelMapper.map(bank,BankRequest_Response.class);
            return bankRequestResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<BankRequest_Response> findBankByBankID(int bankID) {
        return repository.findById(bankID).map(bank -> {
            BankRequest_Response bankRequestResponse = modelMapper.map(bank,BankRequest_Response.class);
            return bankRequestResponse;
        });
    }

    @Override
    public Optional<BankRequest_Response> findBankByName(String BankName) {
        return repository.findBankByName(BankName).map(bank -> {
            BankRequest_Response bankRequestResponse = modelMapper.map(bank,BankRequest_Response.class);
            return bankRequestResponse;
        });
    }

    @Override
    public Optional<BankRequest_Response> findBankByRepresent(String Represent) {
        return repository.findBankByRepresent(Represent).map(bank -> {
            BankRequest_Response bankRequestResponse = modelMapper.map(bank,BankRequest_Response.class);
            return bankRequestResponse;
        });
    }

    @Override
    public void Save(Bank bank) {
        repository.save(bank);
    }

    @Override
    public void Insert(BankRequest_Response bankRequest) {

        var newObj=Bank.builder()
                .Represent(bankRequest.getRepresent())
                .BankName(bankRequest.getBankName())
                .Logo(bankRequest.getLogo())

                .build();
        repository.save(newObj);
    }

    @Override
    public void Update(BankRequest_Response bankRequest, int id) {
        Optional<Bank> bank= repository.findById(id);
        bank.get().setBankName(bankRequest.getBankName());
        bank.get().setRepresent(bankRequest.getRepresent());
//
        repository.save(bank.get());
    }
}

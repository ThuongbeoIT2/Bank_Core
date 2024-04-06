package com.example.secumix.security.bankentity.ATMBank;

import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.bankentity.Bank.Bank;
import com.example.secumix.security.bankentity.Bank.BankRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ATMBankService implements IATMBankService{
    @Autowired
    private ATMBankRepository repository;
    @Autowired
    private BankRepository bankRepository;
    private ModelMapper modelMapper= new ModelMapper();
    @Override
    public List<ATMBankResponse> getAll() {
        return repository.findAll().stream().map(atmBank -> {
            ATMBankResponse atmBankResponse=modelMapper.map(atmBank,ATMBankResponse.class);
            return atmBankResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ATMBankResponse> GetAllATMByBank(String Represent) {
        return repository.GetAllATMByBank(Represent).stream().map(atmBank -> {
            ATMBankResponse atmBankResponse=modelMapper.map(atmBank,ATMBankResponse.class);
            return atmBankResponse;
        }).collect(Collectors.toList());
    }


    @Override
    public Optional<ATMBankResponse> findATMBankByATMCode(String ATMCode) {
        return repository.findATMBankByATMCode(ATMCode).map(atmBank -> {
            ATMBankResponse atmBankResponse=modelMapper.map(atmBank,ATMBankResponse.class);
            return atmBankResponse;
        });
    }

    @Override
    public void Save(ATMBank atmBank) {
        repository.save(atmBank);
    }

    @Override
    public void Insert(ATMBankRequest atmBankRequest) {
        ATMBank atmBank= new ATMBank();
        atmBank.setDistrict(atmBankRequest.getDistrict());
        atmBank.setProvince(atmBankRequest.getProvince());
        atmBank.setWard(atmBankRequest.getWard());
        Optional<Bank> bank= bankRepository.findById(atmBankRequest.getBankID());
        atmBank.setBank(bank.get());
        atmBank.setATMCode();
        repository.save(atmBank);
    }

    @Override
    public void Update(ATMBank atmBank) {
        repository.save(atmBank);
    }
}

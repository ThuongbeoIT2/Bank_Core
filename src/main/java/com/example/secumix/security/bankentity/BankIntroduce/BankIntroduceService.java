package com.example.secumix.security.bankentity.BankIntroduce;

import com.example.secumix.security.Utils.UserUtils;

import com.example.secumix.security.bankentity.Bank.Bank;
import com.example.secumix.security.bankentity.Bank.BankRepository;
import com.example.secumix.security.bankentity.Bank.IBankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankIntroduceService implements IBankIntroduceService{
    @Autowired
    private BankIntroduceRepository repository;
    @Autowired
    private BankRepository bankRepository;

    private ModelMapper modelMapper= new ModelMapper();
    @Override
    public BankIntroduceResponse findBankIntroduceByBankID(int bankID) {
        BankIntroduce bankIntroduce= repository.findBankIntroduceByBankID(bankID);
        BankIntroduceResponse bankIntroduceResponse= modelMapper.map(bankIntroduce,BankIntroduceResponse.class);
        return bankIntroduceResponse;
    }

    @Override
    public void Save(BankIntroduce bankIntroduce) {
        repository.save(bankIntroduce);
    }

    @Override
    public void Insert(BankIntroduceRequest bankIntroduceRequest) {
        Optional<Bank> bank = bankRepository.findById(bankIntroduceRequest.getBankID());
        var newObj= BankIntroduce.builder()
                .TitleIntroduce(bankIntroduceRequest.getTitleIntroduce())
                .AvatarIntroduce(bankIntroduceRequest.getAvatarIntroduce())
                .Description(bankIntroduceRequest.getDescription())
                .bank(bank.get())
                .build();
        repository.save(newObj);
    }


}

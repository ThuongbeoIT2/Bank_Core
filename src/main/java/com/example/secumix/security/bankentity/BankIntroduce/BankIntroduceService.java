package com.example.secumix.security.bankentity.BankIntroduce;

import com.example.secumix.security.Utils.UserUtils;

import com.example.secumix.security.bankentity.Bank.Bank;
import com.example.secumix.security.bankentity.Bank.IBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankIntroduceService implements IBankIntroduceService{
    @Autowired
    private BankIntroduceRepository repository;
    @Autowired
    private IBankService bankService;
    @Override
    public Optional<BankIntroduce> findBankIntroduceByBankID(int bankID) {
        return repository.findBankIntroduceByBankID(bankID);
    }

    @Override
    public void Save(BankIntroduce bankIntroduce) {
        repository.save(bankIntroduce);
    }

    @Override
    public void Insert(BankIntroduceRequest bankIntroduceRequest) {
        Optional<Bank> bank = bankService.findBankByBankID(bankIntroduceRequest.getBankID());
        var newObj= BankIntroduce.builder()
                .TitleIntroduce(bankIntroduceRequest.getTitleIntroduce())
                .AvatarIntroduce(bankIntroduceRequest.getAvatarIntroduce())
                .Description(bankIntroduceRequest.getDescription())
                .bank(bank.get())
                .CreatedAt(UserUtils.getCurrentDay())
                .UpdatedAt(UserUtils.getCurrentDay())
                .build();
        repository.save(newObj);
    }

    @Override
    public void Update(BankIntroduceRequest bankIntroduceRequest) {
        Optional<BankIntroduce> bankIntroduce = findBankIntroduceByBankID(bankIntroduceRequest.getBankID());
        bankIntroduce.get().setTitleIntroduce(bankIntroduceRequest.getTitleIntroduce());
        bankIntroduce.get().setDescription(bankIntroduceRequest.getDescription());
        bankIntroduce.get().setAvatarIntroduce(bankIntroduceRequest.getAvatarIntroduce());
        bankIntroduce.get().setUpdatedAt(UserUtils.getCurrentDay());
        Save(bankIntroduce.get());
    }
}

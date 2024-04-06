package com.example.secumix.security.bankentity.BankBranch;

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
public class BankBranchService implements IBankBranchService{
    @Autowired
    private BankBranchRepository bankBranchRepository;
    @Autowired
    private BankRepository bankRepository;
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<BankBranchResponse> getAll() {
        return bankBranchRepository.findAll().stream().map(bankBranch -> {
            BankBranchResponse bankBranchResponse=modelMapper.map(bankBranch,BankBranchResponse.class);
            return bankBranchResponse;
        }
        ).collect(Collectors.toList());
    }

    @Override
    public List<BankBranchResponse> GetAllBankBranchByBank(String Represent) {
        return bankBranchRepository.GetAllBranchByBank(Represent).stream().map(bankBranch -> {
                    BankBranchResponse bankBranchResponse=modelMapper.map(bankBranch,BankBranchResponse.class);
                    return bankBranchResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<BankBranchResponse> findBankBranchByBank(String BankCode) {
        return bankBranchRepository.findATMBankByBankCode(BankCode).map(bankBranch -> {
                    BankBranchResponse bankBranchResponse=modelMapper.map(bankBranch,BankBranchResponse.class);
                    return bankBranchResponse;
                }
        );
    }

    @Override
    public void Save(BankBranch bankBranch) {
        bankBranchRepository.save(bankBranch);
    }

    @Override
    public void Insert(BankBranchRequest bankBranchRequest) {
         BankBranch bankBranch= new BankBranch();
         bankBranch.setDistrict(bankBranchRequest.getDistrict());
         bankBranch.setProvince(bankBranchRequest.getProvince());
         bankBranch.setWard(bankBranchRequest.getWard());
         Bank bank= bankRepository.findById(bankBranchRequest.getBankID()).get();
         bankBranch.setBank(bank);
         bankBranch.setBankBranchName(bankBranchRequest.getBankBranchName());
         bankBranch.setBankCode();
         bankBranchRepository.save(bankBranch);
    }
/* Chỉ cho phép sửa tên */
    @Override
    public void Update(BankBranchRequest bankBranchRequest,int ID) {
        BankBranch bankBranch= bankBranchRepository.findById(ID).get();
        bankBranch.setBankBranchName(bankBranchRequest.getBankBranchName());
        bankBranchRepository.save(bankBranch);
    }
}

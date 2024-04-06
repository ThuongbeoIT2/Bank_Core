package com.example.secumix.security.bankentity.BankBranch;


import java.util.List;
import java.util.Optional;

public interface IBankBranchService {
    List<BankBranchResponse> getAll();
    List<BankBranchResponse> GetAllBankBranchByBank(String Represent);
//    List<BankBranch> GetAllATMByProvine(String Province);
//    List<BankBranch> GetAllATMByProvineAndPresent(String Province,String Represent);
    Optional<BankBranchResponse> findBankBranchByBank(String BankCode);
    void Save(BankBranch bankBranch);
    void Insert(BankBranchRequest bankBranchRequest);
    void Update(BankBranchRequest bankBranchRequest,int ID);
}

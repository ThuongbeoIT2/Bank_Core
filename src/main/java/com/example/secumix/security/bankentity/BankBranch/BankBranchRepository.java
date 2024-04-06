package com.example.secumix.security.bankentity.BankBranch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch,Integer> {
    @Query("select o from bankbranch o where o.bank.Represent=:Represent")
    List<BankBranch> GetAllBranchByBank(String Represent);

    @Query("select o from bankbranch o where o.BankCode=:BankCode")
    Optional<BankBranch> findATMBankByBankCode(String BankCode);
}

package com.example.secumix.security.bankentity.ATMBank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ATMBankRepository extends JpaRepository<ATMBank,Integer> {
    @Query("select o from atmbank o where o.bank.Represent=:Represent")
    List<ATMBank> GetAllATMByBank(String Represent);
    @Query("select o from atmbank o where o.ATMCode=:ATMCode")
    Optional<ATMBank> findATMBankByATMCode(String ATMCode);
}

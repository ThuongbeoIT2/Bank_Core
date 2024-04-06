package com.example.secumix.security.bankentity.BankIntroduce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankIntroduceRepository extends JpaRepository<BankIntroduce,Integer> {
    @Query("select o from bankintroduce o where o.bank.BankID=:bankID")
   BankIntroduce findBankIntroduceByBankID(int bankID);
}

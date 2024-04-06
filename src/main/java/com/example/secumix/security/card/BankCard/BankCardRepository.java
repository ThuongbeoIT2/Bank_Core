package com.example.secumix.security.card.BankCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard,Integer> {
    @Query("select o from bankcard o where o.Code=:Code")
    Optional<BankCard> findBankCardByCode(String Code);
    @Query("select o from bankcard o where o.userID=:userID")
    List<BankCard> findBankCardByUser(String userID);
    @Query("select o from bankcard o where o.userID=:userID and o.bankBranch.bank.Represent=:Represent")
    List<BankCard> findBankCardByUserAndBank(int userID,String Represent);
}

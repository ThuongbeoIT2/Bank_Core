package com.example.secumix.security.bankentity.Bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank,Integer> {
    @Query("select o from bank o where o.BankName=:BankName")
    Optional<Bank> findBankByName(String BankName);
    @Query("select o from bank o where o.Represent=:Represent")
    Optional<Bank> findBankByRepresent(String Represent);
}

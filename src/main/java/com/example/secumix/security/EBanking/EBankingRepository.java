package com.example.secumix.security.EBanking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EBankingRepository extends JpaRepository<Ebanking,Integer> {
    @Query("select o from ebanking o where o.PhoneID=:Phonenumber")
    Optional<Ebanking> findbyPhoneNumber(String Phonenumber);

}

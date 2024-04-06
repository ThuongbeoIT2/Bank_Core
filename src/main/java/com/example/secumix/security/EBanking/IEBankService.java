package com.example.secumix.security.EBanking;




import java.util.Optional;

public interface IEBankService {
    Optional<Ebanking> findByPhone(String Phonenumber);

}

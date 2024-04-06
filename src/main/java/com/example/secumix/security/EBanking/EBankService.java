package com.example.secumix.security.EBanking;

import com.example.secumix.security.config.JwtService;

import com.example.secumix.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class EBankService implements IEBankService{
    @Autowired
    private EBankingRepository eBankingRepository;
    @Autowired
    private UserRepository userService;
    @Override
    public Optional<Ebanking> findByPhone(String Phonenumber) {
        return eBankingRepository.findbyPhoneNumber(Phonenumber);
    }


}

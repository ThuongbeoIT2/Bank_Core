package com.example.secumix.security.card.BankCard;

import com.example.secumix.security.bankentity.Bank.IBankService;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankCardService implements ICardService{

    @Autowired
    private UserService userService;
    @Autowired
    private BankCardRepository bankCardRepository;
    @Override
    public List<BankCard> listCardByUser(String username, String Represent) {
        Optional<User> user= userService.FindByEmail(username);
        List<BankCard> rs = bankCardRepository.findBankCardByUserAndBank(user.get().getId(),Represent);
        return rs;
    }

    @Override
    public Optional<BankCard> findBankCardByCode(String Code) {
        return bankCardRepository.findBankCardByCode(Code);
    }
}

package com.example.secumix.security.card.BankCard;

import java.util.List;
import java.util.Optional;

public interface ICardService {
    List<BankCard> listCardByUser(String username,String Represent);
    Optional<BankCard> findBankCardByCode(String Code);

}

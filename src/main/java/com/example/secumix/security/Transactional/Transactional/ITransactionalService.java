package com.example.secumix.security.Transactional.Transactional;

import com.example.secumix.security.card.BankCard.BankCard;

public interface ITransactionalService {
    void Save(BankCard bankCardA,BankCard bankCardB,Long amount,String message);
}

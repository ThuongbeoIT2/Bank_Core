//package com.example.secumix.security.Transactional.Transactional;
//
//import com.example.secumix.security.card.BankCard.BankCard;
//import com.example.secumix.security.card.BankCard.BankCardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TransactionalService implements ITransactionalService{
//    @Autowired
//    private TransactionalRepository transactionalRepository;
//    @Autowired
//    private BankCardRepository bankCardRepository;
//    @Override
//    public void Save(BankCard bankCardA, BankCard bankCardB, Long amount, String message) {
//        Transactional transactional= new Transactional(bankCardA,bankCardB,amount,message);
//        transactionalRepository.save(transactional);
//        bankCardRepository.save(bankCardA);
//        bankCardRepository.save(bankCardB);
//    }
//}

package com.example.secumix.security.Transactional.TransactionalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionalService implements ITranTypeService{
    @Autowired
    private TranTypeRepository tranTypeRepository;
    @Override
    public Optional<TransactionalType> findTypeByName(String typename) {
        Optional<TransactionalType> rs= tranTypeRepository.findTypeByName(typename);
        return rs;
    }

    @Override
    public List<TransactionalType> getAllTranType() {
        return tranTypeRepository.findAll();
    }

    @Override
    public void Save(TransactionalType transactionalType) {
        tranTypeRepository.save(transactionalType);
    }

    /* Chỉ cập nhận Description */
    @Override
    public void UpdateTransactionalType(TransactionalTypeRequest transactionalTypeRequest) {
       Optional<TransactionalType>  rs=   tranTypeRepository.findTypeByName(transactionalTypeRequest.getTypeName());
            rs.get().setDescription(transactionalTypeRequest.getDescription());
            tranTypeRepository.save(rs.get());
    }
}

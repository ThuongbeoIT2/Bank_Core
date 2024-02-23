package com.example.secumix.security.Transactional.TransactionalType;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITranTypeService {
    Optional<TransactionalType> findTypeByName(@Param("typename") String typename);
    List<TransactionalType> getAllTranType();
    void Save(TransactionalType transactionalType);
    void UpdateTransactionalType(TransactionalTypeRequest transactionalTypeRequest);
}

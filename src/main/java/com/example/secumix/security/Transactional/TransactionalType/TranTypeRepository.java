package com.example.secumix.security.Transactional.TransactionalType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranTypeRepository extends JpaRepository<TransactionalType,Integer> {
    @Query("select o from transactionaltype o where o.TypeName=:typename")
    Optional<TransactionalType> findTypeByName( String typename);

}

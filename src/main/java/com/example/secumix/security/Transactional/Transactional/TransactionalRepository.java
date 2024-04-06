package com.example.secumix.security.Transactional.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionalRepository extends JpaRepository<Transactional,Integer> {
}

package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepo extends JpaRepository<Pay, Integer> {
}

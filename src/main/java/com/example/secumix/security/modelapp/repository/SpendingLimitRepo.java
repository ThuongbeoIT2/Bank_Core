package com.example.secumix.security.modelapp.repository;


import com.example.secumix.security.modelapp.entities.SpendingLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SpendingLimitRepo extends JpaRepository<SpendingLimit,Integer> {
    @Query("select o from spendingLimit o where o.category.cateName=:catename and o.user.email=:email")
    Optional<SpendingLimit> findByUserEmailAndCategoryName(String email, String catename);
    @Query("select o from spendingLimit o where o.user.email=:email")
    List<SpendingLimit> getAllByEmail(String email);
}

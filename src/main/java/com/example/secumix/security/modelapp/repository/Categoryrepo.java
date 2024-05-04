package com.example.secumix.security.modelapp.repository;


import com.example.secumix.security.modelapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface Categoryrepo extends JpaRepository<Category,Integer> {
    @Query("select o from category o where o.cateName=:name" )
    Optional<Category> findCategoriesByName(String name);
}

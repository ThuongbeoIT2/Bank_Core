package com.example.secumix.security.modelapp.repository;


import com.example.secumix.security.modelapp.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface TradeRepo extends JpaRepository<Trade,Integer> {
    @Query("select o from trade o where o.user.email=:email")
    List<Trade> findTradeByUser(String email);
    @Query("select o from trade o where o.user.email=:email and o.category.cateName=:cate")
    List<Trade> findTradeByUserCate(String email,String cate);
    @Query("SELECT t FROM trade t WHERE t.user.email = :email AND YEAR(t.createdAt) = YEAR(CURRENT_DATE()) AND MONTH(t.createdAt) = MONTH(CURRENT_DATE())")
    List<Trade> findTradeByUserAndCurrentMonth(@Param("email") String email);
    @Query("SELECT t FROM trade t WHERE t.user.email = :email AND YEAR(t.createdAt) = :year AND MONTH(t.createdAt) = :month")
    List<Trade> findTradeByUserAndYearMonth(@Param("email") String email, @Param("year") int year, @Param("month") int month);


}

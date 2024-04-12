package com.example.secumix.security.store.repository;



import com.example.secumix.security.store.model.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
    @Query("select o from orderdetail o where o.cart.user.email=:email")
    List<OrderDetail> getAllByUser(String email);
    @Query("select o from orderdetail o where o.orderDetailId=:orderdetailid and o.cart.user.email=:email")
    Optional<OrderDetail> findByIDandUser(int orderdetailid, String email);
    @Query("select o from orderdetail o where o.storeName=:storeName")
    List<OrderDetail> getAllByStore(String storeName);

}

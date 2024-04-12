package com.example.secumix.security.store.repository;

import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("select o from product o where o.store.storeId=:storeid and o.productName=:name")
    Optional<Product> findByName(int storeid, String name);
    @Query("select o from product o where o.productName=:name")
    List<Product> findByProductName( String name);
    @Query("select o from product o where o.store.emailmanager=:email")
    List<Product> getAllByStore(String email);
    @Query("select o from product o where o.productName like %:key%")
    List<Product> SearchByKey(String key);
    @Query("select o from product o where o.productType.productTypeId=:producttypeid")
    List<Product> findByProductType(int producttypeid);
}

package com.example.secumix.security.store.repository;

import com.example.secumix.security.store.model.entities.ImportDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ImportDetailRepo extends JpaRepository<ImportDetail, Integer> {
    @Query("select o from importdt o where  o.product.store.storeId=:storeid")
    List<ImportDetail> findByStore(int storeid);
    @Query("select o from importdt o where  o.product.store.storeId=:storeid and o.product.productName=:productname")
    List<ImportDetail> findByStoreandProduct(int storeid, String productname);
}

package com.example.secumix.security.store.model.services;


import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.response.ProductTypeResponse;

import java.util.List;
import java.util.Optional;

public interface IProductTypeService {
    List<ProductTypeResponse> getAllProductType();
    Optional<ProductType> findProductTypeByName(String name);
    void Save(ProductType productType);

}

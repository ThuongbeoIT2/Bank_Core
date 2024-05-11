package com.example.secumix.security.store.services.impl;



import com.example.secumix.security.store.model.response.ProductResponse;

import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.repository.ProductRepo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<ProductResponse> getAllProduct() {
        return productRepo.findAll().stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setTitle(product.getTitle());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> GetAllByStore() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return productRepo.getAllByStore(email).stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setTitle(product.getTitle());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> findbyId(int id) {
        return productRepo.findById(id).map(
                product -> {
                    product.setView(product.getView()+1);
                    productRepo.save(product);
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setTitle(product.getTitle());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        );
    }

    @Override
    public List<ProductResponse> SearchByKey(String keyword) {
        return productRepo.SearchByKey(keyword).stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setTitle(product.getTitle());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findByProductType(int producttypeid) {
        return productRepo.findByProductType(producttypeid).stream().map(
                product -> {
                    ProductResponse productResponse = new ProductResponse();
                    productResponse.setAvatarProduct(product.getAvatarProduct());
                    productResponse.setProductName(product.getProductName());
                    productResponse.setProductType(product.getProductType().getProductTypeName());
                    productResponse.setQuantity(product.getQuantity());
                    productResponse.setStoreName(product.getStore().getStoreName());
                    productResponse.setTitle(product.getTitle());
                    productResponse.setPrice(product.getPrice());
                    productResponse.setStatus(product.getStatus());
                    productResponse.setDiscount(product.getDiscount());
                    productResponse.setView(product.getView());
                    return productResponse;
                }
        ).collect(Collectors.toList());
    }
}

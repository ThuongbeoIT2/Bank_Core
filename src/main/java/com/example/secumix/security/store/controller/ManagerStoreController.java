package com.example.secumix.security.store.controller;




import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.response.ImportResponse;
import com.example.secumix.security.store.model.services.IProductService;
import com.example.secumix.security.store.repository.ImportDetailRepo;
import com.example.secumix.security.store.repository.ProductRepo;
import com.example.secumix.security.store.repository.StoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/manager/store")
public class ManagerStoreController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ImportDetailRepo importDetailRepo;
    @Autowired
    private StoreRepo storeRepo;
    @Autowired
    private ProductRepo productRepo;
    @GetMapping(value = "/getallproduct")
    ResponseEntity<ResponseObject>GetAllProductByStore(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Cac san pham torng cua hang cua ban.",productService.GetAllByStore())
        );
    }
    @GetMapping(value = "/import/{storeid}/getall")
    ResponseEntity<ResponseObject> GetAllImport(@PathVariable int storeid){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Store> store= storeRepo.findStoreById(storeid);
        if (store.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK","Không có cửa hàng này.","")
            );
        }
        if (!email.equals(store.get().getEmailmanager())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK","Không phải cửa hàng của m.","")
            );
        }
        List<ImportResponse> importResponses=importDetailRepo.findByStore(storeid).stream().map(
                importDetail -> {
                    ImportResponse importResponse= new ImportResponse();
                    importResponse.setImportDetailId(importDetail.getImportDetailId());
                    importResponse.setQuantity(importDetail.getQuantity());
                    importResponse.setPrice(importDetail.getPrice());
                    importResponse.setProductName(importDetail.getProduct().getProductName());
                    importResponse.setStoreName(store.get().getStoreName());
                    importResponse.setCreatedAt(importDetail.getCreatedAt());
                    importResponse.setPriceTotal(importDetail.getPriceTotal());
                    return importResponse;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Thành công.",importResponses)
        );
    }
    @GetMapping(value = "/inport/{storeid}/{productname}")
    ResponseEntity<ResponseObject> GetAllImport(@PathVariable int storeid,
                                                @PathVariable String productname){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Store> store= storeRepo.findStoreById(storeid);
        Optional<Product> product= productRepo.findByName(storeid,productname);

        if (store.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK","Không có cửa hàng này.","")
            );
        }
        if (!email.equals(store.get().getEmailmanager())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK","Không phải cửa hàng của m.","")
            );
        }
        if (product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK","Không có sản phẩm này.","")
            );
        }
        List<ImportResponse> importResponses=importDetailRepo.findByStoreandProduct(storeid,productname).stream().map(
                importDetail -> {
                    ImportResponse importResponse= new ImportResponse();
                    importResponse.setImportDetailId(importDetail.getImportDetailId());
                    importResponse.setQuantity(importDetail.getQuantity());
                    importResponse.setPrice(importDetail.getPrice());
                    importResponse.setProductName(importDetail.getProduct().getProductName());
                    importResponse.setStoreName(store.get().getStoreName());
                    importResponse.setCreatedAt(importDetail.getCreatedAt());
                    importResponse.setPriceTotal(importDetail.getPriceTotal());
                    return importResponse;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Thành công.",importResponses)
        );
    }

}

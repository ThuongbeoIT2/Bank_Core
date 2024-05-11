package com.example.secumix.security.store.controller;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.Utils.UserUtils;


import com.example.secumix.security.store.model.entities.*;
import com.example.secumix.security.store.services.IProductService;
import com.example.secumix.security.store.services.IProductTypeService;
import com.example.secumix.security.store.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class ProductController {

    @Value("${default_avt}")
    private String defaultAvt;

    @Autowired
    private IProductTypeService productTypeService;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductImageRepo productImageRepo;
    @Autowired
    private ImportDetailRepo importDetailRepo;

    @Autowired
    private StoreRepo storeRepo;
    @Autowired
    private StoreTypeRepo storeTypeRepo;
    @Autowired
    private ProductTypeRepo productTypeRepo;

    @Autowired
    IProductService productService;
    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }
    @GetMapping(value = "/managerment/producttype/getall")
    ResponseEntity<ResponseObject> getAllProductType(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Thành công",productTypeService.getAllProductType())
        );
    }

    @PostMapping(value = "/import/{storeid}/insert")
    ResponseEntity<ResponseObject> importInsert(@RequestParam String productname,
                                                @RequestParam int priceIn,
                                                @RequestParam(required = false) Integer priceOut,
                                                @RequestParam(required = false) String producttypename,
                                                @RequestParam int quantity,
                                                @PathVariable int storeid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        if (quantity <= 0)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Quantity must > 0", "")
            );
        Optional<Store> store = storeRepo.findStoreById(storeid);
        if (store.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Store does not exsit", "")
            );
        if (!store.get().getEmailmanager().equals(email)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Bạn cần là chủ của cửa hàng :"+store.get().getStoreName(), "")
            );
        }
        Optional<Product> product = productRepo.findByName(storeid, productname);
        Product finalProduct = new Product();
        if(product.isEmpty()){
            if (priceOut == null) {
                return ResponseEntity.badRequest().body(new ResponseObject("error", "Missing required priceOut for new product", null));
            }
            if (producttypename == null) {
                return ResponseEntity.badRequest().body(new ResponseObject("error", "Missing required producttypename for new product", null));
            }
            Optional<ProductType> productType = productTypeRepo.findProductTypeByName(producttypename.toLowerCase());
            if (productType.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED", "Producttype does not exsit", "")
                );
            Product newObj = Product.builder()
                    .avatarProduct(defaultAvt)
                    .quantity(quantity)
                    .discount(0)
                    .productType(productType.get())
                    .store(store.get())
                    .view(0)
                    .status(0)
                    .price(priceOut)
                    .createdAt(UserUtils.getCurrentDay())
                    .updatedAt(UserUtils.getCurrentDay())
                    .productName(productname)
                    .title(productname+ "--"+store.get().getStoreName())
                    .build();
            finalProduct = newObj;
            productRepo.save(newObj);
        }
        if(product.isPresent()) {
            //trường hợp có product trong kho
            Product updateProduct = product.get();
            updateProduct.setQuantity(updateProduct.getQuantity() + quantity);
            updateProduct.setUpdatedAt(UserUtils.getCurrentDay());
            if (priceOut != null) {
                updateProduct.setPrice(priceOut); // Cập nhật giá nếu được cung cấp
            }
            productRepo.save(updateProduct);
            finalProduct = product.get();
        }

        ImportDetail importDetail = ImportDetail.builder()
                .product(finalProduct)
                .updatedAt(UserUtils.getCurrentDay())
                .createdAt(UserUtils.getCurrentDay())
                .quantity(quantity)
                .price(priceIn)
                .priceTotal(quantity*priceIn)
                .build();
        importDetailRepo.save(importDetail);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Thành Công", "")
        );
    }

    @PostMapping(value = "/management/product/{storeid}/{producttypename}/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestParam MultipartFile avatar ,
                                                 @RequestParam String name,
                                                 @RequestParam String describe,
                                                 @PathVariable int storeid,
                                                 @PathVariable String producttypename
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Store> store= storeRepo.findStoreById(storeid);
        Optional<ProductType> productType= productTypeRepo.findProductTypeByName(producttypename);
        if (productType.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED","Không tìm thấy kiểu sản phẩm "+ producttypename,"")
            );
        }
        if (store.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND","Khong tim thay cua hang cua ban","")
            );
        }
        if (!email.equals(store.get().getEmailmanager())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Bạn không phải chủ của cửa hàng","")
            );
        }
        Optional<Product> product = productRepo.findByName(storeid,name);
        if(product.isPresent())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Product name is present","")
            );

        try {
            Map<String, Object> uploadResult = upload(avatar);
            Product newObj = Product.builder()
                    .avatarProduct(uploadResult.get("secure_url").toString())
                    .discount(0)
                    .store(store.get())
                    .view(0)
                    .status(0)
                    .productType(productType.get())
                    .createdAt(UserUtils.getCurrentDay())
                    .updatedAt(UserUtils.getCurrentDay())
                    .productName(name)
                    .title(describe)
                    .build();
            productRepo.save(newObj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Thành Công","")
            );
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Upload không thành công", "")
            );
        }
    }


    @GetMapping(value = "/product/info/{productid}")
    ResponseEntity<ResponseObject> GetInfoProduct(@PathVariable int productid){
        Optional<Product> product = productRepo.findById(productid);
        if (product.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Thành Công",productService.findbyId(productid))
            );
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED", "Không tồn tại sản phẩm trên", "")
            );
        }
    }

    @GetMapping(value = "/producttype/{producttypeid}")
    ResponseEntity<ResponseObject> GetAllProductByType(@PathVariable int producttypeid){
        Optional<ProductType> productType= productTypeRepo.findById(producttypeid);
        if (productType.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cakjfdskfjdk",productService.findByProductType(producttypeid))
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("OK","Không có đâu","")
            );
        }
    }
    @PostMapping(value = "/productimage/insert")
    ResponseEntity<ResponseObject> insertProductImage(@RequestParam MultipartFile image,
                                                      @RequestParam String title,
                                                      @RequestParam String status,
                                                      @RequestParam String productId){
        Optional<Product> product = productRepo.findById(Integer.valueOf(productId));
        if(product.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject("FAILED","Cannot find product","")
        );
        try {
            Map<String, Object> uploadResult = upload(image);
            ProductImage productImage = ProductImage.builder()
                    .imageProduct(uploadResult.get("secure_url").toString())
                    .status(Integer.parseInt(status))
                    .title(title)
                    .product(product.get())
                    .build();
            productImageRepo.save(productImage);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Thành Công","")
            );
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Upload không thành công","")
            );
        }

    }
    @PostMapping(value = "/search")
    ResponseEntity<ResponseObject> SearchByKey(@RequestParam String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Tim kiem thanh cong",productService.SearchByKey(keyword))
        );
    }


    @PostMapping(value = "/producttype/insert")
    ResponseEntity<ResponseObject> insertProductType(@RequestParam String name,
                                                     @RequestParam MultipartFile avatar,
                                                     @RequestParam String storetypename){
        Optional<StoreType> storeType = storeTypeRepo.findByName(storetypename);
        if(storeType.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Storetype does not exist","")
            );
        Optional<ProductType> RS= productTypeService.findProductTypeByName(name);
        if (RS.isEmpty()){
            try {
                Map<String, Object> uploadResult = upload(avatar);
                ProductType productType=ProductType.builder()
                        .productTypeName(name)
                        .storeType(storeType.get())
                        .productTypeImg(uploadResult.get("secure_url").toString())
                        .createdAt(UserUtils.getCurrentDay())
                        .build();
                productTypeService.Save(productType);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Thành Công","")
                );
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseObject("FAILED","Upload không thành công","")
                );
            }
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Thất bại","")
            );
        }

    }
}

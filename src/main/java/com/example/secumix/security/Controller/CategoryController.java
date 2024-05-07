package com.example.secumix.security.Controller;

import com.cloudinary.Cloudinary;

import com.example.secumix.security.modelapp.entities.Category;
import com.example.secumix.security.modelapp.repository.Categoryrepo;
import com.example.secumix.security.modelapp.response.CateRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CategoryController {
    @Autowired
    private Categoryrepo categoryrepo;
    @Autowired
    private Cloudinary cloudinary;
    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }
    @GetMapping(value = "/category/getall")
    ResponseEntity<List<CateRespone>> getAllCate(){
        List<CateRespone> cateRespones= categoryrepo.findAll().stream().map(
                category -> {
                    CateRespone cateRespone= new CateRespone();
                    cateRespone.setCateName(category.getCateName());
                    cateRespone.setUrlImage(category.getUrlImage());
                    cateRespone.setCateId(category.getCateId());
                    return cateRespone;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(cateRespones);
    }
    @GetMapping(value = "/category/getallbyspend")
    ResponseEntity<List<CateRespone>> getAllCateBySpending(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<CateRespone> cateRespones= categoryrepo.getCategoriesBySpendingLimit(email).stream().map(
                category -> {
                    CateRespone cateRespone= new CateRespone();
                    cateRespone.setCateName(category.getCateName());
                    cateRespone.setUrlImage(category.getUrlImage());
                    cateRespone.setCateId(category.getCateId());
                    return cateRespone;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(cateRespones);
    }
    @GetMapping(value = "/category/infodetail/{cateid}")
    ResponseEntity<Optional<CateRespone>> getAllCate(@PathVariable int cateid){
        Optional<CateRespone> cateRespones= categoryrepo.findById(cateid).map(
                category -> {
                    CateRespone cateRespone= new CateRespone();
                    cateRespone.setCateName(category.getCateName());
                    cateRespone.setUrlImage(category.getUrlImage());
                    cateRespone.setCateId(category.getCateId());
                    return cateRespone;
                }
        );
        return ResponseEntity.status(HttpStatus.OK).body(cateRespones);
    }
    @PostMapping(value = "/admin/category/insert")
    ResponseEntity<String> InsertCate(@RequestParam String catename,
                                      @RequestParam MultipartFile urlImage){
        Optional<Category> RS = categoryrepo.findCategoriesByName(catename);
        if (RS.isEmpty()){
            try {
                Map<String, Object> uploadResult = upload(urlImage);
                Category category = new Category();
                category.setUrlImage(uploadResult.get("secure_url").toString());
                category.setCateName(catename);
                categoryrepo.save(category);
                return ResponseEntity.status(HttpStatus.OK).body("Thành công");
            }catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload không thành công");
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }
    @GetMapping(value = "/category/delete/{cateid}")
    ResponseEntity<String> delete(@PathVariable int cateid){
        Optional<Category> category = categoryrepo.findById(cateid);
        categoryrepo.delete(category.get());
        return ResponseEntity.status(HttpStatus.OK).body("Thành công");
    }
}

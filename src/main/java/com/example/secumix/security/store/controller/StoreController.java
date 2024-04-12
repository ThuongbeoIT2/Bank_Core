package com.example.secumix.security.store.controller;

import com.cloudinary.Cloudinary;
import com.example.secumix.security.ResponseObject;


import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.store.repository.StoreRepo;
import com.example.secumix.security.store.repository.StoreTypeRepo;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/management")
public class StoreController {
    @Autowired
    private StoreTypeRepo storeTypeRepo;

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private NotifyRepository notifyRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private UserRepository userRepository;

    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }

    @PostMapping(value = "/store/{storetypeid}/insert")
    public ResponseEntity<ResponseObject> insertStore(@RequestParam String address,
                                                      @RequestParam MultipartFile image,
                                                      @RequestParam String phonenumber,
                                                      @RequestParam String name,
                                                      @PathVariable int storetypeid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user= userRepository.findByEmail(email).get();
        Optional<Store> store = storeRepo.findByPhonenumber(phonenumber);
        Optional<StoreType> storeType = storeTypeRepo.findById(storetypeid);

        if(store.isPresent())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Store phonenumber alreasy exsis","")
            );
        if(storeType.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Not exist storetype","")
            );
        Map<String, Object> uploadResult = upload(image);

        Store newObj = Store.builder()
                .address(address)
                .storeName(name)
                .rate(5)
                .emailmanager(email)
                .phoneNumber(phonenumber)
                .storeType(storeType.get())
                .image(uploadResult.get("secure_url").toString())
                .build();
        storeRepo.save(newObj);
        Notify notify= Notify.builder()
                .description("Cửa hàng của bạn đã được mở".concat(newObj.getStoreName())) //Truyền link đến store đó
                .user(user)
                .build();
        notifyRepository.save(notify);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Success","")
        );
    }

    @PostMapping(value = "/admin/storetype/insert")
    public ResponseEntity<ResponseObject> insertStoreType(@RequestParam String name
    ){
        Optional<StoreType> store = storeTypeRepo.findByName(name);
        if(store.isPresent()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("FAILED","Storetype exsis","")
            );
        }
        StoreType storeType = StoreType.builder()
                .storeTypeName(name.toUpperCase())
                .build();
        storeTypeRepo.save(storeType);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Scucess","")
        );
    }
}

package com.example.secumix.security.store.controller;

import com.example.secumix.security.ResponseObject;


import com.example.secumix.security.store.model.entities.OrderStatus;
import com.example.secumix.security.store.repository.OrderStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/management/orderstatus")
public class OrderStatusController {
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @PostMapping(value = "/insert")
    ResponseEntity<ResponseObject> InsertStatus(@RequestParam String name){
        Optional<OrderStatus> orderStatus= orderStatusRepo.findbyName(name);
        if (orderStatus.isEmpty()){
            OrderStatus newObj= OrderStatus.builder()
                    .orderStatusName(name.trim().toUpperCase())
                    .build();
            orderStatusRepo.save(newObj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Thêm thành công","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","ljhdfldshf","")
            );
        }
    }
}

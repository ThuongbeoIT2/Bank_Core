package com.example.secumix.security.store.controller;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.entities.OrderDetail;
import com.example.secumix.security.store.model.services.IOrderService;
import com.example.secumix.security.store.repository.OrderDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/shipper")
public class ShipperController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderDetailRepo orderDetailRepo;
    @GetMapping(value = "/changestatus1/{orderdetailid}")
    ResponseEntity<ResponseObject> Changestatus1(@PathVariable int orderdetailid){
        Optional<OrderDetail> orderDetail= orderDetailRepo.findById(orderdetailid);
        if (orderDetail.isPresent()){
            if (orderDetail.get().getShipperid()==null){
                orderService.ChangeStatus1(orderdetailid);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Nhận đơn thành công","")
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("NOT FOUND","Đã có người nhận đơn này","")
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("NOT FOUND","Khong tim thay","")
        );
    }
    @GetMapping(value = "/changestatus2/{orderdetailid}")
    ResponseEntity<ResponseObject> Changestatus2(@PathVariable int orderdetailid){
        Optional<OrderDetail> orderDetail= orderDetailRepo.findById(orderdetailid);
        if (orderDetail.isPresent()){
            if (orderDetail.get().getShipperid()!=null){
                orderService.ChangeStatus2(orderdetailid);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","Giao hàng thành công","")
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("NOT FOUND","Đã có người nhận ffown này","")
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("NOT FOUND","Khong tim thay","")
        );
    }

}

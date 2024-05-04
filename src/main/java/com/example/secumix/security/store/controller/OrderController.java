package com.example.secumix.security.store.controller;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.entities.CartItem;
import com.example.secumix.security.store.model.entities.OrderDetail;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.request.OrderDetailRequest;
import com.example.secumix.security.store.model.services.ICartItemService;
import com.example.secumix.security.store.model.services.IOrderService;
import com.example.secumix.security.store.repository.OrderDetailRepo;
import com.example.secumix.security.store.repository.ProductRepo;
import com.example.secumix.security.user.Permission;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;



@RestController
@RequestMapping(value = "/customer/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @GetMapping(value = "/getall")
    ResponseEntity<ResponseObject> getAllByUSer(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Lay ra thanh cong",orderService.GetAllByUser())
        );
    }
    @GetMapping(value = "/detail/{orderdetailid}")
    ResponseEntity<ResponseObject> getInfoOrder(@PathVariable int orderdetailid){
        if(IsPermisson(orderdetailid)){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Lay ra thanh cong",orderService.GetInfoOrder(orderdetailid))
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("OK","Không có quyền truy cập","")
        );
    }

    private boolean IsPermisson(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).get();
        OrderDetail orderDetail= orderDetailRepo.findById(orderdetailid).get();
        if (orderDetail.getCart().getUser().getEmail().equals(email) || user.getRole().getPermissions().contains(Permission.SHIPPER_READ)){
            return true;
        }
        return false;
    }

    @PostMapping(value = "/insert")
    ResponseEntity<ResponseObject> InsertDR(@RequestParam int quantity,
                                            @RequestParam int productid){
        Optional<Product> product=productRepo.findById(productid);
        if(quantity> product.get().getQuantity()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK","Không đủ hàng trong kho","")
            );
        }
        if (product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK","San pham không tồn tại","")
            );
        }
        OrderDetailRequest orderDetailRequest= new OrderDetailRequest(quantity, productid);
        orderService.Insert(orderDetailRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Đặt hàng thành công","")
        );
    }
    @PostMapping(value = "/insert/{cartitemid}")
    ResponseEntity<ResponseObject> InsertIDR(@PathVariable int cartitemid){
        Optional<CartItem> cartItem = cartItemService.findByIdandUser(cartitemid);
        if (cartItem.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK"," không tồn tại trong giỏ hàng","")
            );
        }
        OrderDetailRequest orderDetailRequest= new OrderDetailRequest(cartItem.get().getQuantity(),cartItem.get().getProduct().getProductId());
        orderService.InsertIDR(orderDetailRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Đặt hàng thành công","")
        );
    }

    @GetMapping(value = "/delete/{orderdetailid}")
    ResponseEntity<ResponseObject> ChangeStatus(@PathVariable int orderdetailid){
        orderService.ChangeStatus3(orderdetailid);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Hoàn đơn thành công","")
        );
    }

}

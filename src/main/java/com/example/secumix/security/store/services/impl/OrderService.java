package com.example.secumix.security.store.services.impl;


import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.notify.NotifyRepository;
import com.example.secumix.security.store.model.entities.*;
import com.example.secumix.security.store.model.request.OrderDetailRequest;
import com.example.secumix.security.store.model.response.OrderDetailResponse;
import com.example.secumix.security.store.services.ICartItemService;
import com.example.secumix.security.store.services.IOrderService;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import com.example.secumix.security.userprofile.ProfileDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
     private OrderDetailRepo orderDetailRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private StoreRepo storeRepo;
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private NotifyRepository notifyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private PayRepo payRepo;
    @Autowired
    private ProfileDetailRepository detailRepository;

    @Override
    public List<OrderDetailResponse> GetAll() {
        return orderDetailRepo.findAll().stream()
                .filter(orderDetail -> orderDetail.getOrderStatus().getOrderStatusId()==1)
                .map(
                orderDetail -> {
                    Store store= storeRepo.findStoreByName(orderDetail.getStoreName()).get();
                    Product product= productRepo.findByName(store.getStoreId(),orderDetail.getProductName()).get();
                    OrderDetailResponse  orderDetailResponse= new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(orderDetail.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(orderDetail.getCart().getUser().getEmail()).get().getAddress());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    return orderDetailResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailResponse> GetAllStore(String storeName) {

        return orderDetailRepo.getAllByStore( storeName).stream().map(
                orderDetail -> {
                    Store store= storeRepo.findStoreByName(orderDetail.getStoreName()).get();
                    Product product= productRepo.findByName(store.getStoreId(),orderDetail.getProductName()).get();
                    OrderDetailResponse  orderDetailResponse= new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(orderDetail.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(orderDetail.getCart().getUser().getEmail()).get().getAddress());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    return orderDetailResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailResponse> GetAllByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return orderDetailRepo.getAllByUser(email).stream().map(
                orderDetail -> {
                    Store store= storeRepo.findStoreByName(orderDetail.getStoreName()).get();
                    Product product= productRepo.findByName(store.getStoreId(),orderDetail.getProductName()).get();
                    OrderDetailResponse  orderDetailResponse= new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(orderDetail.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(orderDetail.getCart().getUser().getEmail()).get().getAddress());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    return orderDetailResponse;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDetail> findByIDandUser(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return orderDetailRepo.findByIDandUser(orderdetailid,email);
    }

    @Override
    public void Insert(OrderDetailRequest orderDetailRequest) {
        Product product= productRepo.findById(orderDetailRequest.getProductid()).get();
        Store store= product.getStore();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cart cart= cartRepo.findByEmail(email);
        OrderStatus orderStatus= orderStatusRepo.findById(1).get();
        OrderDetail orderDetail= OrderDetail.builder()
                .orderStatus(orderStatus)
                .cart(cart)
                .quantity(orderDetailRequest.getQuantity())
                .priceTotal(product.getPrice()*orderDetailRequest.getQuantity())
                .productName(product.getProductName())
                .createdAt(UserUtils.getCurrentDay())
                .updatedAt(UserUtils.getCurrentDay())
                .storeName(store.getStoreName())
                .build();
        orderDetailRepo.save(orderDetail);
        product.setQuantity(product.getQuantity()- orderDetailRequest.getQuantity());
        productRepo.save(product);
        Notify notifyuser= Notify.builder()
                .user(cart.getUser())
                .description("Đặt hàng thanh công. Vui lòng theo dõi thông tin đơn hàng.")
                .build();
        notifyRepository.save(notifyuser);
        User manager = userRepository.findByEmail(store.getEmailmanager()).get();
        Notify notifymanager= Notify.builder()
                .user(manager)
                .description("Đơn hàng mới từ khách hàng"+cart.getUser().getEmail())
                .build();
        notifyRepository.save(notifymanager);
    }

    @Override
    public void Save(OrderDetail orderDetail) {
        orderDetailRepo.save(orderDetail);
    }

    @Override
    public void ChangeStatus1(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User shipper = userRepository.findByEmail(email).get();
        OrderStatus orderStatus= orderStatusRepo.findById(2).get();
        OrderDetail orderDetail= orderDetailRepo.findById(orderdetailid).get();
        orderDetail.setOrderStatus(orderStatus);
        orderDetail.setUpdatedAt(UserUtils.getCurrentDay());
        orderDetail.setShipperid(shipper.getId());
        orderDetailRepo.save(orderDetail);
        Notify notify= Notify.builder()
                .user(orderDetail.getCart().getUser())
                .description("Đơn hàng "+ orderDetail.getProductName()+ " của bạn đã được vận chuyển")
                .build();
        notifyRepository.save(notify);
    }
    /*
     * Duy Thuong
     * Chuyển trạng thái giao hàng thành công
     * PreAuthoz = SHIPPER
     */
    @Override
    public void ChangeStatus2(int orderdetailid) {
        OrderStatus orderStatus= orderStatusRepo.findById(3).get();
        OrderDetail orderDetail= orderDetailRepo.findById(orderdetailid).get();
        orderDetail.setOrderStatus(orderStatus);
        orderDetail.setUpdatedAt(UserUtils.getCurrentDay());
        orderDetailRepo.save(orderDetail);
        Pay pay= Pay.builder()
                .status(0)
                .originalPrice(orderDetail.getPriceTotal())
                .orderDetail(orderDetail)
                .createdAt(UserUtils.getCurrentDay())
                .userId(orderDetail.getCart().getUser().getId())
                .paymentMethod("PTTT")
                .build();
        payRepo.save(pay);
        Notify notify= Notify.builder()
                .user(orderDetail.getCart().getUser())
                .description("Đơn hàng "+ orderDetail.getProductName()+ " của bạn đã hoàn thành")
                .build();
        notifyRepository.save(notify);
    }
    /*
     * Duy Thuong
     * Chuyển trạng thái sang huy đơn
     * PreAuthoz = USER
     */
    @Override
    public void ChangeStatus3(int orderdetailid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        OrderStatus orderStatus= orderStatusRepo.findById(4).get();
        OrderDetail orderDetail= orderDetailRepo.findByIDandUser(orderdetailid,email).get();
        orderDetail.setOrderStatus(orderStatus);
        orderDetail.setUpdatedAt(UserUtils.getCurrentDay());
        orderDetailRepo.save(orderDetail);
        Store store= storeRepo.findStoreByName(orderDetail.getStoreName()).get();
        Product product= productRepo.findByName(store.getStoreId(),orderDetail.getProductName()).get();
        product.setQuantity(product.getQuantity()+orderDetail.getQuantity());
        productRepo.save(product);
        var noti= Notify.builder()
                .description("Cập nhật đơn hàng "+ orderDetail.getProductName())
                .user(orderDetail.getCart().getUser())
                .build();
        notifyRepository.save(noti);
    }
    /*
     * Duy Thuong
     * Đặt hàng gián tiếp thông qua cartitem
     * PreAuthoz = USER
     */
    @Override
    public void InsertIDR(OrderDetailRequest orderDetailRequest) {
        Product product= productRepo.findById(orderDetailRequest.getProductid()).get();
        Store store= product.getStore();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Cart cart= cartRepo.findByEmail(email);
        OrderStatus orderStatus= orderStatusRepo.findById(1).get();
        OrderDetail orderDetail= OrderDetail.builder()
                .orderStatus(orderStatus)
                .cart(cart)
                .quantity(orderDetailRequest.getQuantity())
                .priceTotal(product.getPrice()*orderDetailRequest.getQuantity())
                .productName(product.getProductName())
                .createdAt(UserUtils.getCurrentDay())
                .updatedAt(UserUtils.getCurrentDay())
                .storeName(store.getStoreName())
                .build();
        orderDetailRepo.save(orderDetail);
        CartItem cartItem= cartItemService.findByIdandUser(orderDetailRequest.getProductid()).get();
        cartItemRepo.delete(cartItem);
        Notify notify= Notify.builder()
                .user(cart.getUser())
                .description("Đặt hàng thanh công. Vui lòng theo dõi thông tin đơn hàng.")
                .build();
        notifyRepository.save(notify);
    }

    @Override
    public Optional<OrderDetailResponse> GetInfoOrder(int orderdetailid) {
        return orderDetailRepo.findById(orderdetailid).map(
                orderDetail -> {
                    Store store= storeRepo.findStoreByName(orderDetail.getStoreName()).get();
                    Product product= productRepo.findByName(store.getStoreId(),orderDetail.getProductName()).get();
                    OrderDetailResponse  orderDetailResponse= new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(orderDetail.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(orderDetail.getCart().getUser().getEmail()).get().getAddress());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    return orderDetailResponse;
                }
        );
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailByShipperId(int id) {
        List<OrderDetailResponse> orderDetailResponses= orderDetailRepo.getOrderDetailByShipperId(id).stream().map(
                orderDetail -> {
                    Store store= storeRepo.findStoreByName(orderDetail.getStoreName()).get();
                    Product product= productRepo.findByName(store.getStoreId(),orderDetail.getProductName()).get();
                    OrderDetailResponse  orderDetailResponse= new OrderDetailResponse();
                    orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
                    orderDetailResponse.setProductName(orderDetail.getProductName());
                    orderDetailResponse.setOrderStatusName(orderDetail.getOrderStatus().getOrderStatusName());
                    orderDetailResponse.setQuantity(orderDetail.getQuantity());
                    orderDetailResponse.setProductImg(product.getAvatarProduct());
                    orderDetailResponse.setPriceTotal(orderDetail.getPriceTotal());
                    orderDetailResponse.setStoreName(store.getStoreName());
                    orderDetailResponse.setAddress(detailRepository.findProfileDetailBy(orderDetail.getCart().getUser().getEmail()).get().getAddress());
                    return orderDetailResponse;
                }
        ).collect(Collectors.toList());
        return orderDetailResponses;
    }



}

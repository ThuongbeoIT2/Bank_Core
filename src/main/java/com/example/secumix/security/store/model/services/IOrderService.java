package com.example.secumix.security.store.model.services;



import com.example.secumix.security.store.model.entities.OrderDetail;
import com.example.secumix.security.store.model.request.OrderDetailRequest;
import com.example.secumix.security.store.model.response.OrderDetailResponse;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderDetailResponse> GetAll();
    List<OrderDetailResponse> GetAllStore(String storeName);
    List<OrderDetailResponse> GetAllByUser();
    Optional<OrderDetail> findByIDandUser(int orderdetailid);
    void Insert(OrderDetailRequest orderDatailRequest);
    void Save(OrderDetail orderDatail);
    void ChangeStatus1(int orderdetailid);
    void ChangeStatus2(int orderdetailid);
    void ChangeStatus3(int orderdetailid);
    void InsertIDR(OrderDetailRequest orderDetailRequest);

    Optional<OrderDetailResponse> GetInfoOrder(int orderdetailid);
}

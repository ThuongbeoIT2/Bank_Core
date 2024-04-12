package com.example.secumix.security.store.model.entities;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity(name = "orderdetail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "orderdetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdetailid")
    private int orderDetailId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "quantity")
    @Min(value = 0)
    @Max(value = 20, message = "too many products")
    private int quantity;

    @Column(name = "productname")
    private String productName;
    @Column(name = "storename")
    private String storeName;
    @Column(name = "procetotal")
    private int priceTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartid", foreignKey = @ForeignKey(name = "fk_orderdetail_cart"))
    private Cart cart;
    private Integer shipperid=null;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderstatusid", foreignKey = @ForeignKey(name = "fk_orderdetail_orderstatus"))
    private OrderStatus orderStatus;


}
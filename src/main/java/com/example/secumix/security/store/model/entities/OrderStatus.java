package com.example.secumix.security.store.model.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "orderstatus")
@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "orderstatus")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderstatusid")
    private int orderStatusId;

    @Column(name = "orderstatusname")
    private String orderStatusName;

    @OneToMany(mappedBy = "orderStatus", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetail> orderDetailList;

}
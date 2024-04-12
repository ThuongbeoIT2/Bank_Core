package com.example.secumix.security.store.model.entities;


import com.example.secumix.security.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "store")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int storeId;

    @Column(name = "storename")
    private String storeName;

    @Column(name = "address")
    private String address;

    @Column(name = "phonenumber")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Column(name = "rate")
    private int rate;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "storetypeid", foreignKey = @ForeignKey(name = "fk_store_storetype"))
    private StoreType storeType;

    @OneToMany
    @JsonBackReference
    private List<Product> productList;
    @Column
    private String emailmanager;
    @ManyToMany
    @JoinTable(name = "users_stores",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id"))
    private Set<User> users = new HashSet<>();

}
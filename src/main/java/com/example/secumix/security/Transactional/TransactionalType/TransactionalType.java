package com.example.secumix.security.Transactional.TransactionalType;

import com.example.secumix.security.Transactional.Transactional.Transactional;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Entity(name = "transactionaltype")
@Table(name = "transactionaltype")
@Data

public class TransactionalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int TypeID;
    @Column(unique = true,nullable = false)
    private String TypeName;
    @Column(nullable = false)
    private String Description;
    @OneToMany(mappedBy = "transactionalType")
    @JsonManagedReference
    private List<Transactional> transactionals;
}

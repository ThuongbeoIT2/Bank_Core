package com.example.secumix.security.Transactional.TransactionalType;

import com.example.secumix.security.Transactional.Transactional.Transactional;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "transactionaltype")
@Table(name = "transactionaltype")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int TypeID;
    @Column(unique = true)
    private String TypeName;
    private String Description;
    @OneToMany(mappedBy = "transactionalType")
    @JsonManagedReference
    private List<Transactional> transactionals;
}

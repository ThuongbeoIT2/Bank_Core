package com.example.secumix.security.bankentity.BankIntroduce;

import com.example.secumix.security.bankentity.Bank.Bank;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "bankintroduce")
@Table(name = "bankintroduce")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankIntroduce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int BankIntroduceID;
    @Column(nullable = false,unique = true)
    private String TitleIntroduce;
    @Column(nullable = false)
    private String AvatarIntroduce;
    @Column(nullable = false)
    private String Description;
    @OneToOne
    @JoinColumn(name = "BankID",foreignKey = @ForeignKey(name = "fk_Bank_Introduce"))
    private Bank bank;
}

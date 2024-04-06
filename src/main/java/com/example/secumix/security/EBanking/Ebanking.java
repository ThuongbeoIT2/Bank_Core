package com.example.secumix.security.EBanking;

import com.example.secumix.security.bankentity.Bank.Bank;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity(name = "ebanking")
@Table
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ebanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int EBankID;
    @Column(nullable = false, unique = true)
    @Pattern(regexp="\\d{16}", message="Code must be a 16-digit number")
    private String Code;
    private int userID;
    @Column(nullable = false)
    private String PhoneID;
    @Column(nullable = false)
    private String Password;
    @Column(nullable = false)
    private String OTP;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "BankID",foreignKey = @ForeignKey(name = "fk_Bank_Ebanking"))
    private Bank bank;

}

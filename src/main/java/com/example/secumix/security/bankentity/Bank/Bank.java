package com.example.secumix.security.bankentity.Bank;

import com.example.secumix.security.EBanking.Ebanking;
import com.example.secumix.security.bankentity.BankBranch.BankBranch;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity(name = "bank")
@Table(name = "bank")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bank implements BankBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int BankID;

    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Represent field is only allowed to contain letters and must have exactly 2 characters.")
    @Column(nullable = false, unique = true)
    private String Represent;

    @NotBlank(message = "BankName is not blank")
    @Size(max = 30, message = "The BankName field must not exceed 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "The BankName field must not contain special characters.")
    private String BankName;
    @NotNull
    private String Logo;
    private Date CreatedAt;
    private Date UpdatedAt;
    private Date DeletedAt;

    @OneToMany(mappedBy = "bank")
    @JsonManagedReference
    private List<BankBranch> bankBranches;
    @OneToMany(mappedBy = "bank")
    @JsonManagedReference
    private List<Ebanking> ebankings;
    @Override
    public String getName(Bank bank) {
        return bank.BankName;
    }
}

package com.example.secumix.security.bankentity.BankBranch;

import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.bankentity.Bank.Bank;
import com.example.secumix.security.card.BankCard.BankCard;
import com.example.secumix.security.card.CreditCard.CreditCard;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "bankbranch")
@Table(name = "bankbranch")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int BankBranchID;
    private String BankCode;
    @Column(nullable = false,unique = true)
    private String BankBranchName;
    private String Province;
    private String District;
    private String Ward;
    private Date CreatedAt;
    private Date UpdatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "BankID",foreignKey = @ForeignKey(name = "fk_bank_Branch"))
    private Bank bank;
    @OneToMany(mappedBy = "bankBranch")
    @JsonManagedReference
    private List<BankCard> bankCards;
    @OneToMany(mappedBy = "bankBranch")
    @JsonManagedReference
    private List<CreditCard> creditCards;
    public String AddressBankBranch(BankBranch bankBranch){
        String Address = bankBranch.getWard()+", " +bankBranch.getDistrict()+", "+bankBranch.getProvince();
        return Address;
    }
    public void setBankCode(BankBranch bankBranch){
        String bankcode = bankBranch.bank.getRepresent().concat(UserUtils.generateTempPwd(6));
        bankBranch.BankCode = bankcode;
    }
}

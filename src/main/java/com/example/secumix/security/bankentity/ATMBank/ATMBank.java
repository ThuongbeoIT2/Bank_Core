package com.example.secumix.security.bankentity.ATMBank;


import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.bankentity.Bank.Bank;
import com.example.secumix.security.bankentity.BankBranch.BankBranch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;



import javax.persistence.*;
import java.util.Date;

@Entity(name = "atmbank")
@Table(name = "atmbank")
@Data
public class ATMBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ATMID;
    private String ATMCode;
    private String Province;
    private String District;
    private String Ward;
    private Date CreatedAt;
    private Date UpdatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "bankID",foreignKey = @ForeignKey(name = "fk_bank_atm"))
    private Bank bank;
    public String AddressBankBranch(ATMBank atmBank){
        String Address = atmBank.getWard()+", " +atmBank.getDistrict()+", "+atmBank.getProvince();
        return Address;
    }
    public void setBankCode(ATMBank atmBank){
        String atmcode = atmBank.bank.getRepresent().concat(UserUtils.generateTempPwd(6));
        atmBank.ATMCode = atmcode;
    }
}

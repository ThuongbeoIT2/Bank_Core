package com.example.secumix.security.bankentity.ATMBank;


import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.bankentity.Bank.Bank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


import javax.persistence.*;
import java.util.Date;

@Entity(name = "atmbank")
@Table(name = "atmbank")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ATMBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("ATMID")
    private int ATMID;
    @SerializedName("ATMCode")
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
    public String AddressBankBranch(){
        String Address = this.getWard()+", " +this.getDistrict()+", "+this.getProvince();
        return Address;
    }

    public void setATMCode(){
        String atmcode = this.bank.getRepresent().concat(UserUtils.generateTempPwd(6));
        this.ATMCode = atmcode;

    }
}

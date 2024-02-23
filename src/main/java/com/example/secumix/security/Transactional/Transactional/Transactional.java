package com.example.secumix.security.Transactional.Transactional;

import com.example.secumix.security.Transactional.TransactionalType.TransactionalType;
import com.example.secumix.security.Utils.UserUtils;
import com.example.secumix.security.bankentity.BankBranch.BankBranch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.DatabindException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity(name = "transactional")
@Table(name = "transactional")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transactional {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long TransactionalID;
    @Column(unique = true)
    private String TransactionalCode;
    @Column(nullable = false, unique = true)
    @Pattern(regexp="\\d{16}", message="Code must be a 16-digit number")
    private String CodeA;
    @Column(nullable = false, unique = true)
    @Pattern(regexp="\\d{16}", message="Code must be a 16-digit number")
    private String CodeB;
    @NotNull(message="Amount must not be null")
    @Positive(message="Amount must be a positive number")
    private Long Amount;
    private String BankA;
    private String BankB;
    private String Description;
    private Date CreatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "TranTypeID",foreignKey = @ForeignKey(name = "fk_tupe_Transactional"))
    private TransactionalType transactionalType;
    public void setTransactionalCode(Transactional transactional){
        String code ="GD-";
        String TransactionalCode = code.concat(UserUtils.generateTempPwd(8));
        transactional.TransactionalCode = TransactionalCode;
    }
}

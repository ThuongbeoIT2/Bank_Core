package com.example.secumix.security.card.CreditCard;

import com.example.secumix.security.bankentity.BankBranch.BankBranch;
import com.example.secumix.security.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity(name = "creditcard")
@Table(name = "creditcard")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CreditCardID;

    @Column(nullable = false, unique = true)
    @Pattern(regexp="\\d{16}", message="Code must be a 16-digit number")
    private String Code;

    @Column(nullable = false)
    private String CustomerName;

    @Column(nullable = false)
    @Pattern(regexp="\\d{6}", message="PinCode must be a 6-digit number")
    private String PinCode;

    @NotNull(message="Surplus must not be null")
    @Positive(message="Surplus must be a positive number")
    private Long Surplus;
    @NotNull(message="Debt must not be null")
    @Positive(message="Debt must be a positive number")
    private Long Debt;
    private Boolean CardStatus;

    private Date CreatedAt;

    private int userID;
    private Date UpdatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "BankBranchID",foreignKey = @ForeignKey(name = "fk_Branch_CreditCard"))
    private BankBranch bankBranch;


}

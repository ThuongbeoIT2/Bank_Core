package com.example.secumix.security.card.BankCard;

import com.example.secumix.security.bankentity.BankBranch.BankBranch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity(name = "bankcard")
@Table(name = "bankcard")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int BankCardID;

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

    private Boolean CardStatus;
    private int userID;
    private Date CreatedAt;

    private Date UpdatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "BankBranchID",foreignKey = @ForeignKey(name = "fk_Branch_Card"))
    private BankBranch bankBranch;
}

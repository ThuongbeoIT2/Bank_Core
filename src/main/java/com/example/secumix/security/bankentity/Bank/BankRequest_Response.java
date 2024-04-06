package com.example.secumix.security.bankentity.Bank;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Data
public class BankRequest_Response {
    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Represent field is only allowed to contain letters and must have exactly 2 characters.")
    @Column(nullable = false, unique = true)
    private String Represent;

    @NotBlank(message = "BankName is not blank")
    @Size(max = 30, message = "The BankName field must not exceed 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "The BankName field must not contain special characters.")
    private String BankName;

    private String Logo;
}

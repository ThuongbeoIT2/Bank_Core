package com.example.secumix.security.Transactional.TransactionalType;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class TransactionalTypeRequest {
    @Pattern(regexp = "[a-zA-Z\\s]+", message = "Transaction type name should only contain letters and spaces")
    private String TypeName;

    @Pattern(regexp = "[a-zA-Z\\s]+", message = "Description should only contain letters and spaces")
    private String Description;
}

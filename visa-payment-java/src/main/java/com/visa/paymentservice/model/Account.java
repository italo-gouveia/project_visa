package com.visa.paymentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;
    
    @NotBlank(message = "Document number is required")
    @Pattern(regexp = "^\\d{11}$", message = "Document number must be exactly 11 digits")
    @Column(name = "document_number", unique = true, nullable = false)
    private String documentNumber;
    
    public Account() {}
    
    public Account(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    
    public Long getAccountId() {
        return accountId;
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}

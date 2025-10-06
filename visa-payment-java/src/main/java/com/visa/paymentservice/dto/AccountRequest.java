package com.visa.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AccountRequest {
    
    @NotBlank(message = "Document number is required")
    @Pattern(regexp = "^\\d{11}$", message = "Document number must be exactly 11 digits")
    private String documentNumber;
    
    public AccountRequest() {}
    
    public AccountRequest(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}

package com.visa.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransactionRequest {
    
    @NotNull(message = "Account ID is required")
    private Long accountId;
    
    @NotNull(message = "Operation type ID is required")
    private Long operationTypeId;
    
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    
    public TransactionRequest() {}
    
    public TransactionRequest(Long accountId, Long operationTypeId, BigDecimal amount) {
        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.amount = amount;
    }
    
    public Long getAccountId() {
        return accountId;
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public Long getOperationTypeId() {
        return operationTypeId;
    }
    
    public void setOperationTypeId(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

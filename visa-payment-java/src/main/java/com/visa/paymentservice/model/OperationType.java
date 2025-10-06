package com.visa.paymentservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "operation_types")
public class OperationType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_type_id")
    private Long operationTypeId;
    
    @Column(name = "description", nullable = false)
    private String description;
    
    public OperationType() {}
    
    public OperationType(String description) {
        this.description = description;
    }
    
    public Long getOperationTypeId() {
        return operationTypeId;
    }
    
    public void setOperationTypeId(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}

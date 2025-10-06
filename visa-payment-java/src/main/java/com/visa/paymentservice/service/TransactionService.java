package com.visa.paymentservice.service;

import com.visa.paymentservice.dto.TransactionRequest;
import com.visa.paymentservice.model.Account;
import com.visa.paymentservice.model.OperationType;
import com.visa.paymentservice.model.Transaction;
import com.visa.paymentservice.repository.AccountRepository;
import com.visa.paymentservice.repository.OperationTypeRepository;
import com.visa.paymentservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private OperationTypeRepository operationTypeRepository;
    
    public Transaction createTransaction(TransactionRequest request) {
        // Validate account exists
        Optional<Account> accountOpt = accountRepository.findById(request.getAccountId());
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account with ID " + request.getAccountId() + " not found");
        }
        
        // Validate operation type exists
        Optional<OperationType> operationTypeOpt = operationTypeRepository.findById(request.getOperationTypeId());
        if (operationTypeOpt.isEmpty()) {
            throw new IllegalArgumentException("Operation type with ID " + request.getOperationTypeId() + " not found");
        }
        
        Account account = accountOpt.get();
        OperationType operationType = operationTypeOpt.get();
        
        // Apply business rules for amount sign based on operation type
        BigDecimal amount = request.getAmount();
        String operationDescription = operationType.getDescription().toLowerCase();
        
        // Purchase and withdrawal transactions should be negative
        if (operationDescription.contains("purchase") || operationDescription.contains("withdrawal")) {
            amount = amount.negate();
        }
        // Credit voucher transactions should be positive (already positive from validation)
        
        Transaction transaction = new Transaction(account, operationType, amount);
        return transactionRepository.save(transaction);
    }
}

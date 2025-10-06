package com.visa.paymentservice.service;

import com.visa.paymentservice.dto.AccountRequest;
import com.visa.paymentservice.dto.AccountResponse;
import com.visa.paymentservice.model.Account;
import com.visa.paymentservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public AccountResponse createAccount(AccountRequest request) {
        // Check if account with this document number already exists
        if (accountRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new IllegalArgumentException("Account with document number " + request.getDocumentNumber() + " already exists");
        }
        
        Account account = new Account(request.getDocumentNumber());
        Account savedAccount = accountRepository.save(account);
        
        return new AccountResponse(savedAccount.getAccountId(), savedAccount.getDocumentNumber());
    }
    
    @Transactional(readOnly = true)
    public Optional<AccountResponse> getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .map(account -> new AccountResponse(account.getAccountId(), account.getDocumentNumber()));
    }
    
    @Transactional(readOnly = true)
    public Optional<Account> findAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }
}

package com.visa.paymentservice.controller;

import com.visa.paymentservice.dto.AccountRequest;
import com.visa.paymentservice.dto.AccountResponse;
import com.visa.paymentservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Management", description = "APIs for managing customer accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @PostMapping
    @Operation(summary = "Create a new account", description = "Creates a new account with the provided document number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or account already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        try {
            AccountResponse response = accountService.createAccount(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by ID", description = "Retrieves account information by account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        Optional<AccountResponse> account = accountService.getAccount(accountId);
        return account.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

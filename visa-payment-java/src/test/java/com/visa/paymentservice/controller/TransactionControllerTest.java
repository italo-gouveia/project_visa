package com.visa.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visa.paymentservice.dto.TransactionRequest;
import com.visa.paymentservice.model.Account;
import com.visa.paymentservice.model.OperationType;
import com.visa.paymentservice.repository.AccountRepository;
import com.visa.paymentservice.repository.OperationTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;
    private OperationType normalPurchase;
    private OperationType creditVoucher;

    @BeforeEach
    void setUp() {
        // Create test account
        testAccount = new Account("12345678900");
        testAccount = accountRepository.save(testAccount);

        // Create operation types
        normalPurchase = new OperationType("Normal Purchase");
        normalPurchase = operationTypeRepository.save(normalPurchase);

        creditVoucher = new OperationType("Credit Voucher");
        creditVoucher = operationTypeRepository.save(creditVoucher);
    }

    @Test
    void createTransaction_ValidRequest_ReturnsCreated() throws Exception {
        TransactionRequest request = new TransactionRequest(
                testAccount.getAccountId(),
                normalPurchase.getOperationTypeId(),
                new BigDecimal("50.00")
        );

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transaction_id").exists())
                .andExpect(jsonPath("$.account.account_id").value(testAccount.getAccountId()))
                .andExpect(jsonPath("$.operation_type.operation_type_id").value(normalPurchase.getOperationTypeId()))
                .andExpect(jsonPath("$.amount").value(-50.00)) // Should be negative for purchase
                .andExpect(jsonPath("$.event_date").exists());
    }

    @Test
    void createTransaction_CreditVoucher_ReturnsPositiveAmount() throws Exception {
        TransactionRequest request = new TransactionRequest(
                testAccount.getAccountId(),
                creditVoucher.getOperationTypeId(),
                new BigDecimal("25.00")
        );

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(25.00)); // Should be positive for credit voucher
    }

    @Test
    void createTransaction_InvalidAccountId_ReturnsBadRequest() throws Exception {
        TransactionRequest request = new TransactionRequest(
                999L, // Non-existent account
                normalPurchase.getOperationTypeId(),
                new BigDecimal("50.00")
        );

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTransaction_InvalidOperationTypeId_ReturnsBadRequest() throws Exception {
        TransactionRequest request = new TransactionRequest(
                testAccount.getAccountId(),
                999L, // Non-existent operation type
                new BigDecimal("50.00")
        );

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTransaction_InvalidAmount_ReturnsBadRequest() throws Exception {
        TransactionRequest request = new TransactionRequest(
                testAccount.getAccountId(),
                normalPurchase.getOperationTypeId(),
                new BigDecimal("-10.00") // Negative amount should fail validation
        );

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

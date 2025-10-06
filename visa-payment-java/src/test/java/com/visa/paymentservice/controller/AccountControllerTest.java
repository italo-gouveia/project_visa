package com.visa.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visa.paymentservice.dto.AccountRequest;
import com.visa.paymentservice.model.Account;
import com.visa.paymentservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_ValidRequest_ReturnsCreated() throws Exception {
        AccountRequest request = new AccountRequest("12345678900");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account_id").exists())
                .andExpect(jsonPath("$.document_number").value("12345678900"));
    }

    @Test
    void createAccount_InvalidDocumentNumber_ReturnsBadRequest() throws Exception {
        AccountRequest request = new AccountRequest("123"); // Invalid: not 11 digits

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_DuplicateDocumentNumber_ReturnsBadRequest() throws Exception {
        // Create first account
        Account existingAccount = new Account("12345678900");
        accountRepository.save(existingAccount);

        // Try to create another account with same document number
        AccountRequest request = new AccountRequest("12345678900");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAccount_ExistingAccount_ReturnsOk() throws Exception {
        Account account = new Account("12345678900");
        Account savedAccount = accountRepository.save(account);

        mockMvc.perform(get("/accounts/{accountId}", savedAccount.getAccountId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_id").value(savedAccount.getAccountId()))
                .andExpect(jsonPath("$.document_number").value("12345678900"));
    }

    @Test
    void getAccount_NonExistentAccount_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/accounts/{accountId}", 999L))
                .andExpect(status().isNotFound());
    }
}

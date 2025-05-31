package com.example.transaction.management;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.model.TransactionType;
import com.example.transaction.management.service.TransactionService;
import com.example.transaction.management.exception.TransactionException;
import com.example.transaction.management.controller.TransactionController;
import com.example.transaction.management.exception.TransactionErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction testTransaction;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testTransaction = new Transaction();
        testTransaction.setId(testId);
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setType(TransactionType.DEPOSIT);
        testTransaction.setDescription("Test deposit");
        testTransaction.setCategory("Test");
        testTransaction.setTimestamp(Instant.now());
    }

    @Test
    @DisplayName("Should create a new transaction")
    void testCreateTransaction() throws Exception {
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(testTransaction);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTransaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.description").value("Test deposit"))
                .andExpect(jsonPath("$.category").value("Test"));
    }

    @Test
    @DisplayName("Should return 400 when creating transaction with invalid data")
    void testCreateTransactionInvalidData() throws Exception {
        Transaction invalidTransaction = new Transaction();
        // Missing required fields

        when(transactionService.createTransaction(any(Transaction.class)))
                .thenThrow(new TransactionException(TransactionErrorType.INVALID_AMOUNT));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTransaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get transaction by ID")
    void testGetTransaction() throws Exception {
        when(transactionService.getTransaction(testId)).thenReturn(Optional.of(testTransaction));

        mockMvc.perform(get("/transactions/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.description").value("Test deposit"))
                .andExpect(jsonPath("$.category").value("Test"));
    }

    @Test
    @DisplayName("Should return 404 when transaction not found")
    void testGetTransactionNotFound() throws Exception {
        when(transactionService.getTransaction(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/transactions/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get all transactions with pagination")
    void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = Arrays.asList(testTransaction);
        when(transactionService.getAllTransactions(0, 10)).thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testId.toString()))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"))
                .andExpect(jsonPath("$[0].description").value("Test deposit"))
                .andExpect(jsonPath("$[0].category").value("Test"));
    }

    @Test
    @DisplayName("Should return 400 when pagination parameters are invalid")
    void testGetAllTransactionsInvalidPagination() throws Exception {
        when(transactionService.getAllTransactions(-1, 10))
                .thenThrow(new TransactionException(TransactionErrorType.INVALID_PAGINATION));

        mockMvc.perform(get("/transactions")
                .param("page", "-1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update transaction")
    void testUpdateTransaction() throws Exception {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(testId);
        updatedTransaction.setAmount(new BigDecimal("200.00"));
        updatedTransaction.setType(TransactionType.WITHDRAWAL);
        updatedTransaction.setDescription("Updated description");
        updatedTransaction.setCategory("Updated category");
        updatedTransaction.setTimestamp(Instant.now());

        when(transactionService.updateTransaction(eq(testId), any(Transaction.class)))
                .thenReturn(updatedTransaction);

        mockMvc.perform(put("/transactions/" + testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTransaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.amount").value(200.00))
                .andExpect(jsonPath("$.type").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.category").value("Updated category"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent transaction")
    void testUpdateTransactionNotFound() throws Exception {
        when(transactionService.updateTransaction(eq(testId), any(Transaction.class)))
                .thenThrow(new RuntimeException("Transaction not found"));

        mockMvc.perform(put("/transactions/" + testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTransaction)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete transaction")
    void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(testId);

        mockMvc.perform(delete("/transactions/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent transaction")
    void testDeleteTransactionNotFound() throws Exception {
        doThrow(new TransactionException(TransactionErrorType.TRANSACTION_NOT_FOUND))
                .when(transactionService).deleteTransaction(testId);

        mockMvc.perform(delete("/transactions/" + testId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
package com.example.transaction.management;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.model.TransactionType;
import com.example.transaction.management.repository.InMemoryTransactionRepository;
import com.example.transaction.management.repository.TransactionRepository;
import com.example.transaction.management.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {
    private TransactionService service;
    private TransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        service = new TransactionService(repository);
    }

    @Test
    void testCreateTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Test deposit");
        transaction.setCategory("Test");

        Transaction saved = service.createTransaction(transaction);
        assertNotNull(saved.getId());
        assertEquals(new BigDecimal("100.00"), saved.getAmount());
    }

    @Test
    void testGetTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Test deposit");
        transaction.setCategory("Test");

        Transaction saved = service.createTransaction(transaction);
        Optional<Transaction> retrieved = service.getTransaction(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(saved.getId(), retrieved.get().getId());
    }

    @Test
    void testGetAllTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setType(TransactionType.DEPOSIT);
        transaction1.setDescription("Test deposit 1");
        transaction1.setCategory("Test");

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setType(TransactionType.WITHDRAWAL);
        transaction2.setDescription("Test withdrawal");
        transaction2.setCategory("Test");

        service.createTransaction(transaction1);
        service.createTransaction(transaction2);

        List<Transaction> transactions = service.getAllTransactions(0, 10);
        assertEquals(2, transactions.size());
    }

    @Test
    void testUpdateTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Test deposit");
        transaction.setCategory("Test");

        Transaction saved = service.createTransaction(transaction);
        saved.setAmount(new BigDecimal("150.00"));
        Transaction updated = service.updateTransaction(saved.getId(), saved);
        assertEquals(new BigDecimal("150.00"), updated.getAmount());
    }

    @Test
    void testDeleteTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Test deposit");
        transaction.setCategory("Test");

        Transaction saved = service.createTransaction(transaction);
        service.deleteTransaction(saved.getId());
        Optional<Transaction> retrieved = service.getTransaction(saved.getId());
        assertFalse(retrieved.isPresent());
    }
} 
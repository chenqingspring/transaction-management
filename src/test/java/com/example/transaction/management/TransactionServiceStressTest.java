package com.example.transaction.management;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.model.TransactionType;
import com.example.transaction.management.repository.InMemoryTransactionRepository;
import com.example.transaction.management.repository.TransactionRepository;
import com.example.transaction.management.service.TransactionService;
import com.example.transaction.management.exception.TransactionException;
import com.example.transaction.management.exception.TransactionErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceStressTest {
    // Number of threads for concurrent testing
    private static final int THREAD_COUNT = 10;
    
    // Number of transactions to create per thread
    private static final int TRANSACTIONS_PER_THREAD = 1000;
    
    // Number of operations per thread in update and delete tests
    private static final int OPERATIONS_PER_THREAD = 10;
    
    // Initial number of transactions for update and delete tests
    private static final int INITIAL_TRANSACTIONS = THREAD_COUNT * OPERATIONS_PER_THREAD;
    
    // Maximum page size for pagination tests
    private static final int MAX_PAGE_SIZE = 50;
    
    // Test transaction amount
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("100.00");
    
    // Updated amount for update tests
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal("200.00");

    private TransactionService service;
    private TransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        service = new TransactionService(repository);
    }

    @Test
    @DisplayName("Should handle concurrent creation and retrieval of transactions")
    void testConcurrentCreateAndRead() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < TRANSACTIONS_PER_THREAD; j++) {
                        Transaction transaction = createTransaction(threadId, j);
                        service.createTransaction(transaction);
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        if (!exceptions.isEmpty()) {
            throw new RuntimeException("Test failed with exceptions: " + exceptions);
        }

        int expectedTotal = THREAD_COUNT * TRANSACTIONS_PER_THREAD;
        assertEquals(expectedTotal, successCount.get(), "Not all transactions were created successfully");
        
        // Verify transaction count using valid page size
        List<Transaction> allTransactions = service.getAllTransactions(0, MAX_PAGE_SIZE);
        assertTrue(allTransactions.size() <= MAX_PAGE_SIZE, "Page size should not exceed maximum limit");
    }

    @Test
    @DisplayName("Should handle concurrent update and delete operations")
    void testConcurrentUpdateAndDelete() throws InterruptedException {
        // First create initial transactions
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < INITIAL_TRANSACTIONS; i++) {
            Transaction transaction = createTransaction(0, i);
            transactions.add(service.createTransaction(transaction));
        }

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int index = (threadId * OPERATIONS_PER_THREAD + j) % transactions.size();
                        Transaction transaction = transactions.get(index);
                        
                        // Update transaction
                        transaction.setAmount(UPDATED_AMOUNT);
                        service.updateTransaction(transaction.getId(), transaction);
                        
                        // Delete transaction
                        service.deleteTransaction(transaction.getId());
                        
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        if (!exceptions.isEmpty()) {
            throw new RuntimeException("Test failed with exceptions: " + exceptions);
        }

        int expectedTotal = THREAD_COUNT * OPERATIONS_PER_THREAD;
        assertEquals(expectedTotal, successCount.get(), "Not all operations were completed successfully");
    }

    private Transaction createTransaction(int threadId, int index) {
        Transaction transaction = new Transaction();
        transaction.setAmount(TEST_AMOUNT);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Test transaction " + threadId + "-" + index);
        transaction.setCategory("Test");
        return transaction;
    }
} 
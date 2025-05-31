package com.example.transaction.management.service;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.model.TransactionType;
import com.example.transaction.management.repository.InMemoryTransactionRepository;
import com.example.transaction.management.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionServiceStressTest {
    private TransactionService service;
    private TransactionRepository repository;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        service = new TransactionService(repository);
        executorService = Executors.newFixedThreadPool(10);
    }

    @Test
    void testConcurrentCreateAndRead() throws InterruptedException {
        int numThreads = 10;
        int numTransactions = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < numTransactions; j++) {
                        Transaction transaction = new Transaction();
                        transaction.setAmount(new BigDecimal("100.00"));
                        transaction.setType(TransactionType.DEPOSIT);
                        transaction.setDescription("Stress test transaction");
                        transaction.setCategory("Stress");
                        service.createTransaction(transaction);
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        assertEquals(numThreads * numTransactions, successCount.get());
    }
} 
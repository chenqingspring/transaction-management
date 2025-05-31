package com.example.transaction.management.service;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final ReentrantLock lock = new ReentrantLock();

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction createTransaction(Transaction transaction) {
        lock.lock();
        try {
            if (transaction.getId() == null) {
                transaction.setId(UUID.randomUUID());
            }
            return repository.save(transaction);
        } finally {
            lock.unlock();
        }
    }

    @Cacheable(value = "transactions", key = "#id")
    public Optional<Transaction> getTransaction(UUID id) {
        return repository.findById(id);
    }

    @Cacheable(value = "transactions", key = "'all'")
    public List<Transaction> getAllTransactions(int page, int size) {
        return repository.findAll(page, size);
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction updateTransaction(UUID id, Transaction transaction) {
        lock.lock();
        try {
            if (repository.findById(id).isPresent()) {
                transaction.setId(id);
                return repository.save(transaction);
            }
            throw new RuntimeException("Transaction not found");
        } finally {
            lock.unlock();
        }
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(UUID id) {
        lock.lock();
        try {
            repository.deleteById(id);
        } finally {
            lock.unlock();
        }
    }
} 
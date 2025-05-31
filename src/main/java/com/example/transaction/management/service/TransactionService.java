package com.example.transaction.management.service;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }
        return repository.save(transaction);
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
        if (repository.findById(id).isPresent()) {
            transaction.setId(id);
            return repository.save(transaction);
        }
        throw new RuntimeException("Transaction not found");
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(UUID id) {
        repository.deleteById(id);
    }
} 
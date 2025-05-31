package com.example.transaction.management.service;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.repository.TransactionRepository;
import com.example.transaction.management.exception.TransactionException;
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
        try {
            if (transaction.getId() == null) {
                transaction.setId(UUID.randomUUID());
            }
            return repository.save(transaction);
        } catch (Exception e) {
            throw new TransactionException("Failed to create transaction", e);
        }
    }

    @Cacheable(value = "transactions", key = "#id")
    public Optional<Transaction> getTransaction(UUID id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new TransactionException("Failed to get transaction", e);
        }
    }

    @Cacheable(value = "transactions", key = "'all'")
    public List<Transaction> getAllTransactions(int page, int size) {
        return repository.findAll(page, size);
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction updateTransaction(UUID id, Transaction transaction) {
        try {
            if (repository.findById(id).isPresent()) {
                transaction.setId(id);
                return repository.save(transaction);
            }
            throw new TransactionException("Transaction not found");
        } catch (Exception e) {
            throw new TransactionException("Failed to update transaction", e);
        }
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(UUID id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new TransactionException("Failed to delete transaction", e);
        }
    }
} 
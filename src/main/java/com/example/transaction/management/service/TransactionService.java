package com.example.transaction.management.service;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.repository.TransactionRepository;
import com.example.transaction.management.exception.TransactionException;
import com.example.transaction.management.exception.TransactionErrorType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction createTransaction(@Valid Transaction transaction) {
        try {
            validateTransaction(transaction);
            
            if (transaction.getId() == null) {
                transaction.setId(UUID.randomUUID());
            }
            if (transaction.getTimestamp() == null) {
                transaction.setTimestamp(java.time.Instant.now());
            }
            
            return repository.save(transaction);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new TransactionException(TransactionErrorType.INVALID_AMOUNT, e);
        }
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() == null) {
            throw new TransactionException(TransactionErrorType.INVALID_AMOUNT);
        }
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException(TransactionErrorType.NEGATIVE_AMOUNT);
        }
        if (transaction.getType() == null) {
            throw new TransactionException(TransactionErrorType.INVALID_TYPE);
        }
        if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
            throw new TransactionException(TransactionErrorType.MISSING_DESCRIPTION);
        }
        if (transaction.getCategory() == null || transaction.getCategory().trim().isEmpty()) {
            throw new TransactionException(TransactionErrorType.MISSING_CATEGORY);
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

    public List<Transaction> getAllTransactions(int page, int size) {
        return repository.findAll(page, size);
    }

    @CacheEvict(value = "transactions", key = "#id")
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

    @CacheEvict(value = "transactions", key = "#id")
    public void deleteTransaction(UUID id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new TransactionException("Failed to delete transaction", e);
        }
    }
} 
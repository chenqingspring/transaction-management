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
import java.util.concurrent.locks.ReentrantLock;

@Service
@Validated
public class TransactionService {
    private static final int MAX_PAGE_SIZE = 50;
    private final TransactionRepository repository;
    private final ReentrantLock lock = new ReentrantLock();

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction createTransaction(@Valid Transaction transaction) {
        validateTransaction(transaction);
        return repository.save(transaction);
    }

    @Cacheable(value = "transactions", key = "#id")
    public Optional<Transaction> getTransaction(UUID id) {
        return repository.findById(id);
    }

    public List<Transaction> getAllTransactions(int page, int size) {
        validatePagination(page, size);
        return repository.findAll(page, size);
    }

    @CacheEvict(value = "transactions", key = "#id")
    public Transaction updateTransaction(UUID id, Transaction transaction) {
        validateTransaction(transaction);
        try {
            lock.lock();
            if (!repository.findById(id).isPresent()) {
                throw new TransactionException(TransactionErrorType.TRANSACTION_NOT_FOUND);
            }
            transaction.setId(id);
            return repository.save(transaction);
        } finally {
            lock.unlock();
        }
    }

    @CacheEvict(value = "transactions", key = "#id")
    public void deleteTransaction(UUID id) {
        try {
            lock.lock();
            if (!repository.findById(id).isPresent()) {
                throw new TransactionException(TransactionErrorType.TRANSACTION_NOT_FOUND);
            }
            repository.deleteById(id);
        } finally {
            lock.unlock();
        }
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() == null) {
            throw new TransactionException(TransactionErrorType.INVALID_AMOUNT);
        }
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionException(TransactionErrorType.NEGATIVE_AMOUNT);
        }
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new TransactionException(TransactionErrorType.ZERO_AMOUNT);
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

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new TransactionException(TransactionErrorType.INVALID_PAGINATION);
        }
        if (size <= 0) {
            throw new TransactionException(TransactionErrorType.INVALID_PAGINATION);
        }
        if (size > MAX_PAGE_SIZE) {
            throw new TransactionException(TransactionErrorType.INVALID_PAGINATION);
        }
    }
}
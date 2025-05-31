package com.example.transaction.management.repository;

import com.example.transaction.management.model.Transaction;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final ConcurrentHashMap<UUID, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    public List<Transaction> findAll(int page, int size) {
        return transactions.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        transactions.remove(id);
    }
}
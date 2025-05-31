package com.example.transaction.management.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class Transaction {
    private UUID id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private Instant timestamp;
    private String category;
} 
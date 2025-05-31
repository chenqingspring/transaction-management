package com.example.transaction.management.exception;

public enum TransactionErrorType {
    INVALID_AMOUNT("Invalid transaction amount"),
    INVALID_TYPE("Invalid transaction type"),
    MISSING_DESCRIPTION("Transaction description is required"),
    MISSING_CATEGORY("Transaction category is required"),
    NEGATIVE_AMOUNT("Transaction amount cannot be negative"),
    ZERO_AMOUNT("Transaction amount cannot be zero"),
    TRANSACTION_NOT_FOUND("Transaction not found"),
    INVALID_PAGINATION("Invalid pagination parameters");

    private final String message;

    TransactionErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
} 
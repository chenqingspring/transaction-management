package com.example.transaction.management.exception;

public class TransactionException extends RuntimeException {
    private final TransactionErrorType errorType;

    public TransactionException(TransactionErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public TransactionException(TransactionErrorType errorType, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
    }

    public TransactionErrorType getErrorType() {
        return errorType;
    }
} 
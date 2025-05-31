package com.example.transaction.management.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Transaction Type Enumeration")
public enum TransactionType {
    @Schema(description = "Deposit")
    DEPOSIT,
    
    @Schema(description = "Withdrawal")
    WITHDRAWAL,
    
    @Schema(description = "Transfer")
    TRANSFER
}
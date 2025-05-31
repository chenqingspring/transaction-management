package com.example.transaction.management.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Transaction Entity")
public class Transaction {
    @Schema(description = "Transaction ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "Transaction Amount", example = "100.50")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @Schema(description = "Transaction Type", example = "DEPOSIT", allowableValues = {"DEPOSIT", "WITHDRAWAL", "TRANSFER"})
    @NotNull(message = "Type is required")
    private TransactionType type;
    
    @Schema(description = "Transaction Description", example = "Salary deposit")
    @NotBlank(message = "Description is required")
    private String description;
    
    @Schema(description = "Transaction Category", example = "Income")
    @NotBlank(message = "Category is required")
    private String category;
    
    @Schema(description = "Transaction Timestamp", example = "2023-12-01T10:15:30Z")
    private Instant timestamp;

    public Transaction() {
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
package com.example.transaction.management.controller;

import com.example.transaction.management.model.Transaction;
import com.example.transaction.management.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transaction Management", description = "Transaction Management API, including create, query, update and delete transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @Operation(summary = "Create New Transaction", description = "Create a new transaction record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction created successfully", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Transaction object", 
                required = true, 
                content = @Content(schema = @Schema(implementation = Transaction.class)))
            @RequestBody Transaction transaction) {
        return ResponseEntity.ok(service.createTransaction(transaction));
    }

    @Operation(summary = "Get Transaction Details", description = "Get transaction details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction found", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable UUID id) {
        Optional<Transaction> transaction = service.getTransaction(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get All Transactions", description = "Get all transaction records with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction list", 
                    content = @Content(schema = @Schema(implementation = Transaction.class)))
    })
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @Parameter(description = "Page number (starting from 0)") 
            @RequestParam(defaultValue = "0") int page, 
            @Parameter(description = "Number of records per page") 
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllTransactions(page, size));
    }

    @Operation(summary = "Update Transaction", description = "Update transaction information by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable UUID id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated transaction object", 
                required = true, 
                content = @Content(schema = @Schema(implementation = Transaction.class)))
            @RequestBody Transaction transaction) {
        try {
            return ResponseEntity.ok(service.updateTransaction(id, transaction));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete Transaction", description = "Delete transaction by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable UUID id) {
        service.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}
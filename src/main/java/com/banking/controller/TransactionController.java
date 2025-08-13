package com.banking.controller;

import com.banking.dto.DepositRequest;
import com.banking.dto.TransferRequest;
import com.banking.dto.WithdrawalRequest;
import com.banking.entity.Transaction;
import com.banking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for transaction operations
 */
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Management", description = "Operations for banking transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Transfer money between accounts")
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transferMoney(@Valid @RequestBody TransferRequest request) {
        Transaction transaction = transactionService.transferMoney(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("transaction", transaction);
        response.put("message", "Transfer completed successfully");
        response.put("transactionId", transaction.getTransactionId());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Deposit money to an account")
    @PostMapping("/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@Valid @RequestBody DepositRequest request) {
        Transaction transaction = transactionService.deposit(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("transaction", transaction);
        response.put("message", "Deposit completed successfully");
        response.put("transactionId", transaction.getTransactionId());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Withdraw money from an account")
    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@Valid @RequestBody WithdrawalRequest request) {
        Transaction transaction = transactionService.withdraw(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("transaction", transaction);
        response.put("message", "Withdrawal completed successfully");
        response.put("transactionId", transaction.getTransactionId());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get transaction by ID")
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Get transaction history for an account")
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<Page<Transaction>> getTransactionHistory(
            @PathVariable String accountNumber,
            Pageable pageable) {
        Page<Transaction> transactions = transactionService.getTransactionHistory(accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by type for an account")
    @GetMapping("/account/{accountNumber}/type/{type}")
    public ResponseEntity<Page<Transaction>> getTransactionsByType(
            @PathVariable String accountNumber,
            @PathVariable Transaction.TransactionType type,
            Pageable pageable) {
        Page<Transaction> transactions = transactionService.getTransactionsByType(accountNumber, type, pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by date range for an account")
    @GetMapping("/account/{accountNumber}/date-range")
    public ResponseEntity<Page<Transaction>> getTransactionsByDateRange(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        Page<Transaction> transactions = transactionService.getTransactionsByDateRange(
                accountNumber, startDate, endDate, pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get daily transaction amount for an account")
    @GetMapping("/account/{accountNumber}/daily-amount")
    public ResponseEntity<BigDecimal> getDailyTransactionAmount(@PathVariable String accountNumber) {
        BigDecimal amount = transactionService.getDailyTransactionAmount(accountNumber);
        return ResponseEntity.ok(amount);
    }

    @Operation(summary = "Get failed transactions")
    @GetMapping("/failed")
    public ResponseEntity<Page<Transaction>> getFailedTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionService.getFailedTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get large transactions")
    @GetMapping("/large")
    public ResponseEntity<Page<Transaction>> getLargeTransactions(
            @RequestParam(defaultValue = "10000") BigDecimal minAmount,
            Pageable pageable) {
        Page<Transaction> transactions = transactionService.getLargeTransactions(minAmount, pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Process pending transactions (cleanup)")
    @PostMapping("/process-pending")
    public ResponseEntity<Map<String, String>> processPendingTransactions() {
        transactionService.processPendingTransactions();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pending transactions processed successfully");
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction statistics")
    @GetMapping("/account/{accountNumber}/stats")
    public ResponseEntity<Map<String, Object>> getTransactionStats(@PathVariable String accountNumber) {
        BigDecimal dailyAmount = transactionService.getDailyTransactionAmount(accountNumber);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("dailyTransactionAmount", dailyAmount);
        stats.put("accountNumber", accountNumber);
        stats.put("date", LocalDateTime.now().toLocalDate());
        
        return ResponseEntity.ok(stats);
    }
}
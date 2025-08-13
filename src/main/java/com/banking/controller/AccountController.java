package com.banking.controller;

import com.banking.dto.CreateAccountRequest;
import com.banking.entity.Account;
import com.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for account operations
 */
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "Operations for managing bank accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account")
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @Operation(summary = "Get account by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Get account by account number")
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Get account balance")
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable String accountNumber) {
        BigDecimal balance = accountService.getAccountBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Get accounts by customer ID")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomer(@PathVariable Long customerId) {
        List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get active accounts by customer ID")
    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<List<Account>> getActiveAccountsByCustomer(@PathVariable Long customerId) {
        List<Account> accounts = accountService.getActiveAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Update account status")
    @PatchMapping("/{accountNumber}/status")
    public ResponseEntity<Account> updateAccountStatus(@PathVariable String accountNumber, 
                                                      @RequestParam Account.AccountStatus status) {
        Account account = accountService.updateAccountStatus(accountNumber, status);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Get total balance for customer")
    @GetMapping("/customer/{customerId}/total-balance")
    public ResponseEntity<BigDecimal> getTotalBalance(@PathVariable Long customerId) {
        BigDecimal totalBalance = accountService.calculateTotalBalance(customerId);
        return ResponseEntity.ok(totalBalance);
    }

    @Operation(summary = "Close account")
    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<Account> closeAccount(@PathVariable String accountNumber) {
        Account account = accountService.closeAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Get all accounts")
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts by type")
    @GetMapping("/type/{accountType}")
    public ResponseEntity<List<Account>> getAccountsByType(@PathVariable Account.AccountType accountType) {
        List<Account> accounts = accountService.getAccountsByType(accountType);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Calculate interest for all eligible accounts")
    @PostMapping("/calculate-interest")
    public ResponseEntity<String> calculateInterest() {
        accountService.calculateInterest();
        return ResponseEntity.ok("Interest calculation completed");
    }
}
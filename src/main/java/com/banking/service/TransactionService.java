package com.banking.service;

import com.banking.dto.DepositRequest;
import com.banking.dto.TransferRequest;
import com.banking.dto.WithdrawalRequest;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.exception.AccountInactiveException;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.DailyLimitExceededException;
import com.banking.exception.InsufficientFundsException;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for transaction operations with complex banking scenarios
 */
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    
    @Value("${banking.transfer.minimum.amount:0.01}")
    private BigDecimal minimumTransferAmount;
    
    @Value("${banking.transfer.maximum.amount:50000.00}")
    private BigDecimal maximumTransferAmount;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Transfer money between accounts with comprehensive validation
     */
    public Transaction transferMoney(TransferRequest request) {
        // Validate transfer request
        validateTransferRequest(request);

        // Get accounts
        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(request.getFromAccountNumber()));
        
        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(request.getToAccountNumber()));

        // Validate accounts and transaction
        validateTransferTransaction(fromAccount, toAccount, request.getAmount());

        // Create transaction record
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(
                transactionId,
                Transaction.TransactionType.TRANSFER,
                request.getAmount(),
                request.getDescription() != null ? request.getDescription() : "Transfer between accounts",
                fromAccount,
                toAccount
        );

        try {
            // Perform the transfer
            fromAccount.debit(request.getAmount());
            toAccount.credit(request.getAmount());

            // Save updated accounts
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Mark transaction as completed
            transaction.markAsCompleted(fromAccount.getBalance());
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        } catch (Exception e) {
            // Mark transaction as failed
            transaction.markAsFailed(e.getMessage());
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }

        return transactionRepository.save(transaction);
    }

    /**
     * Deposit money to an account
     */
    public Transaction deposit(DepositRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(request.getAccountNumber()));

        // Validate account
        if (!account.isActive()) {
            throw new AccountInactiveException(account.getAccountNumber(), account.getStatus().toString());
        }

        // Validate amount
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        // Create transaction
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(
                transactionId,
                Transaction.TransactionType.DEPOSIT,
                request.getAmount(),
                request.getDescription() != null ? request.getDescription() : "Deposit to account",
                null,
                account
        );

        try {
            // Perform deposit
            account.credit(request.getAmount());
            accountRepository.save(account);

            // Mark transaction as completed
            transaction.markAsCompleted(account.getBalance());

        } catch (Exception e) {
            transaction.markAsFailed(e.getMessage());
            throw new RuntimeException("Deposit failed: " + e.getMessage(), e);
        }

        return transactionRepository.save(transaction);
    }

    /**
     * Withdraw money from an account
     */
    public Transaction withdraw(WithdrawalRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(request.getAccountNumber()));

        // Validate withdrawal
        validateWithdrawal(account, request.getAmount());

        // Create transaction
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(
                transactionId,
                Transaction.TransactionType.WITHDRAWAL,
                request.getAmount(),
                request.getDescription() != null ? request.getDescription() : "Withdrawal from account",
                account,
                null
        );

        try {
            // Perform withdrawal
            account.debit(request.getAmount());
            accountRepository.save(account);

            // Mark transaction as completed
            transaction.markAsCompleted(account.getBalance());

        } catch (Exception e) {
            transaction.markAsFailed(e.getMessage());
            throw new RuntimeException("Withdrawal failed: " + e.getMessage(), e);
        }

        return transactionRepository.save(transaction);
    }

    /**
     * Get transaction history for an account
     */
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionHistory(String accountNumber, Pageable pageable) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(accountNumber));
        
        return transactionRepository.findTransactionsByAccount(account, pageable);
    }

    /**
     * Get transactions by type for an account
     */
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsByType(String accountNumber, Transaction.TransactionType type, Pageable pageable) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(accountNumber));
        
        return transactionRepository.findTransactionsByAccountAndType(account, type, pageable);
    }

    /**
     * Get transactions within date range
     */
    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsByDateRange(String accountNumber, LocalDateTime startDate, 
                                                       LocalDateTime endDate, Pageable pageable) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(accountNumber));
        
        return transactionRepository.findTransactionsByAccountAndDateRange(account, startDate, endDate, pageable);
    }

    /**
     * Get transaction by transaction ID
     */
    @Transactional(readOnly = true)
    public Transaction getTransactionById(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));
    }

    /**
     * Process pending transactions (cleanup job)
     */
    public void processPendingTransactions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(30);
        List<Transaction> pendingTransactions = transactionRepository.findPendingTransactionsOlderThan(cutoffTime);

        for (Transaction transaction : pendingTransactions) {
            transaction.markAsFailed("Transaction timeout - pending for more than 30 minutes");
            transactionRepository.save(transaction);
        }
    }

    /**
     * Calculate daily transaction amount for an account
     */
    @Transactional(readOnly = true)
    public BigDecimal getDailyTransactionAmount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(accountNumber));
        
        return transactionRepository.calculateDailyTransactionAmount(account, LocalDateTime.now());
    }

    /**
     * Validate transfer request
     */
    private void validateTransferRequest(TransferRequest request) {
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        if (request.getAmount().compareTo(minimumTransferAmount) < 0) {
            throw new IllegalArgumentException("Transfer amount is below minimum: " + minimumTransferAmount);
        }

        if (request.getAmount().compareTo(maximumTransferAmount) > 0) {
            throw new IllegalArgumentException("Transfer amount exceeds maximum: " + maximumTransferAmount);
        }
    }

    /**
     * Validate transfer transaction with comprehensive checks
     */
    private void validateTransferTransaction(Account fromAccount, Account toAccount, BigDecimal amount) {
        // Check if accounts are active
        if (!fromAccount.isActive()) {
            throw new AccountInactiveException(fromAccount.getAccountNumber(), fromAccount.getStatus().toString());
        }

        if (!toAccount.isActive()) {
            throw new AccountInactiveException(toAccount.getAccountNumber(), toAccount.getStatus().toString());
        }

        // Check sufficient funds
        if (!fromAccount.hasSufficientBalance(amount)) {
            throw new InsufficientFundsException(fromAccount.getAccountNumber(), amount.toString());
        }

        // Check daily transaction limit
        BigDecimal dailyAmount = getDailyTransactionAmount(fromAccount.getAccountNumber());
        if (dailyAmount.add(amount).compareTo(fromAccount.getDailyTransactionLimit()) > 0) {
            throw new DailyLimitExceededException(
                    fromAccount.getAccountNumber(),
                    fromAccount.getDailyTransactionLimit().toString(),
                    amount.toString()
            );
        }
    }

    /**
     * Validate withdrawal with comprehensive checks
     */
    private void validateWithdrawal(Account account, BigDecimal amount) {
        // Check if account is active
        if (!account.isActive()) {
            throw new AccountInactiveException(account.getAccountNumber(), account.getStatus().toString());
        }

        // Check sufficient funds
        if (!account.hasSufficientBalance(amount)) {
            throw new InsufficientFundsException(account.getAccountNumber(), amount.toString());
        }

        // Check minimum withdrawal amount
        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            throw new IllegalArgumentException("Withdrawal amount is too small");
        }

        // Check daily transaction limit
        BigDecimal dailyAmount = getDailyTransactionAmount(account.getAccountNumber());
        if (dailyAmount.add(amount).compareTo(account.getDailyTransactionLimit()) > 0) {
            throw new DailyLimitExceededException(
                    account.getAccountNumber(),
                    account.getDailyTransactionLimit().toString(),
                    amount.toString()
            );
        }
    }

    /**
     * Generate unique transaction ID
     */
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Get failed transactions for monitoring
     */
    @Transactional(readOnly = true)
    public Page<Transaction> getFailedTransactions(Pageable pageable) {
        return transactionRepository.findFailedTransactions(pageable);
    }

    /**
     * Get large transactions for monitoring
     */
    @Transactional(readOnly = true)
    public Page<Transaction> getLargeTransactions(BigDecimal minAmount, Pageable pageable) {
        return transactionRepository.findLargeTransactions(minAmount, pageable);
    }
}
package com.banking.service;

import com.banking.dto.CreateAccountRequest;
import com.banking.entity.Account;
import com.banking.entity.Customer;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.CustomerNotFoundException;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for account operations
 */
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Value("${banking.interest.savings.rate:0.025}")
    private BigDecimal savingsInterestRate;

    @Value("${banking.interest.checking.rate:0.001}")
    private BigDecimal checkingInterestRate;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Create a new account
     */
    public Account createAccount(CreateAccountRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        // Generate unique account number
        String accountNumber = generateAccountNumber();
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }

        Account account = new Account(accountNumber, request.getAccountType(), customer, request.getInitialBalance());
        
        // Set interest rate based on account type
        switch (request.getAccountType()) {
            case SAVINGS:
                account.setInterestRate(savingsInterestRate);
                account.setDailyTransactionLimit(new BigDecimal("5000.00"));
                break;
            case CHECKING:
                account.setInterestRate(checkingInterestRate);
                account.setDailyTransactionLimit(new BigDecimal("10000.00"));
                break;
            case BUSINESS:
                account.setInterestRate(new BigDecimal("0.015"));
                account.setDailyTransactionLimit(new BigDecimal("50000.00"));
                break;
        }

        return accountRepository.save(account);
    }

    /**
     * Get account by ID
     */
    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * Get account by account number
     */
    @Transactional(readOnly = true)
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> AccountNotFoundException.byAccountNumber(accountNumber));
    }

    /**
     * Get all accounts for a customer
     */
    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    /**
     * Get active accounts for a customer
     */
    @Transactional(readOnly = true)
    public List<Account> getActiveAccountsByCustomerId(Long customerId) {
        return accountRepository.findActiveAccountsByCustomerId(customerId);
    }

    /**
     * Get account balance
     */
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);
        return account.getBalance();
    }

    /**
     * Update account status
     */
    public Account updateAccountStatus(String accountNumber, Account.AccountStatus status) {
        Account account = getAccountByAccountNumber(accountNumber);
        account.setStatus(status);
        return accountRepository.save(account);
    }

    /**
     * Calculate total balance for a customer
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalBalance(Long customerId) {
        return accountRepository.calculateTotalBalanceByCustomerId(customerId);
    }

    /**
     * Calculate and apply interest to eligible accounts
     */
    public void calculateInterest() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(1);
        List<Account> accounts = accountRepository.findAccountsNeedingInterestCalculation(cutoffDate);

        for (Account account : accounts) {
            if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal interest = account.getBalance()
                        .multiply(account.getInterestRate())
                        .divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP); // Monthly interest

                account.credit(interest);
                account.setLastInterestCalculation(LocalDateTime.now());
                accountRepository.save(account);
            }
        }
    }

    /**
     * Close account
     */
    public Account closeAccount(String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Cannot close account with non-zero balance");
        }

        account.setStatus(Account.AccountStatus.CLOSED);
        return accountRepository.save(account);
    }

    /**
     * Validate account for transactions
     */
    public void validateAccountForTransaction(Account account, BigDecimal amount) {
        if (!account.isActive()) {
            throw new IllegalArgumentException("Account is not active: " + account.getAccountNumber());
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }

        // Check daily transaction limit
        LocalDateTime today = LocalDateTime.now();
        BigDecimal dailyTotal = accountRepository.findById(account.getId())
                .map(acc -> {
                    // In a real implementation, you would sum today's transactions
                    // For simplicity, we'll use the daily limit check
                    return BigDecimal.ZERO;
                })
                .orElse(BigDecimal.ZERO);

        if (dailyTotal.add(amount).compareTo(account.getDailyTransactionLimit()) > 0) {
            throw new IllegalArgumentException("Daily transaction limit exceeded");
        }
    }

    /**
     * Generate unique account number
     */
    private String generateAccountNumber() {
        // Generate a 10-digit account number
        return String.format("%010d", Math.abs(UUID.randomUUID().hashCode() % 10000000000L));
    }

    /**
     * Get all accounts
     */
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Get accounts by type
     */
    @Transactional(readOnly = true)
    public List<Account> getAccountsByType(Account.AccountType accountType) {
        return accountRepository.findByAccountType(accountType);
    }
}
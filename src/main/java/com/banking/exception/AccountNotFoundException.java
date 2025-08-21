package com.banking.exception;

/**
 * Exception thrown when an account is not found
 */
public class AccountNotFoundException extends BankingException {
    
    public AccountNotFoundException(String message) {
        super(message);
    }
    
    public AccountNotFoundException(Long accountId) {
        super(String.format("Account not found with ID: %s", accountId));
    }
    
    public static AccountNotFoundException byAccountNumber(String accountNumber) {
        return new AccountNotFoundException(String.format("Account not found: %s", accountNumber));
    }
}
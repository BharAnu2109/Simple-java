package com.banking.exception;

/**
 * Exception thrown when an account is inactive
 */
public class AccountInactiveException extends BankingException {
    
    public AccountInactiveException(String message) {
        super(message);
    }
    
    public AccountInactiveException(String accountNumber, String status) {
        super(String.format("Account %s is inactive. Current status: %s", accountNumber, status));
    }
}
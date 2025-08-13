package com.banking.exception;

/**
 * Exception thrown when insufficient funds are available for a transaction
 */
public class InsufficientFundsException extends BankingException {
    
    public InsufficientFundsException(String message) {
        super(message);
    }
    
    public InsufficientFundsException(String accountNumber, String amount) {
        super(String.format("Insufficient funds in account %s. Requested amount: %s", accountNumber, amount));
    }
}
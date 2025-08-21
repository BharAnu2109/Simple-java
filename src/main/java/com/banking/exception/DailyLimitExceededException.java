package com.banking.exception;

/**
 * Exception thrown when daily transaction limit is exceeded
 */
public class DailyLimitExceededException extends BankingException {
    
    public DailyLimitExceededException(String message) {
        super(message);
    }
    
    public DailyLimitExceededException(String accountNumber, String dailyLimit, String requestedAmount) {
        super(String.format("Daily transaction limit exceeded for account %s. Limit: %s, Requested: %s", 
                accountNumber, dailyLimit, requestedAmount));
    }
}
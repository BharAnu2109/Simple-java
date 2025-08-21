package com.banking.exception;

/**
 * Exception thrown when a customer is not found
 */
public class CustomerNotFoundException extends BankingException {
    
    public CustomerNotFoundException(String message) {
        super(message);
    }
    
    public CustomerNotFoundException(Long customerId) {
        super(String.format("Customer not found with ID: %s", customerId));
    }
    
    public static CustomerNotFoundException byEmail(String email) {
        return new CustomerNotFoundException(String.format("Customer not found with email: %s", email));
    }
}
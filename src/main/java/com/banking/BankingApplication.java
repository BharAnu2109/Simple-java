package com.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Banking Application - A comprehensive SpringBoot application for banking operations
 * with complex scenarios including account management, transfers, and transaction history.
 */
@SpringBootApplication
@EnableTransactionManagement
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
}
package com.banking.repository;

import com.banking.entity.Account;
import com.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Account entity
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find all accounts for a customer
     */
    List<Account> findByCustomer(Customer customer);

    /**
     * Find all accounts for a customer by customer ID
     */
    List<Account> findByCustomerId(Long customerId);

    /**
     * Find active accounts for a customer
     */
    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId AND a.status = 'ACTIVE'")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") Long customerId);

    /**
     * Find accounts by type
     */
    List<Account> findByAccountType(Account.AccountType accountType);

    /**
     * Find accounts by status
     */
    List<Account> findByStatus(Account.AccountStatus status);

    /**
     * Find accounts with balance greater than specified amount
     */
    @Query("SELECT a FROM Account a WHERE a.balance > :minBalance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);

    /**
     * Find accounts that need interest calculation
     */
    @Query("SELECT a FROM Account a WHERE a.accountType IN ('SAVINGS', 'BUSINESS') " +
           "AND (a.lastInterestCalculation IS NULL OR a.lastInterestCalculation < :cutoffDate)")
    List<Account> findAccountsNeedingInterestCalculation(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Count accounts by customer
     */
    long countByCustomer(Customer customer);

    /**
     * Find accounts by customer and type
     */
    List<Account> findByCustomerAndAccountType(Customer customer, Account.AccountType accountType);

    /**
     * Calculate total balance for a customer
     */
    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a WHERE a.customer.id = :customerId AND a.status = 'ACTIVE'")
    BigDecimal calculateTotalBalanceByCustomerId(@Param("customerId") Long customerId);
}
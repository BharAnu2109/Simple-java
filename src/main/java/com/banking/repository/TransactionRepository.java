package com.banking.repository;

import com.banking.entity.Account;
import com.banking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transaction by transaction ID
     */
    Optional<Transaction> findByTransactionId(String transactionId);

    /**
     * Find all transactions for an account (both incoming and outgoing)
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account ORDER BY t.transactionDate DESC")
    Page<Transaction> findTransactionsByAccount(@Param("account") Account account, Pageable pageable);

    /**
     * Find outgoing transactions from an account
     */
    Page<Transaction> findByFromAccountOrderByTransactionDateDesc(Account fromAccount, Pageable pageable);

    /**
     * Find incoming transactions to an account
     */
    Page<Transaction> findByToAccountOrderByTransactionDateDesc(Account toAccount, Pageable pageable);

    /**
     * Find transactions by type for an account
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
           "AND t.transactionType = :type ORDER BY t.transactionDate DESC")
    Page<Transaction> findTransactionsByAccountAndType(@Param("account") Account account, 
                                                      @Param("type") Transaction.TransactionType type, 
                                                      Pageable pageable);

    /**
     * Find transactions within date range for an account
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    Page<Transaction> findTransactionsByAccountAndDateRange(@Param("account") Account account,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate,
                                                           Pageable pageable);

    /**
     * Find transactions by status
     */
    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    /**
     * Calculate total transaction amount for an account on a specific date
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.fromAccount = :account " +
           "AND DATE(t.transactionDate) = DATE(:date) AND t.status = 'COMPLETED'")
    BigDecimal calculateDailyTransactionAmount(@Param("account") Account account, @Param("date") LocalDateTime date);

    /**
     * Find failed transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = 'FAILED' ORDER BY t.transactionDate DESC")
    Page<Transaction> findFailedTransactions(Pageable pageable);

    /**
     * Find pending transactions older than specified time
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = 'PENDING' AND t.createdAt < :cutoffTime")
    List<Transaction> findPendingTransactionsOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Count transactions by type for an account
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
           "AND t.transactionType = :type AND t.status = 'COMPLETED'")
    long countTransactionsByAccountAndType(@Param("account") Account account, 
                                          @Param("type") Transaction.TransactionType type);

    /**
     * Find transactions between two accounts
     */
    @Query("SELECT t FROM Transaction t WHERE " +
           "((t.fromAccount = :account1 AND t.toAccount = :account2) OR " +
           "(t.fromAccount = :account2 AND t.toAccount = :account1)) " +
           "ORDER BY t.transactionDate DESC")
    Page<Transaction> findTransactionsBetweenAccounts(@Param("account1") Account account1,
                                                     @Param("account2") Account account2,
                                                     Pageable pageable);

    /**
     * Find large transactions above a certain amount
     */
    @Query("SELECT t FROM Transaction t WHERE t.amount >= :minAmount AND t.status = 'COMPLETED' " +
           "ORDER BY t.amount DESC")
    Page<Transaction> findLargeTransactions(@Param("minAmount") BigDecimal minAmount, Pageable pageable);
}
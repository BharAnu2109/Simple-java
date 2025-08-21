package com.banking.repository;

import com.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find customer by email address
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Find customers by first name and last name (case insensitive)
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) = LOWER(:firstName) AND LOWER(c.lastName) = LOWER(:lastName)")
    List<Customer> findByFirstNameAndLastNameIgnoreCase(@Param("firstName") String firstName, 
                                                        @Param("lastName") String lastName);

    /**
     * Find customers by phone number
     */
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find customers with accounts
     */
    @Query("SELECT DISTINCT c FROM Customer c JOIN FETCH c.accounts")
    List<Customer> findCustomersWithAccounts();

    /**
     * Search customers by name (first or last name containing the search term)
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchByName(@Param("searchTerm") String searchTerm);
}
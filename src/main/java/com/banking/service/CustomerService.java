package com.banking.service;

import com.banking.dto.CreateCustomerRequest;
import com.banking.entity.Customer;
import com.banking.exception.CustomerNotFoundException;
import com.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for customer operations
 */
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Create a new customer
     */
    public Customer createCustomer(CreateCustomerRequest request) {
        // Check if email already exists
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + request.getEmail() + " already exists");
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());

        return customerRepository.save(customer);
    }

    /**
     * Get customer by ID
     */
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    /**
     * Get customer by email
     */
    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> CustomerNotFoundException.byEmail(email));
    }

    /**
     * Get all customers
     */
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Update customer information
     */
    public Customer updateCustomer(Long id, CreateCustomerRequest request) {
        Customer customer = getCustomerById(id);

        // Check if new email conflicts with another customer
        if (!customer.getEmail().equals(request.getEmail()) && 
            customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + request.getEmail() + " already exists");
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());

        return customerRepository.save(customer);
    }

    /**
     * Delete customer (soft delete by marking as inactive)
     */
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        // In a real application, you might want to mark as inactive instead of deleting
        // For simplicity, we'll delete but check if customer has accounts first
        if (customer.getAccounts() != null && !customer.getAccounts().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete customer with active accounts");
        }
        customerRepository.delete(customer);
    }

    /**
     * Search customers by name
     */
    @Transactional(readOnly = true)
    public List<Customer> searchCustomersByName(String searchTerm) {
        return customerRepository.searchByName(searchTerm);
    }

    /**
     * Get customers with accounts
     */
    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithAccounts() {
        return customerRepository.findCustomersWithAccounts();
    }
}
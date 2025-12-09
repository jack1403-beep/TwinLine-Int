package com.example.loanmgmt.repository;

import com.example.loanmgmt.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JpaRepository for Customer CRUD operations.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}

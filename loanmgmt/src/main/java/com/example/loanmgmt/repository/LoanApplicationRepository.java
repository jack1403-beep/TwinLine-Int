package com.example.loanmgmt.repository;


import com.example.loanmgmt.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JpaRepository for LoanApplication.
 */
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    List<LoanApplication> findByCustomerId(Long customerId);
    
    List<LoanApplication> findByStatus(String status);
    
    List<LoanApplication> findByCustomer_NameContainingIgnoreCase(String name);
    
    List<LoanApplication> findByStatusAndCustomer_NameContainingIgnoreCase(
            String status, String name
    );

}

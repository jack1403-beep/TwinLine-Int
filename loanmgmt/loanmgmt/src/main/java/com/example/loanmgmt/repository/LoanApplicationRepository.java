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

    // Custom repository method — find loans by status and customer name (joins)
    @Query("SELECT l FROM LoanApplication l WHERE l.status = :status AND lower(l.customer.name) LIKE lower(concat('%', :name, '%'))")
    List<LoanApplication> findByStatusAndCustomerName(@Param("status") String status, @Param("name") String name);
}

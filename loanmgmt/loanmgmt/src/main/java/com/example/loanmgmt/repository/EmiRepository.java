package com.example.loanmgmt.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanmgmt.model.Emi;

import java.util.List;

public interface EmiRepository extends JpaRepository<Emi, Long> {
    List<Emi> findByLoanApplicationIdOrderBySequenceNumber(Long loanId);
}
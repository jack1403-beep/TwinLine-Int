package com.example.loanmgmt.service;

import com.example.loanmgmt.model.Emi;
import com.example.loanmgmt.model.LoanApplication;
import com.example.loanmgmt.modeldto.LoanApplyRequest;

import java.util.List;

/**
 * Service interface for loan operations.
 */
public interface LoanService {
	
	LoanApplication applyForLoan(LoanApplyRequest req);
	LoanApplication getLoan(Long loanId);
	List<LoanApplication> getLoansByCustomer(Long customerId);
	LoanApplication approveLoan(Long loanId);
	Emi payEmi(Long emiId, double amount);
}
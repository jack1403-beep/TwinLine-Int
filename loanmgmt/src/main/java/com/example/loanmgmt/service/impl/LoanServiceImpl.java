package com.example.loanmgmt.service.impl;

import com.example.loanmgmt.model.LoanApplication;
import com.example.loanmgmt.modeldto.LoanApplyRequest;
import com.example.loanmgmt.model.Customer;
import com.example.loanmgmt.model.Emi;
import com.example.loanmgmt.repository.LoanApplicationRepository;
import com.example.loanmgmt.repository.EmiRepository;
import com.example.loanmgmt.repository.CustomerRepository;
import com.example.loanmgmt.service.LoanService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of LoanService with business logic including EMI calculation and eligibility.
 */
@Service
public class LoanServiceImpl implements LoanService {
	
	@Autowired
	LoanApplicationRepository loanRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	EmiRepository emiRepo;
	
	
	@Override
	@Transactional
	public LoanApplication applyForLoan(LoanApplyRequest req) {
		 Customer customer = customerRepo.findById(req.getCustomerId())
				 .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
		// eligibility check: age between 21-60
		 int age = LocalDate.now().getYear() - customer.getDob().getYear();
		 if (age < 21 || age > 60) throw new IllegalArgumentException("Customer not eligible by age");
		 
		// amount already validated via DTO, but double-check
		 if (req.getAmount().compareTo(new BigDecimal("50000")) < 0 ||
				 req.getAmount().compareTo(new BigDecimal("1000000")) > 0) {
			 throw new IllegalArgumentException("Loan amount out of allowed range");
		 }
		 
		 LoanApplication la = new LoanApplication();
		 la.setApplicationNumber("LA-" + System.currentTimeMillis());
		 la.setCustomer(customer);
		 la.setAmount(req.getAmount());
         la.setTenureMonths(req.getTenureMonths());
		 la.setAnnualInterestRate(req.getAnnualInterestRate());
		 la.setPurpose(req.getPurpose());
		 la.setStatus("PENDING");
		 return loanRepo.save(la);
	}
	
	@Override
	public LoanApplication getLoan(Long loanId) {
		return loanRepo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
	}

	
	 @Override
	 public List<LoanApplication> getLoansByCustomer(Long customerId) {
		 return loanRepo.findByCustomerId(customerId);
	 }
	 
	 @Override
	 @Transactional
	 public LoanApplication approveLoan(Long loanId) {
		 LoanApplication loan = loanRepo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
		 if (!"PENDING".equals(loan.getStatus())) {
			 throw new IllegalStateException("Only pending loans can be approved");
		 }
		// set approvedAt and status
		loan.setApprovedAt(java.time.LocalDateTime.now());
		loan.setStatus("APPROVED");
		
		// generate EMI schedule using reducing balance formula
		List<Emi> emiList = generateEmiSchedule(loan);
		loan.setEmis(emiList);
		LoanApplication saved = loanRepo.save(loan);
		emiRepo.saveAll(emiList);
		return saved;
	 }
	 
	 @Override
	 @Transactional
	 public Emi payEmi(Long emiId, double amount) {
		Emi emi = emiRepo.findById(emiId).orElseThrow(() -> new IllegalArgumentException("EMI not found"));
		if ("PAID".equals(emi.getStatus())) throw new IllegalStateException("EMI already paid");
		
		// For simplicity assume amount == emi.amount
		emi.setStatus("PAID");
		emi.setPaidAt(java.time.LocalDateTime.now());
		emiRepo.save(emi);
		
		// update loan outstanding and if all paid, change loan status
		Long loanId = emi.getLoanApplication().getId();
		List<Emi> remaining = emiRepo.findByLoanApplicationIdOrderBySequenceNumber(loanId);
		boolean allPaid = remaining.stream().allMatch(e -> "PAID".equals(e.getStatus()));
		
		if (allPaid) {
			LoanApplication loan = loanRepo.findById(loanId).get();
			loan.setStatus("CLOSED");
		    loanRepo.save(loan);
		}
		return emi;
		 
	 }

      private List<Emi> generateEmiSchedule(LoanApplication loan) {
    	BigDecimal P = loan.getAmount();
    	int n = loan.getTenureMonths();
    	BigDecimal annualRate = loan.getAnnualInterestRate();
    	BigDecimal monthlyRate = annualRate.divide(new BigDecimal("1200"), 8, BigDecimal.ROUND_HALF_UP); // r
    	//EMI = P*r*(1+r)^n / ((1+r)^n -1)
    	BigDecimal onePlusRPowerN = (BigDecimal.ONE.add(monthlyRate)).pow(n);
    	BigDecimal numerator = P.multiply(monthlyRate).multiply(onePlusRPowerN);
    	BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
    	BigDecimal emiAmount = numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    	List<Emi> list = new ArrayList<>();
    	BigDecimal outstanding = P;
    	java.time.LocalDate due = java.time.LocalDate.now().plusMonths(1);
    	for (int i = 1; i <= n; i++) {
    		BigDecimal interest = outstanding.multiply(monthlyRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    		BigDecimal principal = emiAmount.subtract(interest).setScale(2, BigDecimal.ROUND_HALF_UP);
    		outstanding = outstanding.subtract(principal).setScale(2, BigDecimal.ROUND_HALF_UP);
    		if (outstanding.compareTo(BigDecimal.ZERO) < 0) outstanding = BigDecimal.ZERO;
    		Emi emi = new Emi();
    		emi.setLoanApplication(loan);
    		emi.setSequenceNumber(i);
    		emi.setDueDate(due);
    		emi.setPrincipalComponent(principal);
    		emi.setInterestComponent(interest);
    		emi.setAmount(emiAmount);
    		emi.setOutstandingAfterPayment(outstanding);
    		list.add(emi);
    		due = due.plusMonths(1);
    	}
    	return list;
      }
      
      public List<LoanApplication> getAllLoans(){
    	  return loanRepo.findAll();
      }
      
      @Override
      public List<LoanApplication> searchLoans(String status, String name) {

    	  if (status != null && name != null) {
    	      return loanRepo.findByStatusAndCustomer_NameContainingIgnoreCase(status, name);
    	  }

    	  if (status != null) {
    	      return loanRepo.findByStatus(status);
    	  }

    	  if (name != null) {
    	      return loanRepo.findByCustomer_NameContainingIgnoreCase(name);
    	  }

    	  return loanRepo.findAll();
    }
      
      @Override
      public LoanApplication rejectLoan(Long loanId) {
    	  LoanApplication la = loanRepo.findById(loanId)
    			  .orElseThrow(() -> new RuntimeException("Loan not found"));
    	  if (!la.getStatus().equals("PENDING")) {
    		  throw new RuntimeException("Only PENDING loans can be rejected");
    	  }
    	  la.setStatus("REJECTED");
    	  return loanRepo.save(la);
      }
      
      
}

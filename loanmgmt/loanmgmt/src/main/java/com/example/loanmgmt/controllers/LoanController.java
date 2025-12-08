package com.example.loanmgmt.controllers;

import com.example.loanmgmt.model.LoanApplication;
import com.example.loanmgmt.modeldto.CustomerDto;
import com.example.loanmgmt.modeldto.LoanApplyRequest;
import com.example.loanmgmt.modeldto.PaymentRequest;
import com.example.loanmgmt.model.Customer;
import com.example.loanmgmt.model.Emi;
import com.example.loanmgmt.service.LoanService;
import com.example.loanmgmt.repository.CustomerRepository;
import com.example.loanmgmt.repository.EmiRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class LoanController {
	
	
	@Autowired
	LoanService loanService;

	@Autowired
	EmiRepository emiRepository;
	
	@Autowired
	CustomerRepository customerRepo;

    @PostMapping("/customers")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDto dto) {
    	 Customer customer = new Customer();
    	 customer.setName(dto.getName());
    	 customer.setEmail(dto.getEmail());
    	 customer.setPhone(dto.getPhone());
    	 customer.setPan(dto.getPan());
    	 customer.setAadhar(dto.getAadhar());
    	 customer.setDob(dto.getDob());

    	 Customer saved = customerRepo.save(customer);

    	 return ResponseEntity.status(201).body(new CustomerResponse(saved.getId(), "Registered")); 
    }
    
    @PostMapping("/loans/apply")
    public ResponseEntity<?> applyLoan(@Valid @RequestBody LoanApplyRequest req) {
        LoanApplication la = loanService.applyForLoan(req);
        return ResponseEntity.status(201).body(la);
    }
    
    @GetMapping("/loans/{loanId}")
    public ResponseEntity<?> getLoan(@PathVariable Long loanId) {
        LoanApplication la = loanService.getLoan(loanId);
        return ResponseEntity.ok(la);
    }
    
    @PostMapping("/loans/{loanId}/approve")
    public ResponseEntity<?> approveLoan(@PathVariable Long loanId) {
        LoanApplication la = loanService.approveLoan(loanId);
        return ResponseEntity.ok(la);
    }
    
    @GetMapping("/loans/customer/{customerId}")
    public ResponseEntity<?> getLoansByCustomer(@PathVariable Long customerId) {
        List<LoanApplication> loans = loanService.getLoansByCustomer(customerId);
        return ResponseEntity.ok(loans);
    }
    
    
    @PostMapping("/emi/{emiId}/pay")
    public ResponseEntity<?> payEmi(@PathVariable Long emiId, @RequestBody PaymentRequest req){
    	Emi emi = loanService.payEmi(emiId, req.getAmount().doubleValue());
    	return ResponseEntity.ok(emi);
    }    
}
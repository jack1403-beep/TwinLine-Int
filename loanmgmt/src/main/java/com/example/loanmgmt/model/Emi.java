package com.example.loanmgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * EMI entity represents a single installment row.
 */
@Entity
@Table(name = "emis")
@Getter
@Setter
public class Emi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="loan_id")
    private LoanApplication loanApplication;

    @Column(name="sequence_number", nullable=false)
    private Integer sequenceNumber;

    @Column(name="due_date", nullable=false)
    private LocalDate dueDate;

    @Column(name="principal_component", nullable=false, precision=12, scale=2)
    private BigDecimal principalComponent;

    @Column(name="interest_component", nullable=false, precision=12, scale=2)
    private BigDecimal interestComponent;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal amount;

    @Column(name="outstanding_after_payment", nullable=false, precision=12, scale=2)
    private BigDecimal outstandingAfterPayment;

    @Column(nullable=false)
    private String status = "DUE";

    @Column(name="paid_at")
    private LocalDateTime paidAt;
}
package com.example.loanmgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * LoanApplication entity holds application details.
 */
@Entity
@Table(name = "loan_applications")
@Getter
@Setter
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_number", unique = true, nullable = false)
    private String applicationNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name="tenure_months", nullable=false)
    private Integer tenureMonths;

    @Column(name="annual_interest_rate", nullable=false, precision = 5, scale = 2)
    private BigDecimal annualInterestRate;

    private String purpose;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name="applied_at")
    private LocalDateTime appliedAt = LocalDateTime.now();

    @Column(name="approved_at")
    private LocalDateTime approvedAt;

    @Column(name="disbursed_at")
    private LocalDateTime disbursedAt;

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emi> emis;

    // Getters/setters omitted for brevity
}

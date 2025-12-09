package com.example.loanmgmt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Customer entity represents a loan applicant or customer.
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=150)
    private String name;

    @Column(nullable=false, unique=true, length=150)
    private String email;

    @Column(nullable=false, length=10)
    private String phone;

    @Column(nullable=false, unique=true, length=10)
    private String pan;

    @Column(nullable=false, unique=true, length=12)
    private String aadhar;

    @Column(nullable=false)
    private LocalDate dob;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
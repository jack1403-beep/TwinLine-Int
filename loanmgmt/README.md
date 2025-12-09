# Loan Management System

## Overview
Simple Loan Management System using Spring Boot, Hibernate, MySQL, Thymeleaf, jQuery.

## Tech stack
- Java 17+, Spring Boot, Hibernate (JPA)
- MySQL
- Thymeleaf + jQuery
- Spring Security (basic)

## Setup
1. Create database: run `schema.sql`
2. Configure `application.properties`:
   spring.datasource.url=jdbc:mysql://localhost:3306/loan_mgmt
   spring.datasource.username=root
   spring.datasource.password=yourpass
   spring.jpa.hibernate.ddl-auto=validate
3. Build & run:
   mvn clean package
   java -jar target/loan-mgmt-0.0.1-SNAPSHOT.jar

## Endpoints
- POST /api/customers
- POST /api/loans/apply
- GET /api/loans/{loanId}
- POST /api/loans/{loanId}/approve
- GET /api/loans/customer/{customerId}
- POST /api/emi/{emiId}/pay

## Postman
Import provided collection: `postman_collection.json`

## Notes
- For demo, in-memory users: admin/admin123, user/user123
- Security: replace in-memory with DB-backed in production

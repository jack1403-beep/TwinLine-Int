-- schema.sql
CREATE DATABASE IF NOT EXISTS loan_mgmt;
USE loan_mgmt;

-- CUSTOMER TABLE
CREATE TABLE customers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  phone VARCHAR(10) NOT NULL,
  pan VARCHAR(10) UNIQUE NOT NULL,
  aadhar VARCHAR(12) UNIQUE NOT NULL,
  dob DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- LOAN APPLICATION TABLE
CREATE TABLE loan_applications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  application_number VARCHAR(30) UNIQUE NOT NULL,
  customer_id BIGINT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  tenure_months INT NOT NULL,
  annual_interest_rate DECIMAL(5,2) NOT NULL,
  purpose VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  approved_at TIMESTAMP NULL,
  disbursed_at TIMESTAMP NULL,
  CONSTRAINT fk_loan_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE INDEX idx_loan_customer ON loan_applications(customer_id);
CREATE INDEX idx_loan_status ON loan_applications(status);

-- EMI TABLE
CREATE TABLE emis (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  loan_id BIGINT NOT NULL,
  sequence_number INT NOT NULL,
  due_date DATE NOT NULL,
  principal_component DECIMAL(12,2) NOT NULL,
  interest_component DECIMAL(12,2) NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  outstanding_after_payment DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'DUE',
  paid_at TIMESTAMP NULL,
  CONSTRAINT fk_emi_loan FOREIGN KEY (loan_id) REFERENCES loan_applications(id)
);

CREATE INDEX idx_emi_loan ON emis(loan_id);
CREATE INDEX idx_emi_status ON emis(status);

-- SAMPLE DATA
INSERT INTO customers (name,email,phone,pan,aadhar,dob)
VALUES
('Aman Arya','aman@example.com','9876543210','ABCDE1234F','123412341234','1990-05-12'),
('Simran Kour','simran@example.com','9123456780','FGHIJ5678K','234523452345','1988-08-20'),
('Rahul Verma','rahul@example.com','9988776655','LMNOP9012Q','345634563456','1975-12-01');

INSERT INTO loan_applications (application_number,customer_id,amount,tenure_months,annual_interest_rate,purpose,status)
VALUES
('LA-202512001',1,250000.00,36,10.50,'Home Renovation','PENDING'),
('LA-202512002',1,60000.00,12,12.00,'Medical Expense','PENDING'),
('LA-202512003',2,500000.00,60,9.50,'Education','APPROVED'),
('LA-202512004',3,150000.00,24,11.00,'Car Repair','REJECTED'),
('LA-202512005',2,90000.00,18,12.00,'Wedding','PENDING');

-- (EMI rows will be generated on approval via application logic)
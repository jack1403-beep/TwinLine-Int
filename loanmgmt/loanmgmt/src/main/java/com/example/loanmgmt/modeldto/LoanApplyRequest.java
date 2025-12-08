package com.example.loanmgmt.modeldto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for loan application submission.
 */
@Getter
@Setter
public class LoanApplyRequest {
    @NotNull
    private Long customerId;

    @NotNull @DecimalMin("50000.00") @DecimalMax("1000000.00")
    private BigDecimal amount;

    @NotNull @Min(6) @Max(360)
    private Integer tenureMonths;

    @NotNull
    private BigDecimal annualInterestRate;

    @NotBlank
    private String purpose;

}


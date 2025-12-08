package com.example.loanmgmt.modeldto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRequest {

    @NotNull(message = "Payment amount is required")
    @Min(value = 1, message = "Payment amount must be greater than zero")
    private Double amount;

    @NotNull(message = "Payment date is required")
    private String paymentDate; 

    private String paymentMode; 
}

package com.example.loanmgmt.modeldto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO for creating a customer.
 */
@Getter
@Setter
public class CustomerDto {
    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN")
    private String pan;

    @Pattern(regexp = "\\d{12}", message = "Aadhar must be 12 digits")
    private String aadhar;

    @NotNull
    private LocalDate dob;
}

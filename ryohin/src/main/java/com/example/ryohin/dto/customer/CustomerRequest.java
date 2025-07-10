package com.example.ryohin.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank
    private String customerName;
    @Email
    @NotBlank
    private String email;
    @Size(min = 8)
    private String password;
    @NotBlank
    private String shippingAddress;
    @NotBlank
    private String phoneNumber;
}

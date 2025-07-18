package com.example.ryohin.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private String customerName;
    private String email;
    private String shippingAddress;
    private String phoneNumber;
}

package com.example.ryohin.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Orderrequest {
    @Valid
    @NotNull(message = "顧客情報は必須です")
    private CustomerInfo customerInfo;
}
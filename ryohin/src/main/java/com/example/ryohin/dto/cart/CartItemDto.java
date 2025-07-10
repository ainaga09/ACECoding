package com.example.ryohin.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartItemDto implements Serializable {

    private String productId;
    private String name;
    private BigDecimal price;
    private int quantity;
    // getters, setters, constructor
}

package com.example.ryohin.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartItemDto implements Serializable {

    private Integer productId;
    private String productName;
    private int price;
    private int quantity;
    private int subtotal;

}

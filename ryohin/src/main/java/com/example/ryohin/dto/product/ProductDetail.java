package com.example.ryohin.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    private Integer productId;
    private String productName;
    private String description;
    private String material;
    private int price;
    private String imageUrl;
    private Integer stockQuantity;
}
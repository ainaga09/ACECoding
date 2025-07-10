package com.example.ryohin.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    private Integer productId;
    private String productName;
    private String description;
    private String material;
    private BigDecimal price;
    private String imageUrl;
    private Integer stockQuantity;
}
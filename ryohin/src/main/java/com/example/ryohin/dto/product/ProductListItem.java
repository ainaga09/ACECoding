package com.example.ryohin.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListItem {
    private Integer productId;
    private String productName;
    private String imageUrl;
    private BigDecimal price;
}
package com.example.ryohin.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListItem {
    private Integer product_id;
    private String product_name;
    private String image_url;
    private BigDecimal price;
}
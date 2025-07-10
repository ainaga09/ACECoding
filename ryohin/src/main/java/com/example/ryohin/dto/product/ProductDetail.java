package com.example.ryohin.dto.product;

import jakarta.websocket.Decoder.Text;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    private Integer product_id;
    private String product_name;
    private String description;
    private String material;
    private BigDecimal price;
    private String image_url;
    private Integer stock_quantity;
}
package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItem;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order_id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product_id;
    
    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal item_price;
}
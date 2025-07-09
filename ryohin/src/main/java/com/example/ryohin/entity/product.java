package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
 

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor

public class product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_Id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItem = new ArrayList<>();

    @OneToMany(mappedBy = "orderitem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItem = new ArrayList<>();


    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private String material;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock_quantityInteger;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime created_At;

    @Column(nullable = false)
    private LocalDateTime updated_At;


    @PrePersist
    protected void onCreate() {
        created_At = LocalDateTime.now();
        updated_At = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updated_At = LocalDateTime.now();
    }






    
}

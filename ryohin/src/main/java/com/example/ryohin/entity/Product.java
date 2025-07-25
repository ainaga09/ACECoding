package com.example.ryohin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

 

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_pro_name", columnList = "name")
})
@Data
@NoArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

  

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItem = new ArrayList<>();


    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private String material;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @Min(0)
    private Integer stockQuantity;

    @Column(nullable = false)
    private String imageUrl;

   
    private LocalDateTime createdAt;

    
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    
}

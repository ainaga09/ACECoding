package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "CartItems")
@NoArgsConstructor

public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cart_item_id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private product product;

    @Column(nullable = false)
    private Integer quantity;

    
    

}
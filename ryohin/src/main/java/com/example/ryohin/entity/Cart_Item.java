package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "cart_items")
@NoArgsConstructor

public class Cart_Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cart_item_id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private product product;

    @Column(nullable = false)
    private Integer quantity;

    

}
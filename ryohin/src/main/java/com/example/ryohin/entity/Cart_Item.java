package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor

public class Cart_Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cart_item_id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private String cart_id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private String product_id;

    @Column(nullable = false)
    private Integer quantity;

    

}

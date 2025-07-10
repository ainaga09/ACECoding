package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

 

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cart_Id;

    @Column(nullable = false)
    private String session_id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

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

    public void addCartItem(CartItem cartItem){
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }



    
}



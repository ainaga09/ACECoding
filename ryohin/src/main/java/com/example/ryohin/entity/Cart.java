package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

 

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String cart_Id;

    @Column(nullable = false)
    private String session_id;

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

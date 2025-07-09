package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "Customers", indexes = {
    @Index(name = "idx_email", columnList = "email")
})
@Data
@NoArgsConstructor

public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customer_id;

    @Column(nullable = false)
    private String customer_name;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @Size(min=8)
    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String shipping_address;

    @Column(nullable = false)
    private String phone_number;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();

    // 1対多の関連付け
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
    }
   
}

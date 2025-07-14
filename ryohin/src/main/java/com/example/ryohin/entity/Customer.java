package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Customers", indexes = {
    @Index(name = "idx_email", columnList = "email")
})

@NoArgsConstructor

public class Customer {

    @Id
    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String customerName;



    @Size(min=8)
    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();

    }
    
    public void addOrder(Order order){
        orders.add(order);
        order.setCustomer(this);
    }

    public Customer orElseThrow(Object object) {
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
   
}

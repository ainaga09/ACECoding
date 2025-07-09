package com.example.ryohin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;


@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;
    
    @Column(nullable = false)
    private LocalDateTime order_date;
    
    @Column(nullable = false)
    private BigDecimal total_amount;
    
    @Column(nullable = false)
    private String guest_name;
    
    @Column(nullable = false)
    private String guest_email;
    
    @Column(nullable = false)
    private String shipping_fee;
    
    @Column(nullable = false)
    private String guest_phone_number;
    
    @Column(nullable = false)
    private String order_status;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
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
    // Helper method to add order item
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder_id(this);
    }
}
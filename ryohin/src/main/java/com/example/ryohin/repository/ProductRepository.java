package com.example.ryohin.repository;

import com.example.ryohin.entity.Product;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - ?2 WHERE p.productId = ?1 AND p.stockQuantity >= ?2")
    int decreaseStock(Integer productId, Integer stockQuantity);

    
}

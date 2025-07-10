package com.example.ryohin.repository;

import com.example.ryohin.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemrepository extends JpaRepository<CartItem, Integer> {

} 





















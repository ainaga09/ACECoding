package com.example.ryohin.repository;

import com.example.ryohin.entity.Cart_Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemrepository extends JpaRepository<Cart_Item, Integer> {

} 





















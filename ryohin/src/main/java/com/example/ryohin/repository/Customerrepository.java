package com.example.ryohin.repository;

import com.example.ryohin.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Customerrepository extends JpaRepository<Customer, Integer> {
}

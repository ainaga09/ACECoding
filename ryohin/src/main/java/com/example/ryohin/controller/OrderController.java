package com.example.ryohin.controller;

import com.example.ryohin.dto.cart.Cart;
import com.example.ryohin.dto.order.OrderRequest; 
import com.example.ryohin.dto.order.OrderResponse; 
import com.example.ryohin.service.CartService;
import com.example.ryohin.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
 private final OrderService orderService;
    private final CartService cartService;
    
    
    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            HttpSession session) {
        
        Cart cart = cartService.getCartFromSession(session);
        
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            OrderResponse orderResponse = orderService.placeOrder(cart, orderRequest, session);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }   
}

package com.example.ryohin.service;

import com.example.ryohin.dto.cart.Cart;
import com.example.ryohin.dto.cart.CartItem;
import com.example.ryohin.entity.Product;
import com.example.ryohin.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

     private static final String CART_SESSION_KEY = "cart";

     @Autowired
     private ProductRepository productRepository;

     public Cart getCart(HttpSession session) {
         Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
         if (cart == null) {
             cart = new Cart();
             session.setAttribute(CART_SESSION_KEY, cart);
         }
         return cart;
     }

     public void addItemToCart(HttpSession session, Integer productId, Integer quantity) {
         Cart cart = getCart(session);
         Optional<Product> product = productRepository.findById(productId);
         product.ifPresent(p -> {
             CartItem cartItem = new CartItem();
             cartItem.setProduct(p);
             cartItem.setQuantity(quantity);
             cart.getItems().add(cartItem);
         });
     }

     public void updateItemQuantity(HttpSession session, Integer productId, Integer quantity) {
         Cart cart = getCart(session);
         cart.getItems().stream()
                 .filter(item -> item.getProduct().getId().equals(productId))
                 .findFirst()
                 .ifPresent(item -> item.setQuantity(quantity));
     }

     public void removeItemFromCart(HttpSession session, Integer productId) {
         Cart cart = getCart(session);
         cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
     }

     public void clearCart(HttpSession session) {
         session.removeAttribute(CART_SESSION_KEY);
     }
}
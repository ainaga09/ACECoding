package com.example.ryohin.service;

import com.example.ryohin.dto.cart.Cart;
import com.example.ryohin.dto.cart.CartItemDto;
import com.example.ryohin.entity.Product;
import com.example.ryohin.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";
    
    private final ProductRepository productRepository;
    

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Cart getCartFromSession(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }
    
    public Cart addItemToCart(Integer productId, Integer quantity, HttpSession session) {
        Optional<Product> productOpt = productRepository.findById(productId);
        
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            Cart cart = getCartFromSession(session);
            
            CartItemDto item = new CartItemDto();
            item.setProductId(product.getProductId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);
            
            cart.addItem(item);
            session.setAttribute(CART_SESSION_KEY, cart);
            
            return cart;
        }
        
        return null;
    }
    
    public Cart updateItemQuantity(Integer itemId, Integer quantity, HttpSession session) {
        Cart cart = getCartFromSession(session);
        cart.updateQuantity(itemId, quantity);
        session.setAttribute(CART_SESSION_KEY, cart);
        return cart;
    }
    
    public Cart removeItemFromCart(Integer itemId, HttpSession session) {
        Cart cart = getCartFromSession(session);
        cart.removeItem(itemId);
        session.setAttribute(CART_SESSION_KEY, cart);
        return cart;
    }
    
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}
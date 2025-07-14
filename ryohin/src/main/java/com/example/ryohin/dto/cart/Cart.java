package com.example.ryohin.dto.cart;

import lombok.Data;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;



@Data
public class Cart implements Serializable {
    private Map<Integer, CartItemDto> items = new LinkedHashMap<>();
    private int totalQuantity;
    private int totalPrice;
    
    public void addItem(CartItemDto item) {
        Integer itemId = item.getProductId();
        

        if (items.containsKey(itemId)) {
            CartItemDto existingItem = items.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            existingItem.setSubtotal(existingItem.getPrice() * existingItem.getQuantity());
        } else {

            item.setProductId(itemId);
            item.setSubtotal(item.getPrice() * item.getQuantity());
            items.put(itemId, item);
        }
        

        calculateTotals();
    }
    
    public void updateQuantity(Integer itemId, int quantity) {
        if (items.containsKey(itemId)) {
            CartItemDto item = items.get(itemId);
            item.setQuantity(quantity);
            item.setSubtotal(item.getPrice() * quantity);
            calculateTotals();
        }
    }
    
    public void removeItem(Integer itemId) {
        items.remove(itemId);
        calculateTotals();
    }
    
    public void calculateTotals() {
        totalQuantity = 0;
        totalPrice = 0;
        
        for (CartItemDto item : items.values()) {
            totalQuantity += item.getQuantity();
            totalPrice += item.getSubtotal();
        }
    }
}
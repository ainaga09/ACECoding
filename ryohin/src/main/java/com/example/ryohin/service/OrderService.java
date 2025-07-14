package com.example.ryohin.service;

import com.example.ryohin.dto.cart.Cart;
import com.example.ryohin.dto.cart.CartItemDto;
import com.example.ryohin.dto.order.CustomerInfo;
import com.example.ryohin.dto.order.OrderRequest;
import com.example.ryohin.dto.order.OrderResponse;
import com.example.ryohin.entity.Order;
import com.example.ryohin.entity.OrderItem;
import com.example.ryohin.entity.Product;
import com.example.ryohin.repository.OrderItemRepository;
import com.example.ryohin.repository.OrderRepository;
import com.example.ryohin.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Transactional
    public OrderResponse placeOrder(Cart cart, OrderRequest orderRequest, HttpSession session) {
        if (cart == null || cart.getItems().isEmpty()) {
            return null;
        }

        // 注文エンティティ作成
        Order order = new Order();
        CustomerInfo customerInfo = orderRequest.getCustomerInfo();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setGuestName(customerInfo.getName());
        order.setGuestEmail(customerInfo.getEmail());
        order.setGuestShippingAddress(customerInfo.getAddress());
        order.setGuestPhoneNumber(customerInfo.getPhoneNumber());
        order.setOrderStatus("PENDING");

        // 注文明細作成と在庫減算
        for (CartItemDto cartItem : cart.getItems().values()) {
            try {
                Integer productId = Integer.valueOf(cartItem.getProductId());
                Product product = productRepository.findById(productId).orElseThrow(
                    () -> new IllegalStateException("在庫確認後に商品が見つかりません: " + cartItem.getProductName())
                );

                if (product.getStockQuantity() < cartItem.getQuantity()) {
                    throw new RuntimeException("在庫不足: " + cartItem.getProductName());
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setItemPrice(product.getPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                order.addOrderItem(orderItem);

                // 在庫を減らす
                int updatedRows = productRepository.decreaseStock(productId, cartItem.getQuantity());

                if (updatedRows != 1) {
                    throw new IllegalStateException(
                        "在庫の更新に失敗しました (更新行数: " + updatedRows + ")。" +
                        "商品ID: " + product.getProductId() +
                        ", 商品名: " + product.getName() +
                        ", 要求数量: " + cartItem.getQuantity()
                    );
                }

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("商品IDの形式が不正です: " + cartItem.getProductId());
            }
        }

        // 注文保存
        Order savedOrder = orderRepository.save(order);

        // カートクリア
        cartService.clearCart(session);

        return new OrderResponse(savedOrder.getOrderId(), savedOrder.getOrderDate());
    }
}

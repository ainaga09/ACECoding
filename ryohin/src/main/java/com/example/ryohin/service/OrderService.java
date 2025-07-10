package com.example.ryohin.service;

import com.example.ryohin.dto.cart.Cart;
import com.example.ryohin.dto.cart.CartItemDto;
import com.example.ryohin.dto.order.CustomerInfo;
import com.example.ryohin.dto.order.Orderrequest;
import com.example.ryohin.dto.order.orderResponse;
import com.example.ryohin.entity.Order;
import com.example.ryohin.entity.OrderItem;
import com.example.ryohin.entity.Product;
import com.example.ryohin.entity.CartItem;
import com.example.ryohin.repository.OrderItemRepository;
import com.example.ryohin.repository.OrderRepository;
import com.example.ryohin.repository.ProductRepository;
import com.example.ryohin.repository.CartItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemrepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemrepository,
            ProductRepository productRepository,
            CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemrepository = orderItemrepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Transactional
    public orderResponse placeOrder(Cart cart, Orderrequest orderRequest, HttpSession session) {
        if (cart == null || cart.getItems().isEmpty()) {
            return null;
        }

        // 在庫確認
        for (CartItem cartItem : cart.getItems().values()) {
            Optional<Product> productOpt = productRepository.findById(cartItem.getProductId());
            if (productOpt.isEmpty() || productOpt.get().getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("在庫不足または商品未存在: " + cartItem.getName());
            }
        }

        // 注文エンティティ作成
        Order order = new Order();
        CustomerInfo customerInfo = orderRequest.getCustomerInfo();
        order.setOrder_date(LocalDateTime.now());
        order.setTotal_amount(BigDecimal.valueOf(cart.getTotalPrice()));
        order.setGuest_name(customerInfo.getName());
        order.setGuest_email(customerInfo.getEmail());
        order.setGuest_shipping_address(customerInfo.getAddress());
        order.setGuest_phone_number(customerInfo.getPhoneNumber());
        order.setOrder_status("PENDING");

        // 注文明細作成と在庫減算
        for (CartItem cartItem : cart.getItems().values()) {
            Product product = productRepository.findById(cartItem.getProduct_Id()).orElseThrow(
                () -> new IllegalStateException("在庫確認後に商品が見つかりません: " + cartItem.getName())
            );

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setItem_price(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            order.addOrderItem(orderItem);

            // 在庫減算処理と結果のチェック
            int updatedRows = productRepository.decreaseStock(product.getProduct_Id(), cartItem.getQuantity());

            // 更新された行数が1でない場合（在庫更新に失敗した場合）
            if (updatedRows != 1) {
                throw new IllegalStateException(
                    "在庫の更新に失敗しました (更新行数: " + updatedRows + ")。" +
                    "商品ID: " + product.getProduct_Id() +
                    ", 商品名: " + product.getName() +
                    ", 要求数量: " + cartItem.getQuantity()
                    // 必要であれば、考えられる原因（競合など）を示すメッセージを追加
                );
            }
        }

        // 注文保存
        Order savedOrder = orderRepository.save(order);

        // カートクリア
        cartService.clearCart(session);

        return new orderResponse(savedOrder.getOrder_id(), savedOrder.getOrder_date());
    }
}
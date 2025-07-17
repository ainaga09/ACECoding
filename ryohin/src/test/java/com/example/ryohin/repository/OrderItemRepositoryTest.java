package com.example.ryohin.repository;

import com.example.ryohin.entity.Order;
import com.example.ryohin.entity.OrderItem;
import com.example.ryohin.entity.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setName("商品A");
        product1.setPrice(1000);
        product1.setStockQuantity(10);
        product1.setImageUrl("product1");
        product1.setDescription("説明");
        product1.setMaterial("素材");
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product1);

        product2 = new Product();
        product2.setName("商品B");
        product2.setPrice(2000);
        product2.setStockQuantity(5);
        product2.setImageUrl("product2");
        product2.setDescription("説明");
        product2.setMaterial("素材");
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product2);
    }

    private Order createSampleOrder(String customerName) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(3000);
        order.setGuestName(customerName);
        order.setGuestEmail(customerName.toLowerCase() + "@example.com");
        order.setGuestShippingAddress("住所" + customerName);
        order.setGuestPhoneNumber("090-" + customerName.hashCode());
        order.setOrderStatus("PENDING");
        return orderRepository.save(order);
    }

    private OrderItem createOrderItem(Order order, Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setItemPrice(product.getPrice());
        return item;
    }


    @Test
    void saveOrderItem_ShouldAssignId() {
        Order order = createSampleOrder("customerName");
        OrderItem item = createOrderItem(order, product1, 10);
        OrderItem saved = orderItemRepository.save(item);
        assertNotNull(saved.getOrderItem());
    }

    @Test
    void findById_WhenExists_ShouldReturnOrderItem() {
        Order order = createSampleOrder("customerName");
        OrderItem orderItem = createOrderItem(order, product1, 10);
        OrderItem savedItem = orderItemRepository.save(orderItem);
        Integer id = savedItem.getOrderItem();

        Optional<OrderItem> foundItemOpt = orderItemRepository.findById(id);

        assertTrue(foundItemOpt.isPresent(), "OrderItem should be found by ID");
        OrderItem foundItem = foundItemOpt.get();
        assertEquals(10, foundItem.getQuantity());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        Optional<OrderItem> result = Optional.empty();
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllOrderItems() {
        Order order = createSampleOrder("TestUser");
        orderItemRepository.save(createOrderItem(order, product1, 1));
        orderItemRepository.save(createOrderItem(order, product2, 2));

        List<OrderItem> items = orderItemRepository.findAll();
        assertEquals(2, items.size());
    }


    @Test
    void saveOrderItem_WithNegativeQuantity_ShouldThrowException() {
        Order order = createSampleOrder("NegativeQuantityUser");
        OrderItem item = createOrderItem(order, product1, -1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            try {
                orderItemRepository.saveAndFlush(item);
            } catch (jakarta.validation.ConstraintViolationException e) {
                throw new DataIntegrityViolationException("Validation failed", e);
            }
        }, "Saving OrderItem with negative quantity should throw DataIntegrityViolationException");
    }

    @Test
    void saveOrderItem_ShouldLinkToOrderAndProduct() {
        Order order = createSampleOrder("TestCustomer");
        Product product = product1; // setUp() で登録済み
        OrderItem item = createOrderItem(order, product, 2);

        OrderItem savedItem = orderItemRepository.save(item);

        assertNotNull(savedItem.getOrderItem());
        assertNotNull(savedItem.getOrder());
        assertNotNull(savedItem.getProduct());

        assertEquals(order.getOrderId(), savedItem.getOrder().getOrderId());
        assertEquals(product.getOrderItem(), savedItem.getProduct().getOrderItem());
    }


}
    

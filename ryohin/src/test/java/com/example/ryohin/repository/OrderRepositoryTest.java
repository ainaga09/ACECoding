package com.example.ryohin.repository;

import com.example.ryohin.entity.Order;
import com.example.ryohin.entity.OrderItem;
import com.example.ryohin.entity.Product;
import jakarta.persistence.PersistenceException; // 制約違反用
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException; // Spring Data JPAの例外

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest // JPA関連のテストに特化した設定（インメモリDB使用、関連Beanのみロード）
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // テストデータ準備や永続化の検証に使用

    @Autowired
    private OrderRepository orderRepository; // テスト対象のリポジトリ

    @Autowired // OrderDetailの削除確認用にインジェクト
    private OrderItemRepository orderItemRepository;

    @Autowired // テストデータ準備用にインジェクト
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

        // 各テストメソッド実行前に共通の商品データを準備
    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setName("商品A");
        product1.setPrice(1000);
        product1.setStockQuantity(10);
        product1.setImageUrl("product1");
        entityManager.persist(product1); // TestEntityManagerで永続化

        product2 = new Product();
        product2.setName("商品B");
        product2.setPrice(2000);
        product2.setStockQuantity(5);
        product2.setImageUrl("product1");
        entityManager.persist(product2);

        entityManager.flush(); // DBに即時反映させ、IDなどを確定させる
    }

    // テスト用のOrderオブジェクトを作成するヘルパーメソッド
    private Order createSampleOrder(String customerName) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(3000); // (1000*1 + 2000*1)
        order.setGuestName(customerName);
        order.setGuestEmail(customerName.toLowerCase() + "@example.com");
        order.setGuestShippingAddress("住所 " + customerName);
        order.setGuestPhoneNumber("090-" + customerName.hashCode());
        order.setOrderStatus("PENDING"); // 初期ステータス

        OrderItem item = new OrderItem();
        item.setProduct(product1); // 事前に永続化したProductエンティティを設定
        item.setItemPrice(product1.getPrice());
        item.setQuantity(1);
        order.addOrderItem(item); // Orderエンティティのヘルパーメソッドで詳細を追加

        OrderItem item2 = new OrderItem();
        item2.setProduct(product2);
        item2.setItemPrice(product2.getPrice());
        item2.setQuantity(1);
        order.addOrderItem(item2);
        return order;
    }

    @Test
    @DisplayName("注文と注文アイテムを正常に保存できる")
    void saveOrderWithItems_Success() {
        // Arrange
        Order order = createSampleOrder("顧客1");

        // Act
        Order savedOrder = orderRepository.save(order); // Orderを保存 (CascadeType.ALLによりOrderDetailも保存されるはず)
        entityManager.flush(); // DBへ反映
        entityManager.clear(); // 永続化コンテキストキャッシュをクリアし、DBからの取得を確実にする
    }
    
}


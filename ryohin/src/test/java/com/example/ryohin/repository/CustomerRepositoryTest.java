package com.example.ryohin.repository;
 
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
import com.example.ryohin.entity.Customer;
import com.example.ryohin.entity.Product;
 
import jakarta.persistence.PersistenceException; // JPA標準の例外
import jakarta.validation.ConstraintViolation;
 
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException; // Spring Data JPAの例外
import org.springframework.orm.jpa.JpaSystemException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.id.IdentifierGenerationException;
 
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
 
@DataJpaTest
public class CustomerRepositoryTest {
 
    @Autowired
    private TestEntityManager entityManager; // テストデータの準備や永続化の検証に使用
 
    @Autowired
    private CustomerRepository customerRepository; // テスト対象
 
    private Customer customer1;
    private Customer customer2;
 
    private Customer createCustomer(String customername, String email, String passwordHash, String shippingAddress, String phoneNumber) {
        Customer customer = new Customer();
        customer.setCustomerName(customername);
        customer.setEmail(email);
        customer.setPasswordHash(passwordHash);
        customer.setShippingAddress(shippingAddress);
        customer.setPhoneNumber(phoneNumber);
        return customer;
    }
 
    @BeforeEach
    void setUp() {
        // 各テストメソッド実行前に共通のデータを準備
        customer1 = createCustomer("顧客A", "customerA@example.com", "passwordHashA", "住所A", "000-0000-0000");
        customer2 = createCustomer("顧客B", "customerB@example.com", "passwordHashB", "住所B", "111-1111-1111");
        entityManager.persist(customer1); // TestEntityManagerで永続化
        entityManager.persist(customer2);
        entityManager.flush(); // DBに即時反映
        entityManager.clear(); // 永続化コンテキストキャッシュをクリアし、後続のテストがDBから取得するようにする
    }
 
    @Test
    @DisplayName("顧客を正常に保存,createdAtの確認も")
    void saveCutomer_created_Success() {
    // Arrange
    Customer newCustomer = createCustomer("顧客C", "customerC@example.com", "passwordHashC", "住所C", "222-2222-2222");
 
    // 現在時刻の直前を取得（比較用）
    Instant beforeSave = Instant.now();
 
    // Act
    Customer savedCustomer = customerRepository.save(newCustomer);
    entityManager.flush();
    entityManager.clear();
 
    // DBから再取得（確認のため）
    Optional<Customer> optionalCustomer = customerRepository.findById(savedCustomer.getEmail());
 
    // Assert
    assertThat(optionalCustomer).isPresent();
 
    Customer retrievedCustomer = optionalCustomer.get();
 
    assertThat(retrievedCustomer).isNotNull();
    assertThat(retrievedCustomer.getCustomerName()).isNotNull();
    assertThat(retrievedCustomer.getEmail()).isNotNull();
    assertThat(retrievedCustomer.getPasswordHash()).isNotNull();
    assertThat(retrievedCustomer.getShippingAddress()).isNotNull();
    assertThat(retrievedCustomer.getPhoneNumber()).isNotNull();
    assertThat(retrievedCustomer.getCreatedAt()).isNotNull();
 
   
   ZoneOffset offset = OffsetDateTime.now().getOffset();
   Instant createdAt = retrievedCustomer.getCreatedAt().toInstant(offset);
   Instant afterSave = Instant.now();
 
    assertThat(createdAt).isAfterOrEqualTo(beforeSave);
    assertThat(createdAt).isBeforeOrEqualTo(afterSave);
 
    }
 
 
    @Test
    @DisplayName("必須項目(customername)がnullで保存しようとすると例外発生")
    void saveCustomer_WithNullcustomerName_ShouldThrowException() {
        // Arrange
       
        Customer customer = new Customer();
        customer.setCustomerName(null);
        customer.setEmail("ddd@dddd");
        customer.setPasswordHash("passwordHash1");
        customer.setShippingAddress("shippingAddress1");
        customer.setPhoneNumber("phoneNumber1");
   
        // Act & Assert
        assertThatThrownBy(() -> {
            customerRepository.save(customer);
            entityManager.flush(); // DBへの反映時に制約違反が発生
        })
        .isInstanceOf(ConstraintViolationException.class) // Spring Data JPAがラップした例外
        .hasCauseInstanceOf(JdbcSQLIntegrityConstraintViolationException.class); // JPAレイヤの例外
 
    }
 
    @Test
    @DisplayName("必須項目(email)がnullで保存しようとすると例外発生")
    void saveCustomer_WithNullemail_ShouldThrowException() {
        // Arrange
       
        Customer customer = new Customer();
        customer.setCustomerName("customerName2");
        customer.setEmail(null);
        customer.setPasswordHash("passwordHash2");
        customer.setShippingAddress("shippingAddress2");
        customer.setPhoneNumber("phoneNumber2");
 
 
       // Act & Assert
        assertThatThrownBy(() -> {
            customerRepository.save(customer);
            entityManager.flush(); // DBへの反映時に制約違反が発生
        })
 
          .isInstanceOf(JpaSystemException.class) // Spring Data JPAがラップした例外
        .hasCauseInstanceOf(IdentifierGenerationException.class); // JPAレイヤの例外
 
    }
 
    @Test
    @DisplayName("必須項目(passward)がnullで保存しようとすると例外発生")
    void saveCustomer_WithNullpasswordhash_ShouldThrowException() {
        // Arrange
       
        Customer customer = new Customer();
        customer.setCustomerName("customerName2");
        customer.setEmail("dddd@dddd");
        customer.setPasswordHash(null);
        customer.setShippingAddress("shippingAddress2");
        customer.setPhoneNumber("phoneNumber2");
   
        // Act & Assert
        assertThatThrownBy(() -> {
            customerRepository.save(customer);
            entityManager.flush(); // DBへの反映時に制約違反が発生
        })
        .isInstanceOf(ConstraintViolationException.class) // Spring Data JPAがラップした例外
        .hasCauseInstanceOf(JdbcSQLIntegrityConstraintViolationException.class); // JPAレイヤの例外
    }
 
 
@Test
    @DisplayName("必須項目(shippingAddress)がnullで保存しようとすると例外発生")
    void saveCustomer_WithNullShippingAddress_ShouldThrowException() {
        // Arrange
       
        Customer customer = new Customer();
        customer.setCustomerName("customerName2");
        customer.setEmail("dddd@dddd");
        customer.setPasswordHash("passwordHash2");
        customer.setShippingAddress(null);
        customer.setPhoneNumber("phoneNumber2");
   
        // Act & Assert
        assertThatThrownBy(() -> {
            customerRepository.save(customer);
            entityManager.flush(); // DBへの反映時に制約違反が発生
        })
        .isInstanceOf(ConstraintViolationException.class) // Spring Data JPAがラップした例外
        .hasCauseInstanceOf(JdbcSQLIntegrityConstraintViolationException.class); // JPAレイヤの例外
    }
 
 
    @Test
    @DisplayName("必須項目(phoneNumber)がnullで保存しようとすると例外発生")
    void saveCustomer_WithNullPhoneNumber_ShouldThrowException() {
        // Arrange
       
        Customer customer = new Customer();
        customer.setCustomerName("customerName2");
        customer.setEmail("dddd@dddd");
        customer.setPasswordHash("passwordHash2");
        customer.setShippingAddress("shippingAddress2");
        customer.setPhoneNumber(null);
   
        // Act & Assert
        assertThatThrownBy(() -> {
            customerRepository.save(customer);
            entityManager.flush(); // DBへの反映時に制約違反が発生
        })
        .isInstanceOf(ConstraintViolationException.class) // Spring Data JPAがラップした例外
        .hasCauseInstanceOf(JdbcSQLIntegrityConstraintViolationException.class); // JPAレイヤの例外
    }
 
 
}
 
 
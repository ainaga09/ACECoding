package com.example.ryohin.service;

import com.example.ryohin.dto.customer.CustomerRequest;
import com.example.ryohin.entity.Customer;
import com.example.ryohin.repository.CustomerRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("saveCustomer: 正常なリクエストでCustomerが保存される")
    void saveCustomer_Success_ShouldSaveCustomer() {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setCustomerName("山田太郎");
        request.setEmail("yamada@example.com");
        request.setPassword("pass123");
        request.setPhoneNumber("09012345678");
        request.setShippingAddress("東京都");

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerName(request.getCustomerName());
        savedCustomer.setEmail(request.getEmail());
        savedCustomer.setPasswordHash(request.getPassword());
        savedCustomer.setPhoneNumber(request.getPhoneNumber());
        savedCustomer.setShippingAddress(request.getShippingAddress());
        savedCustomer.setCreatedAt(LocalDateTime.now());

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        Customer result = customerService.saveCustomer(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo(request.getCustomerName());
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("saveCustomer: createdAt が現在時刻に近い値に設定される")
    void saveCustomer_Success_ShouldSetCreatedAtCorrectly() {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setCustomerName("山田太郎");
        request.setEmail("yamada@example.com");
        request.setPassword("pass123");
        request.setPhoneNumber("09012345678");
        request.setShippingAddress("東京都");

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Customer result = customerService.saveCustomer(request);

        // Assert
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(result.getCreatedAt()).isAfter(LocalDateTime.now().minusSeconds(5));
    }

    @Test
    @DisplayName("saveCustomer: null を渡すと NullPointerException がスローされる")
    void saveCustomer_Fail_WhenRequestIsNull_ShouldThrowNPE() {
        // Arrange
        CustomerRequest request = null;

        // Act & Assert
        assertThatThrownBy(() -> customerService.saveCustomer(request))
            .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(customerRepository);
    }

    @Test
    @DisplayName("saveCustomer: 不完全なCustomerRequestではerrorがスローされる")
    void saveCustomer_Fail_WhenFieldsMissing_ShouldThrowException() {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setCustomerName(null); // 必須項目の欠如

        // Act & Assert
        assertThatThrownBy(() -> customerService.saveCustomer(request))
            .isInstanceOf(NullPointerException.class); // または IllegalArgumentException

        verifyNoInteractions(customerRepository);
    }

    @Test
    @DisplayName("saveCustomer: emailがすでに存在する場合はerrorがスローされる")
    void saveCustomer_Fail_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        CustomerRequest request = new CustomerRequest();
        request.setCustomerName("田中花子");
        request.setEmail("existing@example.com");
        request.setPassword("pass456");
        request.setPhoneNumber("08098765432");
        request.setShippingAddress("大阪府");

        when(customerRepository.save(any(Customer.class)))
            .thenThrow(new RuntimeException("email 重複"));

        // Act & Assert
        assertThatThrownBy(() -> customerService.saveCustomer(request))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("email 重複");

        verify(customerRepository).save(any(Customer.class));
    }
}

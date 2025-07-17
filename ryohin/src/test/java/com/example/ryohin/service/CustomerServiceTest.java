package com.example.ryohin.service;


import com.example.ryohin.dto.customer.CustomerRequest;
import com.example.ryohin.entity.Customer;
import com.example.ryohin.entity.Product;
import com.example.ryohin.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private CustomerRequest validRequest;

    @BeforeEach
    void setup() {
        validRequest = new CustomerRequest();
        validRequest.setCustomerName("山田次郎");
        validRequest.setEmail("yamada@example.com");
        validRequest.setPassword("Password123");
        validRequest.setShippingAddress("東京都新宿区1-1-1");
        validRequest.setPhoneNumber("090-1234-5678");
    }

    @Test
    @DisplayName("saveCustomer: 正常な入力でCustomerが保存される")
    void saveCustomer_Success_ShouldSaveCustomer() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer saved = invocation.getArgument(0);
            saved.setCustomerId(1);
            return saved;
        });

        // Act
        Customer result = customerService.saveCustomer(validRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo(validRequest.getCustomerName());
        assertThat(result.getEmail()).isEqualTo(validRequest.getEmail());
        assertThat(result.getShippingAddress()).isEqualTo(validRequest.getShippingAddress());
        assertThat(result.getPhoneNumber()).isEqualTo(validRequest.getPhoneNumber());
        assertThat(result.getPasswordHash()).isEqualTo(validRequest.getPassword());
        assertThat(result.getCreatedAt()).isNotNull();
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("saveCustomer: 入力がnullの場合はNullPointerExceptionが発生")
    void saveCustomer_Fail_WhenRequestIsNull_ShouldThrowNPE() {
        // Act & Assert
        assertThatThrownBy(() -> customerService.saveCustomer(null))
            .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(customerRepository);
    }

    @Test
    @DisplayName("saveCustomer: メールアドレスが既に存在する場合に例外をスロー")
    void saveCustomer_Fail_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        doThrow(new RuntimeException("Email already exists"))
            .when(customerRepository).save(any(Customer.class));

        // Act & Assert
        assertThatThrownBy(() -> customerService.saveCustomer(validRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Email already exists");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("saveCustomer: CustomerRequestの一部がnullでも保存される（Entity側の制約による）")
    void saveCustomer_Fail_WhenFieldsMissing_ShouldAllowNullUnlessValidated() {
        // Arrange
        validRequest.setEmail(null); // nullでも例外が出ない仕様ならこのままで
        when(customerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Customer result = customerService.saveCustomer(validRequest);

        // Assert
        assertThat(result.getEmail()).isNull(); // バリデーションがなければ通る
        verify(customerRepository).save(any(Customer.class));
    }

}


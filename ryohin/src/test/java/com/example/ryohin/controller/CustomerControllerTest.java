package com.example.ryohin.controller;

//DTO
import com.example.ryohin.dto.customer.CustomerRequest;

//Service
import com.example.ryohin.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

//junit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

//springframework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc; // HTTPリクエストをシミュレート

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean// Service層のモック
    private CustomerService customerService;

    private CustomerRequest validCustomerRequest; 


    @BeforeEach
    void setUp() {
        //会員登録リクエスト準備
        validCustomerRequest = new CustomerRequest();
        validCustomerRequest.setCustomerName("Test User");
        validCustomerRequest.setEmail("test@example.com");
        validCustomerRequest.setPassword("Test Password");
        validCustomerRequest.setShippingAddress("Test ShppingAddress");
        validCustomerRequest.setPhoneNumber("0123456789");

        // --- Serviceメソッドのデフォルトモック設定 (lenient) ---
       lenient().when(customerService.saveCustomer(validCustomerRequest)).thenReturn(null);

    }

/*@BeforeEach
    void setUp() {
        mockSession = new MockHttpSession();

        // ---リクエスト準備---
        validCustomerRequest = new CustomerRequest();
        validCustomerRequest.setCustomerName("Test User");
        validCustomerRequest.setEmail("test@example.com");
        validCustomerRequest.setPassword("00000000");
        validCustomerRequest.setShppingAddress("Test Address");
        validCustomerRequest.setPhoneNumber("0123456789");
        

        // --- Serviceメソッドのデフォルトモック設定 (lenient) ---
       lenient().when(customerService.saveCustomer(validCustomerRequest)).thenReturn(null);
    }*/
// === 正常系テスト ===
    @Nested
    @DisplayName("正常系: POST /api/customers/saveCustomer")
    class SaveCustomerSuccessTest{
    void saveCustomer_WithValidRequest_ShouldReturns201() throws Exception {
        mockMvc.perform(post("/api/customers/saveCustomer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCustomerRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated()) // 201 Created
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
                    
                

    }

    //===異常系テスト：バリデーションエラー===
    @Nested
    @DisplayName("異常系テスト:バリデーションエラー")
    class SaveCustomerValidationErrorTests{

         // ヘルパーメソッド：バリデーションテストを実行し、結果を検証する
        private void performValidationTest(CustomerRequest request, String expectedField, String expectedMessage) throws Exception {
            mockMvc.perform(post("/api/customers/saveCustomer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest()) // 400 Bad Request を期待
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Content-Type が JSON であることを期待
                            // JSONレスポンスのルートレベルにあるキー (expectedFieldの値そのもの) の値を検証する
                            .andExpect(jsonPath("$['" + expectedField + "']", is(expectedMessage)));
                            
            verifyNoInteractions(customerService); // バリデーションエラーなのでサービスは呼ばれない
        }

        @Test
        @DisplayName("CustomerRequest.customerName が空文字列の場合、400 Bad Requestとエラーメッセージを返す ")
        void SaveCustomer_WithBlankCustomerName_ShouldReturnBadRequest() throws Exception {
            CustomerRequest invalidCustomerRequest = new CustomerRequest(); 
            invalidCustomerRequest.setCustomerName("");//@NotBlank違反
            invalidCustomerRequest.setEmail("test@example.com"); 
            invalidCustomerRequest.setPassword("00000000");
            invalidCustomerRequest.setShippingAddress("ShppingAddress");
            invalidCustomerRequest.setPhoneNumber("0123456789");

            performValidationTest(invalidCustomerRequest, "customerName", "お名前は必須です");


        }


        @Test
        @DisplayName("CustomerRequest.email が空文字列の場合、400 Bad Requestとエラーメッセージを返す ")
        void SaveCustomer_WithBlankEmail_ShouldReturnBadRequest() throws Exception{
            CustomerRequest invalidCustomerRequest = new CustomerRequest(); 
            invalidCustomerRequest.setCustomerName("Name");
            invalidCustomerRequest.setEmail(""); //@NotBlank違反
            invalidCustomerRequest.setPassword("00000000");
            invalidCustomerRequest.setShippingAddress("ShppingAddress");
            invalidCustomerRequest.setPhoneNumber("0123456789");

             performValidationTest(invalidCustomerRequest, "email", "メールアドレスは必須です");


        }


        @Test
        @DisplayName("CustomerRequest.email がemail形式でない場合、400 Bad Requestとエラーメッセージを返す ")
        void SaveCustomer_WithInvalidEmail_ShouldReturnBadRequest() throws Exception{
            CustomerRequest invalidCustomerRequest = new CustomerRequest(); 
            invalidCustomerRequest.setCustomerName("Name");
            invalidCustomerRequest.setEmail("invalid-email"); //@Email違反
            invalidCustomerRequest.setPassword("00000000");
            invalidCustomerRequest.setShippingAddress("ShppingAddress");
            invalidCustomerRequest.setPhoneNumber("0123456789");

            performValidationTest(invalidCustomerRequest, "email", "有効なメールアドレスを入力してください");

        }


        @Test
        @DisplayName("CustomerRequest.password が空文字列の場合、400 Bad Requestとエラーメッセージを返す ")
        void SaveCustomer_WithBlankPassword_ShouldReturnBadRequest() throws Exception{
            CustomerRequest invalidCustomerRequest = new CustomerRequest(); 
            invalidCustomerRequest.setCustomerName("Name");
            invalidCustomerRequest.setEmail("test@example.com"); 
            invalidCustomerRequest.setPassword(null);//@NotBlank違反
            invalidCustomerRequest.setShippingAddress("ShppingAddress");
            invalidCustomerRequest.setPhoneNumber("0123456789");

            performValidationTest(invalidCustomerRequest, "password", "パスワードは必須です");   
        }


        @Test
        @DisplayName("CustomerRequest.password が8文字未満の場合、400 Bad Requestとエラーメッセージを返す ")
        void SaveCustomer_WithSizePassword_ShouldReturnBadRequest() throws Exception{
            CustomerRequest invalidCustomerRequest = new CustomerRequest(); 
            invalidCustomerRequest.setCustomerName("Name");
            invalidCustomerRequest.setEmail("test@example.com"); 
            invalidCustomerRequest.setPassword("0000000");//@Size違反
            invalidCustomerRequest.setShippingAddress("ShppingAddress");
            invalidCustomerRequest.setPhoneNumber("0123456789");

            performValidationTest(invalidCustomerRequest, "password", "パスワードは8文字以上で設定してください");   
        }


        @Test
        @DisplayName("CustomerRequest.shippingAddress が空文字列の場合、400 Bad Requestとエラーメッセージを返す ")
        void SaveCustomer_WithBlankShippingAddress_ShouldReturnBadRequest() throws Exception{
            CustomerRequest invalidCustomerRequest = new CustomerRequest(); 
            invalidCustomerRequest.setCustomerName("Name");
            invalidCustomerRequest.setEmail("test@example.com"); 
            invalidCustomerRequest.setPassword("00000000");
            invalidCustomerRequest.setShippingAddress("");//@NotBlank違反
            invalidCustomerRequest.setPhoneNumber("0123456789");

            performValidationTest(invalidCustomerRequest, "shippingAddress", "住所は必須です");

        }


        @Test
        @DisplayName("CustomerRequest.phoneNumber が空文字列の場合、400 Bad Requestとエラーメッセージを返す ")
        void SaveCustomer_WithBlankPhoneNumber_ShouldReturnBadRequest() throws Exception{
            CustomerRequest invalidCustomerRequest = new CustomerRequest(); 
            invalidCustomerRequest.setCustomerName("Name");
            invalidCustomerRequest.setEmail("test@example.com"); 
            invalidCustomerRequest.setPassword("00000000");
            invalidCustomerRequest.setShippingAddress("ShppingAddress");
            invalidCustomerRequest.setPhoneNumber("");//@NotBlank違反

            performValidationTest(invalidCustomerRequest, "phoneNumber", "電話番号は必須です");
            
        }

    }



    

}

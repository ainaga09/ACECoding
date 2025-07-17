package com.example.ryohin.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank(message = "お名前は必須です")
    private String customerName;
    @Email(message = "有効なメールアドレスを入力してください")
    @NotBlank(message = "メールアドレスは必須です")
    private String email;
    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, message = "パスワードは8文字以上で設定してください")
    private String password;
    @NotBlank(message = "住所は必須です")
    private String shippingAddress;
    @NotBlank(message = "電話番号は必須です")
    private String phoneNumber;
}

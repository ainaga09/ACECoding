package com.example.ryohin.service;

public class LoginService {package com.example.ryohin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ryohin.entity.Customer;
import com.example.ryohin.repository.CustomerRepository;
import com.example.ryohin.dto.CustomerResponse;

@Service
public class LoginService {

    private final CustomerRepository customerRepository;

    @Autowired
    public LoginService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse login(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("メールアドレスまたはパスワードが間違っています"));

        if (!customer.getPassword().equals(password)) {
            throw new RuntimeException("メールアドレスまたはパスワードが間違っています");
        }

        return new CustomerResponse(
            customer.getName(),
            customer.getAddress(),
            customer.getPhoneNumber()
        );
    }

}


    
}

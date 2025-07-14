package com.example.ryohin.service;

import com.example.ryohin.entity.Customer;
import com.example.ryohin.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> searchCustomerByEmail(String email) {
        return customerRepository.findById(email);
    }
}

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

    public Optional<Customer> authenticate(String email, String password) {
        Optional<Customer> customerOpt = customerRepository.findById(email);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getPasswordHash().equals(password)) {
                return Optional.of(customer); 
            }
        }
        return Optional.empty(); 
    }

}


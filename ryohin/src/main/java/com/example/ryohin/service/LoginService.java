package com.example.ryohin.service;


import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.ryohin.entity.Customer;
import com.example.ryohin.repository.CustomerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
	
	private final CustomerRepository customerRepository;
	
	public Optional<Customer> searchCustomerByEail(String email){
		return customerRepository.findById(email);
	}

}
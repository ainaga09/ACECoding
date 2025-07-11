package com.example.ryohin.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.ryohin.entity.Customer;
import com.example.ryohin.repository.CustomerRepository;
import com.example.ryohin.dto.customer.CustomerResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
	
	private final CustomerRepository customerRepository;
	
	public Optional<Customer> searchCustomerByEail(String email){
		return customerRepository.findByEmail(email);
	}

}
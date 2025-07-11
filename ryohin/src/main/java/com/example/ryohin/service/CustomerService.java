package com.example.ryohin.service;
 
import com.example.ryohin.repository.CustomerRepository;
import com.example.ryohin.dto.customer.CustomerRequest;

import com.example.ryohin.entity.Customer;
 

import org.springframework.transaction.annotation.Transactional;
 
 
import java.time.LocalDateTime;

public class CustomerService {
 
    private CustomerRepository customerRepository;
    private CustomerRequest customerRequest;
 
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.customerRequest = new CustomerRequest();
    }
       
 
 
@Transactional
    public Customer saveCustomer(Customer customer) {
        

        customer.setCustomerName(customerRequest.getCustomerName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPasswordHash(customerRequest.getPassword());
        customer.setShippingAddress(customerRequest.getShippingAddress());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setCreatedAt(LocalDateTime.now());        

         Customer savedCustomer = customerRepository.save(customer);

         return savedCustomer;
    }

 

    
}
 
package com.example.ryohin.service;
 
import com.example.ryohin.repository.CustomerRepository;
import com.example.ryohin.dto.customer.CustomerRequest;

import com.example.ryohin.entity.Customer;
 import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
 
 
import java.time.LocalDateTime;

@Service
public class CustomerService {
 
    private CustomerRepository customerRepository;
    
 
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        //this.customerRequest = new CustomerRequest();
    }
       
 
 
@Transactional
    public Customer saveCustomer(CustomerRequest customerRequest) {

        Customer customer = new Customer();
        customer.setCustomerName(customerRequest.getCustomerName());
        customer.setEmail(customerRequest.getEmail());

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
 
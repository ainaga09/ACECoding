package com.example.ryohin.controller;

import com.example.ryohin.dto.customer.CustomerRequest;
import com.example.ryohin.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /*@GetMapping("/info")
    public ResponseEntity<CustomerResponse> getCustomerInfo() {
        CustomerResponse customer = customerService.getCustomer();
        return ResponseEntity.ok(customer);
    }*/


    @PostMapping("/saveCustomer")
    public ResponseEntity<Void> saveCustomer(
        @Valid @RequestBody CustomerRequest customerRequest ) {

    customerService.saveCustomer(customerRequest);
    return new ResponseEntity<>(HttpStatus.CREATED);
}



    
}

package com.example.ryohin.controller;

import com.example.ryohin.dto.customer.CustomerRequest;
import com.example.ryohin.dto.customer.Customerresponse;
import com.example.ryohin.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/info")
    public ResponseEntity<Customerresponse> getCustomerInfo(HttpSession session) {
        Customerresponse customer = customerService.getMockCustomer();
        return ResponseEntity.ok(customer);
    }


    @PostMapping("/register")
    public ResponseEntity<Customerresponse> registerCustomer(
        @Valid @RequestBody CustomerRequest customerRequest,
        HttpSession session) {

    Customerresponse response = customerService.registerCustomer(customerRequest);
    return ResponseEntity.ok(response);
}



    @PutMapping("/update")
    public ResponseEntity<Customerresponse> updateCustomer(
            @Valid @RequestBody CustomerRequest customerRequest,
            HttpSession session) {

        Customerresponse updated = (Customerresponse) customerService.updateCustomerInfo(customerRequest);
        return ResponseEntity.ok(updated);
    }
}

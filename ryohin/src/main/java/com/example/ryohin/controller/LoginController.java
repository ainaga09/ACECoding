package com.example.ryohin.controller;

import com.example.ryohin.dto.customer.CustomerResponse;
import com.example.ryohin.dto.customer.loginRequest;
import com.example.ryohin.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")

public class LoginController {

    private final LoginService loginService;
    
}

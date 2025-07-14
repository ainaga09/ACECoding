package com.example.ryohin.controller;

import com.example.ryohin.dto.customer.LoginForm;
import com.example.ryohin.entity.Customer;
import com.example.ryohin.service.LoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") @Valid LoginForm form, Model model) {
        Optional<Customer> customerOpt = loginService.searchCustomerByEmail(form.getEmail());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getPasswordHash().equals(form.getPassword())) { // 実際はハッシュ比較すべき
                return "redirect:/menu";
            }
        }
        model.addAttribute("errorMsg", "メールアドレスかパスワードが間違っています。");
        return "login";
    }
}

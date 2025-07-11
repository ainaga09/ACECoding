package com.example.ryohin.controller;

import com.example.ryohin.dto.customer.CustomerResponse;
import com.example.ryohin.dto.customer.LoginRequest;
import com.example.ryohin.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService service;

	@GetMapping("/login")
	public String view(Model model, LoginForm form) {
		
		return "login"; //login.htmlをする
	}
	
	@PostMapping("/login")
	public String login(Model model, LoginForm form) {
		var userInfo = service.searchUserById(form.getLoginId());
		var isCorrectUserAuth = userInfo.isPresent() 
				&& form.getPassword().equals(userInfo.get().getPassword());
		if(isCorrectUserAuth) {
			return "redirect:/menu";
		}else {
			model.addAttribute("errorMsg","IDとPASSの組み合わせが間違っています。");
			return "login";
		}
	}
}
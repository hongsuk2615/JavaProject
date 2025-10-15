package com.springboot.springproject.auth.controller;

import com.springboot.springproject.auth.jwt.JwtProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/auth")
    public String authPage() {
        return "auth/auth";
    }
}

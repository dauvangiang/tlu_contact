package com.mobile.group.tlu_contact_be.controller;


import com.mobile.group.tlu_contact_be.dto.request.LoginRequest;
import com.mobile.group.tlu_contact_be.dto.response.LoginResponse;
import com.mobile.group.tlu_contact_be.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws Exception {
        return authService.login(request);
    }
}


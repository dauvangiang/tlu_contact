package com.mobile.group.tlu_contact_be.controller;


import com.mobile.group.tlu_contact_be.dto.request.LoginRequest;
import com.mobile.group.tlu_contact_be.dto.request.auth.UserLoginReq;
import com.mobile.group.tlu_contact_be.dto.request.user.AddUserReq;
import com.mobile.group.tlu_contact_be.dto.response.LoginResponse;
import com.mobile.group.tlu_contact_be.service.AuthService;
import com.mobile.group.tlu_contact_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("v1/auth/register")
    public ResponseEntity<Object> register(@RequestBody AddUserReq request) {
        System.err.println("Hello");
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("v1/auth/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginReq request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws Exception {
        return authService.login(request);
    }
}


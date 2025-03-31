package com.mobile.group.tlu_contact_be.controller;


import com.mobile.group.tlu_contact_be.dto.request.auth.UserLoginReq;
import com.mobile.group.tlu_contact_be.dto.request.user.CreateUserReq;
import com.mobile.group.tlu_contact_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("v1/auth/register")
    public ResponseEntity<Object> register(@Valid @RequestBody CreateUserReq request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("v1/auth/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginReq request) {
        return ResponseEntity.ok(userService.login(request));
    }
}


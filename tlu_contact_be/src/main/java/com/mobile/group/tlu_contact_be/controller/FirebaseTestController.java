package com.mobile.group.tlu_contact_be.controller;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/firebase")
public class FirebaseTestController {
    @GetMapping("/check")
    public String checkFirebase() {
        return "Firebase is working: " + FirebaseAuth.getInstance().toString();
    }
}

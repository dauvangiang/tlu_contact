package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.model.User;
import com.mobile.group.tlu_contact_be.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("v1/users/profile")
    public String getUserProfile(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        return "User UID: " + uid;
    }

    @GetMapping("v1/users/{id}")
    public User getUser(@PathVariable String id) throws ExecutionException, InterruptedException {
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        return userService.getAllUsers();
    }

    @PutMapping("v1/users/update/{id}")
    public void updateUser(@PathVariable String id, @RequestBody User user) throws ExecutionException, InterruptedException {
        userService.updateUser(id, user);
    }

    @DeleteMapping("v1/users/delete/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}

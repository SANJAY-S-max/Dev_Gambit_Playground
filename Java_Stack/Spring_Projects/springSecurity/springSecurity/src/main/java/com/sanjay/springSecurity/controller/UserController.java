package com.sanjay.springSecurity.controller;

import com.sanjay.springSecurity.model.Users;
import com.sanjay.springSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return service.register(user);
    }

    // Fixed: path is now "/login" under "/user" mapping = "/user/login"
    // which matches the permitAll in SecurityConfig ("/user/login")
    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return service.verify(user);
    }
}

package com.jerrycaffe.Spring.security.config.controllers;

import com.jerrycaffe.Spring.security.config.model.Users;
import com.jerrycaffe.Spring.security.config.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controllers {
    @Autowired
    private UserService userService;
    @GetMapping("/hello")
    public String getHello(){
        return "<h1>Hello my boss how are you today?</h1>";
    }
    @PostMapping("/user")
    public Users registerUser(@RequestBody Users user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Users users){
        return userService.verify(users);
    }


}

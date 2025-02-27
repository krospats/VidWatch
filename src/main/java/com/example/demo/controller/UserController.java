package com.example.demo.controller;

import com.example.demo.model.User;
import java.util.List;

import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/user")
    public User findUserByUserName(@RequestParam String username) {
        return userService.findUserByUserName(username);
    }

    @GetMapping("/{email}")
    public User findUserByEmail(@PathVariable String email) {
        return userService.findUserByUserEmail(email);
    }


}

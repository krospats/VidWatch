package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




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

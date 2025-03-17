package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import java.util.List;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping()
    public User findUserByUserName(@RequestParam String username) throws NotFoundException {
        User newUser =  userService.findUserByUserName(username);
        if (newUser == null) {
            throw new NotFoundException("User not found");
        }
        return newUser;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable int id) throws NotFoundException {

        User newUser =  userService.findUserById(id);
        if (newUser == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return newUser;
    }

}

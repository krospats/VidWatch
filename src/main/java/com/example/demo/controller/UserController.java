package com.example.demo.controller;

import com.example.demo.model.User;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/users")
public class UserController {


    @GetMapping
    public List<User> findAllUsers() {
        return List.of(
                User.builder().userName("Abobich").email("maksos@gmail.com").age(18).build(),
                User.builder().userName("Rambrosich").email("evilMonsta777@mail.ru").age(20).build(),
                User.builder().userName("PahomLegich").email("pahom396@zandex.ru").age(12).build()

        );
    }
}

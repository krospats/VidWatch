package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;
import org.springframework.stereotype.Service;


public interface UserService {
    List<User> findAllUsers();

    User findUserByUserName(String userName);

    User findUserByUserEmail(String email);


}

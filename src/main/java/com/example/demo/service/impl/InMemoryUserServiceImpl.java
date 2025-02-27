package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.InMemoryStudentDao;
import com.example.demo.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InMemoryUserServiceImpl implements UserService {

    private final InMemoryStudentDao studentRepository;

    @Override
    public List<User> findAllUsers() {
        return studentRepository.findAllUsers();
    }

    @Override
    public User findUserByUserName(String userName) {
        return studentRepository.findUserByUserName(userName);
    }

    @Override
    public User findUserByUserEmail(String email) {
        return studentRepository.findUserByUserEmail(email);

    }

}

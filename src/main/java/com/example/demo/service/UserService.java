package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.InMemoryStudentDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService {
    private final InMemoryStudentDao studentRepository;

    @Autowired
    UserService(InMemoryStudentDao studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<User> findAllUsers() {
        return studentRepository.findAllUsers();
    }

    public User findUserByUserName(String userName) {
        return studentRepository.findUserByUserName(userName);
    }

    public User findUserByUserEmail(String email) {
        return studentRepository.findUserByUserEmail(email);

    }


}

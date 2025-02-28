package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.InMemoryStudentDao;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;




@Service
@AllArgsConstructor
public class UserService {
    private final InMemoryStudentDao studentRepository;

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

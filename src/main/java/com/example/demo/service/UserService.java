package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Создание пользователя
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Получение пользователя по ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Обновление пользователя
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserName(userDetails.getUserName());
        user.setEmail(userDetails.getEmail());
        user.setAge(userDetails.getAge());
        return userRepository.save(user);
    }

    // Удаление пользователя
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
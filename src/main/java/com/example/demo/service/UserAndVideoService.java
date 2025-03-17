package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserAndVideoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserAndVideoService {

    @Autowired
    private UserAndVideoRepository userRepository;

    // Получение пользователя по ID с видео
    public Optional<User> getUserWithVideos(Long id) {
        return userRepository.findByIdWithVideos(id);
    }
}
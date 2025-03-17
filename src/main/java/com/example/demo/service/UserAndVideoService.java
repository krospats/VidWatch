package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserAndVideoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserAndVideoService {

    private final UserAndVideoRepository userRepository;

    @Autowired
    public UserAndVideoService(UserAndVideoRepository userRepository) {
        this.userRepository = userRepository;

    }

    // Получение пользователя по ID с видео
    public Optional<User> getUserWithVideos(Long id) {
        return userRepository.findByIdWithVideos(id);
    }
}
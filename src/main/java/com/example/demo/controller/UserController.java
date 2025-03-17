package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.VideoDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserAndVideoRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.VideoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/v1/users")
public class UserController {


    private final UserService userService;
    private final VideoService videoService;


    @Autowired
    public UserController(UserService userService, VideoService videoService) {
        this.userService = userService;
        this.videoService = videoService;

    }

    // Создание пользователя
    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Получение всех пользователей
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // Получение пользователя по ID
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Обновление пользователя
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    // Удаление пользователя
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        List<VideoDto> videos = videoService.getAllVideos();
        for (VideoDto video : videos) {
            videoService.deleteVideo(video.getId());
        }
        userService.deleteUser(id);
    }
}
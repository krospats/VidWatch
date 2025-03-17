package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.service.UserAndVideoService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
public class UserAndVideoController {

    private final UserAndVideoService userAndVideoService;

    @Autowired
    public UserAndVideoController(UserAndVideoService videoService) {
        this.userAndVideoService = videoService;
    }

    @GetMapping("/{id}/videos")
    public ResponseEntity<User> getUserWithVideos(@PathVariable Long id) {
        Optional<User> user = userAndVideoService.getUserWithVideos(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

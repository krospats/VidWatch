package com.example.demo.controller;


import com.example.demo.dto.UserDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.service.CacheService;
import com.example.demo.service.UserAndVideoService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
public class  UserAndVideoController {

    private final UserAndVideoService userAndVideoService;
    private final CacheService cacheService;

    @Autowired
    public UserAndVideoController(UserAndVideoService videoService, CacheService cacheService) {
        this.userAndVideoService = videoService;
        this.cacheService = cacheService;
    }



    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> getUsersByVideoName(@RequestParam String videoName) {
        List<UserDto> users = userAndVideoService.getUsersByVideoName(videoName);

        if (users.isEmpty()) {
            throw new NotFoundException("No users found with video: " + videoName);
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/cache-info")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("currentSize", cacheService.size());
        info.put("maxSize", cacheService.getMaxSize());
        return ResponseEntity.ok(info);
    }
}

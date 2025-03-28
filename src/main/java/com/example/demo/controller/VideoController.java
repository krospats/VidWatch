package com.example.demo.controller;

import com.example.demo.dto.VideoDto;
import com.example.demo.model.Video;
import com.example.demo.service.VideoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // Создание видео
    @PostMapping
    public ResponseEntity<VideoDto> createVideo(@RequestBody Video video) {
        VideoDto createdVideo = videoService.createVideo(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
    }

    // Получение всех видео
    @GetMapping
    public ResponseEntity<List<VideoDto>> getAllVideos() {
        List<VideoDto> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    // Получение видео по ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getVideoById(@PathVariable Long id) {
        VideoDto video = videoService.getVideoById(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        }
        return ResponseEntity.notFound().build();
    }

    // Обновление видео
    @PutMapping("/{id}")
    public ResponseEntity<VideoDto> updateVideo(@PathVariable Long id, @RequestBody Video videoDetails) {
        VideoDto updatedVideo = videoService.updateVideo(id, videoDetails);
        return ResponseEntity.ok(updatedVideo);
    }

    // Удаление видео
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}

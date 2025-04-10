package com.example.demo.controller;

import com.example.demo.dto.VideoDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/videos")
@Tag(name = "Видео", description = "Операции с видео")
public class VideoController {

    private final VideoService videoService;
    private final UserRepository userRepository;

    @Autowired
    public VideoController(VideoService videoService, UserRepository userRepository) {
        this.videoService = videoService;
        this.userRepository = userRepository;
    }

    // Создание видео

    @PostMapping
    @Operation(
            summary = "Создать новое видео",
            description = "Создает новое видео и связывает его с пользователем",
            responses = {
                @ApiResponse(responseCode = "201", description = "Видео успешно создано"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    public ResponseEntity<VideoDto> createVideo(
            @Parameter(description = "Данные видео", required = true)
            @Valid @RequestBody Video video) {
        if (video.getUserId() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Fetch the user from repository
        User user = userRepository.findById(video.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + video.getUserId()));

        video.setUser(user);
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
    public ResponseEntity<VideoDto> getVideoById(@PathVariable @Min(value = 1, message = "Id не может быть отрицательным") Long id) {
        VideoDto video = videoService.getVideoById(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        }
        return ResponseEntity.notFound().build();
    }

    // Обновление видео
    @PutMapping("/{id}")
    public ResponseEntity<VideoDto> updateVideo(
            @PathVariable @Min(value = 1, message = "Id не может быть отрицательным") Long id,
            @Valid @RequestBody Video videoDetails) {
        VideoDto videoDto = videoService.getVideoById(id);
        if (videoDto == null) {
            throw new NotFoundException("there is no video with id " + id);
        }

        VideoDto updatedVideo = videoService.updateVideo(id, videoDetails);
        return ResponseEntity.ok(updatedVideo);
    }

    // Удаление видео
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable @Min(value = 1, message = "Id не может быть отрицательным") Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}

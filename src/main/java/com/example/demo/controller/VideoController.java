package com.example.demo.controller;

import com.example.demo.dto.VideoDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;
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

    @PostMapping("/bulk")
    @Operation(
            summary = "Массовое создание видео",
            description = "Создает несколько видео за один запрос",
            responses = {
                @ApiResponse(
                            responseCode = "201",
                            description = "Видео успешно созданы",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = VideoDto.class))
                            )
                    ),
                @ApiResponse(
                            responseCode = "400",
                            description = "Неверные входные данные"
                    ),
                @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден"
                    )
            }
    )
    public ResponseEntity<List<VideoDto>> createVideosBulk(
            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Список видео для создания",
                    required = true,
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = Video.class))
                    )
            )
            List<Video> videos) {

        List<VideoDto> createdVideos = videos.stream()
                .map(request -> {
                    User user = userRepository.findById(request.getUserId())
                            .orElseThrow(() -> new NotFoundException("User not found"));
                    request.setUser(user);
                    return videoService.createVideo(request);
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideos);
    }

    @Operation(
            summary = "Получить все видео",
            description = "Возвращает список всех видео",
            responses = {
                @ApiResponse(responseCode = "200", description = "Лист успешно возвращен"),
                @ApiResponse(responseCode = "404", description = "Не найдено ни одного видео")
            }
    )
    @GetMapping
    public ResponseEntity<List<VideoDto>> getAllVideos() {
        List<VideoDto> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    @Operation(
            summary = "Получить видео по айди",
            description = "Возвращает видео с введенным айди",
            responses = {
                @ApiResponse(responseCode = "200", description = "Видео найдено"),
                @ApiResponse(responseCode = "200", description = "Ошибка в вводимых данных"),
                @ApiResponse(responseCode = "404", description = "Не найдено видео")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getVideoById(@PathVariable @Min(value = 1, message = "Id не может быть отрицательным") Long id) {
        VideoDto video = videoService.getVideoById(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Обновить видео по айди",
            description = "Обновляет видео с введенным айди",
            responses = {@ApiResponse(responseCode = "200",
                    description = "Видео изменено"), @ApiResponse(responseCode = "200",
                    description = "Ошибка в вводимых данных"), @ApiResponse(responseCode = "404",
                    description = "Не найдено видео")
            }
    )
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

    @Operation(
            summary = "Удалить видео по айди",
            description = "Удаляет видео с введенным айди",
            responses = {@ApiResponse(responseCode = "200",
                    description = "Видео удалено"), @ApiResponse(responseCode = "200",
                    description = "Ошибка в вводимых данных")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable @Min(value = 1, message = "Id не может быть отрицательным") Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}

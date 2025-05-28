package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.service.CacheService;
import com.example.demo.service.UserAndVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи и видео", description = "Операции с пользователями и их видео")
public class  UserAndVideoController {

    private final UserAndVideoService userAndVideoService;
    private final CacheService cacheService;

    @Autowired
    public UserAndVideoController(UserAndVideoService videoService, CacheService cacheService) {
        this.userAndVideoService = videoService;
        this.cacheService = cacheService;
    }


    @Operation(
            summary = "Поиск пользователей по видео",
            description = "Возвращает список пользователей, у которых есть видео с указанным названием",
            responses = {
                @ApiResponse(
                            responseCode = "200",
                            description = "Пользователи найдены",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    ),
                @ApiResponse(
                            responseCode = "404",
                            description = "Пользователи не найдены",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    )
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> getUsersByVideoName(@RequestParam String videoName) {
        List<UserDto> users = userAndVideoService.getUsersByVideoName(videoName);

        if (users.isEmpty()) {
            throw new NotFoundException("No users found with video: " + videoName);
        }

        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Информация о кэше",
            description = "Возвращает текущий размер и максимальный размер кэша",
            responses = {
                @ApiResponse(
                            responseCode = "200",
                            description = "Информация о кэше",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/cache-info")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("currentSize", cacheService.size());
        info.put("maxSize", cacheService.getMaxSize());
        return ResponseEntity.ok(info);
    }
}

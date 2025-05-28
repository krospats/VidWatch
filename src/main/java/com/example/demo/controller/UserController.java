package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/users")
@Tag(name = "Управление пользователями", description = "CRUD операции для пользователей")
public class UserController {


    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping
    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя с указанными данными",
            responses = {
                @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                @ApiResponse(
                            responseCode = "400",
                            description = "Неверные входные данные",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    )
            }
    )
    public UserDto createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей",
            responses = {
                @ApiResponse(
                            responseCode = "200",
                            description = "Лист успешно возвращен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    ),
                @ApiResponse(
                            responseCode = "404",
                            description = "Пользователей нету",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    )
            }
    )
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        if (users.isEmpty()) {
            throw new NotFoundException("there is no users");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя по айди",
            description = "Возвращает пользователя с указанным айди",
            responses = {
                @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                @ApiResponse(
                            responseCode = "400",
                            description = "Неверные входные данные",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    ),
                @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя не существует",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    )

            }
    )
    public ResponseEntity<UserDto> getUserById(@Valid @Min(value = 1, message = "Id не может быть отрицательным") @PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        if (userDto == null) {
            throw new NotFoundException("there is no user with id " + id);
        }
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Изменить пользователя",
            description = "Изменяет существующего пользователя пользователя",
            responses = {
                @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно изменен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                @ApiResponse(
                            responseCode = "400",
                            description = "Неверные входные данные",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    ),
                @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    )
            }
    )
    public ResponseEntity<UserDto> updateUser(
            @Min(value = 1, message = "Id не может быть отрицательным") @PathVariable Long id,
            @Valid @RequestBody User userDetails) {
        UserDto userDto = userService.updateUser(id, userDetails);
        if (userDto == null) {
            throw new NotFoundException("there is no user with id " + id);
        }
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Изменить пользователя",
            description = "Изменяет существующего пользователя пользователя",
            responses = {
                @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно удален",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                @ApiResponse(
                            responseCode = "400",
                            description = "Неверные входные данные",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    ),
                @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "Неверные данные", format = "String")
                            )
                    )
            }
    )
    public void deleteUser(@Min(value = 1, message = "Id не может быть отрицательным") @PathVariable Long id) {
        userService.deleteUser(id);
    }
}
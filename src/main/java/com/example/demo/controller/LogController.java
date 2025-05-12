package com.example.demo.controller;

import com.example.demo.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/logs")
@Tag(name = "Логи", description = "Операции с логами системы")
public class LogController {

    @SuppressWarnings("checkstyle:Indentation")
    @GetMapping(value = "/{date}", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Получить логи за дату",
            description = "Возвращает файл с логами за указанную дату",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Логи успешно получены",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Логи не найдены"
                    )
            }
    )
    public ResponseEntity<Resource> getLogsByDate(
            @Parameter(description = "Дата в формате YYYY-MM-DD", example = "2023-12-31")
            @PathVariable String date) throws IOException {

        // 1. Проверяем формат даты
        LocalDate logDate;
        try {
            logDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Use YYYY-MM-DD");
        }

        // 2. Проверяем существование файла логов
        Path logFilePath = Path.of("application.log");
        if (!Files.exists(logFilePath)) {
            throw new NotFoundException("Log file not found");
        }

        // 3. Фильтруем логи по дате
        List<String> filteredLines = Files.lines(logFilePath)
                .filter(line -> line.startsWith(logDate.toString()))
                .toList();

        if (filteredLines.isEmpty()) {
            throw new NotFoundException("No logs found for date: " + date);
        }

        // 4. Создаем временный файл в памяти (не на диске)
        byte[] logContent = String.join(System.lineSeparator(), filteredLines).getBytes();
        ByteArrayResource resource = new ByteArrayResource(logContent);

        // 5. Формируем ответ с файлом
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=logs-" + date + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(logContent.length)
                .body(resource);
    }
}
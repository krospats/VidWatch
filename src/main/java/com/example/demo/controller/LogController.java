package com.example.demo.controller;

import com.example.demo.dto.LogFileResponse;
import com.example.demo.dto.LogTaskResponse;
import com.example.demo.dto.LogTaskStatus;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/logs")
@Tag(name = "Логи", description = "Асинхронная работа с логами")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/generate/{date}")  // Убедитесь, что URL одинаковый!
    @Operation(summary = "Запустить генерацию логов")
    public ResponseEntity<LogTaskResponse> generateLogFile(
            @PathVariable String date) {
        String taskId = logService.startLogGeneration(date);
        return ResponseEntity.accepted()
                .body(new LogTaskResponse(taskId, "Task started", LogTaskStatus.PROCESSING));
    }


    @GetMapping("/status/{taskId}")
    @Operation(summary = "Получить статус задачи",
            description = "Возвращает текущий статус задачи генерации логов")
    public ResponseEntity<LogTaskResponse> getTaskStatus(
            @Parameter(description = "ID задачи", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String taskId) {

        LogTaskResponse response = logService.getTaskStatus(taskId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{taskId}")
    @Operation(summary = "Скачать сгенерированные логи",
            description = "Возвращает файл с логами, если задача завершена успешно")
    @ApiResponse(responseCode = "200", description = "Файл логов успешно получен",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "404", description = "Логи не найдены или задача не существует")
    @ApiResponse(responseCode = "409", description = "Задача еще не завершена")
    @ApiResponse(responseCode = "400", description = "Неверный ID задачи")
    public ResponseEntity<Resource> downloadLogFile(
            @Parameter(description = "ID задачи", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String taskId) {

        try {
            LogFileResponse fileResponse = logService.getLogFile(taskId);

            if (fileResponse == null || fileResponse.getContent() == null || fileResponse.getContent().length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ByteArrayResource("No logs found for this task".getBytes()));
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + fileResponse.getFilename())
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(fileResponse.getContent().length)
                    .body(new ByteArrayResource(fileResponse.getContent()));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            // Задача еще не завершена
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ByteArrayResource(e.getMessage().getBytes()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ByteArrayResource(e.getMessage().getBytes()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ByteArrayResource("Error processing log file".getBytes()));
        }
    }
}


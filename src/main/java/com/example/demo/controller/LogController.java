package com.example.demo.controller;

import com.example.demo.exceptions.NotFoundException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/logs")
public class LogController {

    @GetMapping(value = "/{date}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<InputStreamResource> getLogsByDate(@PathVariable String date) throws IOException {
        LocalDate logDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        String logFilePath = "application.log";

        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            throw new NotFoundException("Log file not found");
        }

        // Фильтрация логов по дате
        List<String> filteredLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            filteredLines = reader.lines()
                    .filter(line -> line.startsWith(logDate.toString()))
                    .toList();
        }

        // Создаем временный файл с отфильтрованными логами
        File tempFile = File.createTempFile("logs-" + date, ".log");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (String line : filteredLines) {
                writer.write(line);
                writer.newLine();
            }
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs-" + date + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(tempFile.length())
                .body(resource);
    }
}
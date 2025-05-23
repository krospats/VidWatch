package com.example.demo.task;

import com.example.demo.dto.LogTaskStatus;
import com.example.demo.exceptions.NotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LogTask {
    private final String date;
    private volatile LogTaskStatus status;
    private String errorMessage;
    private byte[] logContent;

    public LogTask(String date) {
        this.date = date;
        this.status = LogTaskStatus.PROCESSING;
    }

    public void generate() throws IOException {
        Path logFilePath = Path.of("application.log");
        if (!Files.exists(logFilePath)) {
            throw new FileNotFoundException("Log file not found");
        }

        String content = Files.lines(logFilePath)
                .filter(line -> line.startsWith(date))
                .collect(Collectors.joining(System.lineSeparator()));

        if (content.isEmpty()) {
            throw new NotFoundException("No logs found for date: " + date);
        }

        this.logContent = content.getBytes();
    }

    // геттеры и сеттеры
}
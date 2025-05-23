package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LogTaskResponse {
    private String taskId;
    private String message;
    private LogTaskStatus status;
    // конструкторы, геттеры, сеттеры
}

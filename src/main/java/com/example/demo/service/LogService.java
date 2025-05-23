package com.example.demo.service;

import com.example.demo.dto.LogFileResponse;
import com.example.demo.dto.LogTaskResponse;
import com.example.demo.dto.LogTaskStatus;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.demo.exceptions.NotFoundException;
import com.example.demo.task.LogTask;
import org.springframework.stereotype.Service;



@Service
public class LogService {
    private final Map<String, LogTaskStatus> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<String, String> taskMessages = new ConcurrentHashMap<>();
    private final Map<String, LogTask> tasks = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();


    public String startLogGeneration(String date) {
        String taskId = UUID.randomUUID().toString();
        LogTask task = new LogTask(date);
        tasks.put(taskId, task);

        executor.submit(() -> {
            try {
                updateTaskStatus(taskId, LogTaskStatus.PROCESSING, "processing");
                Thread.sleep(20000);

                task.generate();  // Ваша реальная логика генерации
                task.setStatus(LogTaskStatus.COMPLETED);
                updateTaskStatus(taskId, LogTaskStatus.COMPLETED, "completed");

            } catch (InterruptedException e) {
                task.setStatus(LogTaskStatus.FAILED);
                updateTaskStatus(taskId, LogTaskStatus.FAILED, "failed");
                task.setErrorMessage("Processing interrupted");
                Thread.currentThread().interrupt();

            } catch (Exception e) {
                task.setStatus(LogTaskStatus.FAILED);
                updateTaskStatus(taskId, LogTaskStatus.FAILED, "failed");
                task.setErrorMessage(e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        return taskId;
    }

    public void updateTaskStatus(String taskId, LogTaskStatus status, String message) {
        taskStatusMap.put(taskId, status);
        taskMessages.put(taskId, message);
    }

    public LogTaskResponse getTaskStatus(String taskId) {
        LogTaskStatus status = taskStatusMap.get(taskId);
        String message = taskMessages.get(taskId);
        return new LogTaskResponse(taskId, message, status);
    }

    public LogFileResponse getLogFile(String taskId) {
        LogTask task = tasks.get(taskId);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        if (task.getStatus() != LogTaskStatus.COMPLETED) {
            throw new IllegalStateException("Task is not completed yet");
        }

        return new LogFileResponse(
                taskId,
                "logs-" + task.getDate() + ".log",
                task.getLogContent()
        );
    }

}

package com.example.taskmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Task Management System");
        response.put("version", "1.0.0");
        response.put("status", "Running");
        response.put("endpoints", Map.of(
            "tasks", "/api/tasks",
            "summary", "/api/tasks/summary",
            "h2-console", "/h2-console",
            "health", "/actuator/health"
        ));
        response.put("message", "Welcome to the Task Management System API!");
        return response;
    }
}

package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.dto.TaskSummaryDTO;
import com.example.taskmanagement.entity.TaskStatus;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // For local development - remove in production
public class TaskController {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    
    @Autowired
    private TaskService taskService;
    
    // Create a new task
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        logger.info("REST request to create task: {}", taskDTO.getTitle());
        
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }
    
    // Get all tasks with pagination
    @GetMapping
    public ResponseEntity<Page<TaskDTO>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("REST request to get all tasks");
        
        Page<TaskDTO> tasks = taskService.getAllTasks(page, size, sortBy, sortDir);
        return ResponseEntity.ok(tasks);
    }
    
    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        logger.info("REST request to get task: {}", id);
        
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }
    
    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, 
                                             @Valid @RequestBody TaskDTO taskDTO) {
        logger.info("REST request to update task: {}", id);
        
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }
    
    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("REST request to delete task: {}", id);
        
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    
    // Get tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(@PathVariable TaskStatus status) {
        logger.info("REST request to get tasks by status: {}", status);
        
        List<TaskDTO> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }
    
    // Get tasks by assignee
    @GetMapping("/assignee/{assignedTo}")
    public ResponseEntity<List<TaskDTO>> getTasksByAssignee(@PathVariable String assignedTo) {
        logger.info("REST request to get tasks for assignee: {}", assignedTo);
        
        List<TaskDTO> tasks = taskService.getTasksByAssignee(assignedTo);
        return ResponseEntity.ok(tasks);
    }
    
    // Search tasks
    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasks(@RequestParam String title) {
        logger.info("REST request to search tasks by title: {}", title);
        
        List<TaskDTO> tasks = taskService.searchTasksByTitle(title);
        return ResponseEntity.ok(tasks);
    }
    
    // Get overdue tasks
    @GetMapping("/overdue")
    public ResponseEntity<List<TaskDTO>> getOverdueTasks() {
        logger.info("REST request to get overdue tasks");
        
        List<TaskDTO> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // Get task summary
    @GetMapping("/summary")
    public ResponseEntity<TaskSummaryDTO> getTaskSummary() {
        logger.info("REST request to get task summary");
        
        TaskSummaryDTO summary = taskService.getTaskSummary();
        return ResponseEntity.ok(summary);
    }
    
    // Mark task as completed
    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskDTO> completeTask(@PathVariable Long id) {
        logger.info("REST request to complete task: {}", id);
        
        TaskDTO completedTask = taskService.completeTask(id);
        return ResponseEntity.ok(completedTask);
    }
}

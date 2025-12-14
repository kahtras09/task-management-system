package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.dto.TaskSummaryDTO;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.TaskPriority;
import com.example.taskmanagement.entity.TaskStatus;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    
    @Autowired
    private TaskRepository taskRepository;
    
    // Create a new task
    public TaskDTO createTask(TaskDTO taskDTO) {
        logger.info("Creating new task: {}", taskDTO.getTitle());
        
        Task task = convertToEntity(taskDTO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        Task savedTask = taskRepository.save(task);
        logger.info("Task created with ID: {}", savedTask.getId());
        
        return convertToDTO(savedTask);
    }
    
    // Get task by ID
    public TaskDTO getTaskById(Long id) {
        logger.info("Fetching task with ID: {}", id);
        
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return convertToDTO(task.get());
        } else {
            logger.warn("Task not found with ID: {}", id);
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
    }
    
    // Get all tasks with pagination
    public Page<TaskDTO> getAllTasks(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all tasks - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> tasks = taskRepository.findAll(pageable);
        
        return tasks.map(this::convertToDTO);
    }
    
    // Update task
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        logger.info("Updating task with ID: {}", id);
        
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        // Update fields
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setPriority(taskDTO.getPriority());
        existingTask.setAssignedTo(taskDTO.getAssignedTo());
        existingTask.setDueDate(taskDTO.getDueDate());
        existingTask.setUpdatedAt(LocalDateTime.now());
        
        Task updatedTask = taskRepository.save(existingTask);
        logger.info("Task updated: {}", updatedTask.getId());
        
        return convertToDTO(updatedTask);
    }
    
    // Delete task
    public void deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);
        
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        
        taskRepository.deleteById(id);
        logger.info("Task deleted: {}", id);
    }
    
    // Get tasks by status
    public List<TaskDTO> getTasksByStatus(TaskStatus status) {
        logger.info("Fetching tasks by status: {}", status);
        
        List<Task> tasks = taskRepository.findByStatus(status);
        return tasks.stream()
                   .map(this::convertToDTO)
                   .collect(Collectors.toList());
    }
    
    // Get tasks assigned to a person
    public List<TaskDTO> getTasksByAssignee(String assignedTo) {
        logger.info("Fetching tasks assigned to: {}", assignedTo);
        
        List<Task> tasks = taskRepository.findByAssignedTo(assignedTo);
        return tasks.stream()
                   .map(this::convertToDTO)
                   .collect(Collectors.toList());
    }
    
    // Get overdue tasks
    public List<TaskDTO> getOverdueTasks() {
        logger.info("Fetching overdue tasks");
        
        List<Task> tasks = taskRepository.findOverdueTasks(
            LocalDateTime.now(), TaskStatus.COMPLETED);
        
        return tasks.stream()
                   .map(this::convertToDTO)
                   .collect(Collectors.toList());
    }
    
    // Search tasks by title
    public List<TaskDTO> searchTasksByTitle(String title) {
        logger.info("Searching tasks by title: {}", title);
        
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return tasks.stream()
                   .map(this::convertToDTO)
                   .collect(Collectors.toList());
    }
    
    // Get task summary for dashboard
    public TaskSummaryDTO getTaskSummary() {
        logger.info("Generating task summary");
        
        long totalTasks = taskRepository.count();
        long pendingTasks = taskRepository.countByStatus(TaskStatus.PENDING);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long completedTasks = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long overdueTasks = taskRepository.findOverdueTasks(
            LocalDateTime.now(), TaskStatus.COMPLETED).size();
        
        return new TaskSummaryDTO(totalTasks, pendingTasks, inProgressTasks, 
                                completedTasks, overdueTasks);
    }
    
    // Mark task as completed
    public TaskDTO completeTask(Long id) {
        logger.info("Marking task as completed: {}", id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        task.setStatus(TaskStatus.COMPLETED);
        task.setUpdatedAt(LocalDateTime.now());
        
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }
    
    // Helper methods for conversion between Entity and DTO
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setAssignedTo(task.getAssignedTo());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
    
    private Task convertToEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.PENDING);
        task.setPriority(dto.getPriority() != null ? dto.getPriority() : TaskPriority.MEDIUM);
        task.setAssignedTo(dto.getAssignedTo());
        task.setDueDate(dto.getDueDate());
        return task;
    }
}

package com.example.taskmanagement.repository;

import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.TaskPriority;
import com.example.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Find tasks by status
    List<Task> findByStatus(TaskStatus status);
    
    // Find tasks by priority
    List<Task> findByPriority(TaskPriority priority);
    
    // Find tasks assigned to a specific person
    List<Task> findByAssignedTo(String assignedTo);
    
    // Find tasks by status with pagination
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    
    // Find tasks due before a certain date
    List<Task> findByDueDateBefore(LocalDateTime dateTime);
    
    // Find overdue tasks (due date in the past and not completed)
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status != :completedStatus")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now, 
                               @Param("completedStatus") TaskStatus completedStatus);
    
    // Find tasks by title containing (case-insensitive search)
    List<Task> findByTitleContainingIgnoreCase(String title);
    
    // Count tasks by status
    long countByStatus(TaskStatus status);
    
    // Find tasks assigned to person with specific status
    List<Task> findByAssignedToAndStatus(String assignedTo, TaskStatus status);
    
    // Custom query to find high priority tasks that are not completed
    @Query("SELECT t FROM Task t WHERE t.priority = :priority AND t.status != :status ORDER BY t.dueDate ASC")
    List<Task> findHighPriorityIncomplete(@Param("priority") TaskPriority priority, 
                                         @Param("status") TaskStatus status);
}

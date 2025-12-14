package com.example.taskmanagement.dto;

public class TaskSummaryDTO {
    private long totalTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private long completedTasks;
    private long overdueTasks;
    
    // Constructors
    public TaskSummaryDTO() {}
    
    public TaskSummaryDTO(long totalTasks, long pendingTasks, long inProgressTasks, 
                         long completedTasks, long overdueTasks) {
        this.totalTasks = totalTasks;
        this.pendingTasks = pendingTasks;
        this.inProgressTasks = inProgressTasks;
        this.completedTasks = completedTasks;
        this.overdueTasks = overdueTasks;
    }
    
    // Getters and Setters
    public long getTotalTasks() {
        return totalTasks;
    }
    
    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }
    
    public long getPendingTasks() {
        return pendingTasks;
    }
    
    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }
    
    public long getInProgressTasks() {
        return inProgressTasks;
    }
    
    public void setInProgressTasks(long inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }
    
    public long getCompletedTasks() {
        return completedTasks;
    }
    
    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }
    
    public long getOverdueTasks() {
        return overdueTasks;
    }
    
    public void setOverdueTasks(long overdueTasks) {
        this.overdueTasks = overdueTasks;
    }
}

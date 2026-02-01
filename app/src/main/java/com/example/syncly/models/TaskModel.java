package com.example.syncly.models;

public class TaskModel {
    int task_id;
    String description;
    String due_date;
    int status;

    public TaskModel(int task_id, String description, String dueDate, int status) {
        this.task_id = task_id;
        this.description = description;
        this.due_date = dueDate;
        this.status = status;
    }

    public String getDescription() { return description; }
    public String getDueDate() { return due_date; }
}


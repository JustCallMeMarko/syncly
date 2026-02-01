package com.example.syncly.models;

public class TaskModel {
    int task_id;
    String name;
    String due_date;
    int status;

    public TaskModel(int task_id, String name, String dueDate, int status) {
        this.task_id = task_id;
        this.name = name;
        this.due_date = dueDate;
        this.status = status;
    }

    public String getName() { return name; }
    public String getDueDate() { return due_date; }
}


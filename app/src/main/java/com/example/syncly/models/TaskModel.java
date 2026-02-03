package com.example.syncly.models;

public class TaskModel {
    private int task_id;
    private String name;
    private String due_date;
    private int status;

    public TaskModel(int task_id, String name, String due_date, int status) {
        this.task_id = task_id;
        this.name = name;
        this.due_date = due_date;
        this.status = status;
    }

    public int getId() { return task_id; }
    public String getName() { return name; }
    public String getDueDate() { return due_date; }
    public int isCompleted() { return status; }

    public void setStatus(int status) { this.status = status; }
    public void setName(String name) { this.name = name; }
    public void setDueDate(String due_date) { this.due_date = due_date; }
}
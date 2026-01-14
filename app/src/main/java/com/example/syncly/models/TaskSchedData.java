package com.example.syncly.models;

import java.util.Date;

public class TaskSchedData {
    String name;
    String description;
    Date due_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public TaskSchedData(String name, String description, Date due_date) {
        this.name = name;
        this.description = description;
        this.due_date = due_date;
    }
}

package com.example.syncly.models;

public class ScheduleModel {
    int schedId;
    String description;
    String date;

    public ScheduleModel(int schedId, String description, String date) {
        this.schedId = schedId;
        this.description = description;
        this.date = date;
    }

    public String getDescription() { return description; }
    public String getDate() { return date; }
}


package com.example.syncly.backend;

import com.example.syncly.models.TaskSchedData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskData {
    private static TaskData instance;

    // 2. The data you want to share (e.g., your API response)
    private List<TaskSchedData> items = new ArrayList<>();

    public List<TaskSchedData> getItems() {
        return items;
    }

    public void setItems(List<TaskSchedData> items) {
        this.items = items;
    }

    public void addItems(String name, String description, Date date){
        items.add(new TaskSchedData(name, description, date));
    }

    // 3. Private constructor so no one else can call "new DataManager()"
    private TaskData() {}

    // 4. The global "Getter" for the instance itself
    public static synchronized TaskData getInstance() {
        if (instance == null) {
            instance = new TaskData();
        }
        return instance;
    }
}

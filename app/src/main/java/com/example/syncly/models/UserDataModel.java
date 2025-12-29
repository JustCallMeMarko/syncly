package com.example.syncly.models;

public class UserDataModel {

    private Info info;

    private UserData data;

    // Getters
    public Info getInfo() { return info; }
    public UserData getData() { return data; }

    // --- Inner Class for "info" ---
    public class Info {
        private String status;

        public String getStatus() { return status; }
    }

    // --- Inner Class for "data" ---
    public class UserData {
        private int userId;

        private String username;

        public int getUserId() { return userId; }
        public String getUsername() { return username; }
    }
}
package com.example.syncly.models;

import android.net.Uri;

public class SpacesModel {
    public int getSpace_id() {
        return space_id;
    }

    private int space_id;
    private String name;
    private String deadline;
    private Uri imageUri = null;
    public SpacesModel(int spaceId, String name, String deadline) {
        space_id = spaceId;
        this.name = name;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public String getDeadline() {
        return deadline;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}

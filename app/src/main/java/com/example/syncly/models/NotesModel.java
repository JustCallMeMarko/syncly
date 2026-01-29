package com.example.syncly.models;

public class NotesModel {
    int note_id;
    String note;

    public NotesModel(int note_id, String note) {
        this.note_id = note_id;
        this.note = note;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

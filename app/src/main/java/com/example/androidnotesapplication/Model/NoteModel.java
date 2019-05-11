package com.example.androidnotesapplication.Model;

public class NoteModel {

    private String title;
    private String description;
    public NoteModel() {
    }

    public NoteModel(String title, String description, String time) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}

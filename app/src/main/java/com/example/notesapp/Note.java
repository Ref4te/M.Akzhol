package com.example.notesapp;

public class Note {
    private int id;
    private String title;
    private String category;
    private String time;
    private String description;

    public Note(int id, String title, String category, String time, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.time = time;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}
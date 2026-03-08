package com.example.makzholvtipo_35;

public class Task {
    private int id;
    private String title;
    private String deadline;
    private String category;

    public Task(int id, String title, String deadline, String category) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getCategory() {
        return category;
    }
}
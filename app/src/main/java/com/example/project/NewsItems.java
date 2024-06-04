package com.example.project;

public class NewsItems {
    String title;
    String description;
    String link;

    public NewsItems(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    @Override
    public String toString() {
        return title; // Useful for ArrayAdapter that can utilize toString() for display
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}

package com.example.capstone.ui.mock;

import java.util.List;

public class Difficulty {
    private String id;
    private String name; // e.g., "Easy", "Medium", "Hard"
    private String description;
    private List<String> tags;

    public Difficulty(String id, String name, String description, List<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getTags() { return tags; }
}

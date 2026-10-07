package com.example.capstone.model;

import java.util.List;

public class InterviewTopic {
    private final String title;
    private final String difficulty;
    private final List<String> focusAreas;
    private final String id;

    public InterviewTopic(String title, String difficulty, List<String> focusAreas, String id) {
        this.title = title;
        this.difficulty = difficulty;
        this.focusAreas = focusAreas;
        this.id = id;
    }
    public String getTitle() {
        return title;
        }
    public String getDifficulty() {
        return difficulty;
    }
    public List<String> getFocusAreas() {
        return focusAreas;
    }
    public String getId() {
        return id;
    }
}

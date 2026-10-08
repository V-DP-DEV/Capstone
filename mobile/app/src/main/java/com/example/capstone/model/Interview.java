package com.example.capstone.model;

import java.util.List;

public class Interview {
    private String id;
    private String title;
    private String difficulty;
    private List<String> focusAreas;

    public Interview(String id, String title, String difficulty, List<String> focusAreas) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.focusAreas = focusAreas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getFocusAreas() {
        return focusAreas;
    }

    public void setFocusAreas(List<String> focusAreas) {
        this.focusAreas = focusAreas;
    }
}

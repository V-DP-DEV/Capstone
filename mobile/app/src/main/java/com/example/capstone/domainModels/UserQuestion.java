package com.example.capstone.domainModels;

/**
 * Domain model representing an individual question within a mock interview session.
 * Stores question identification, text content, difficulty level, and question type classifications.
 */
public class UserQuestion {
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public String getType() {
        return type;
    }

    private String id;
    private String text;
    private String difficulty;

    private String type;

    public void setType(String technical) {
    }

    public void setText(String s) {
    }
}
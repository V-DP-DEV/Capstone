package com.example.capstone.model;

public class Question {
    private String id;
    private String category;
    private String questionText;
    private String codeSnippet;
    private int durationInSeconds;

    public Question(String id, String category, String questionText, String codeSnippet, int durationInSeconds) {
        this.id = id;
        this.category = category;
        this.questionText = questionText;
        this.codeSnippet = codeSnippet;
        this.durationInSeconds = durationInSeconds;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCodeSnippet() {
        return codeSnippet;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }
    public boolean hasCodeSnippet() {
        return codeSnippet != null && !codeSnippet.trim().isEmpty();
    }
}

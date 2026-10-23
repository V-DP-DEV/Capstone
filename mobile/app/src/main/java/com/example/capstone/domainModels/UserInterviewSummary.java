package com.example.capstone.domainModels;

import java.util.List;

/**
 * Domain model representing a summary of an available mock interview session.
 * Used primarily for browsing, selecting, and previewing interview details prior to starting.
 */
public class UserInterviewSummary {
  private String name;
  private long id;
  private String difficulty;
  private int totalQuestions;
  private List<String> questionTypes;

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public int getTotalQuestions() {
    return totalQuestions;
  }

  public List<String> getQuestionTypes() {
    return questionTypes;
  }
}


package com.example.capstone.domainModels;

import java.util.List;

public class UserInterview {
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public int getTotalQuestions() {
    return totalQuestions;
  }

  public int getDuration() {
    return duration;
  }

  public List<UserQuestion> getQuestions() {
    return questions;
  }
  public InterviewCategory getCategory() {
    return category;
  }

  private int id;
  private String name;
  private String difficulty;
  private int totalQuestions;
  private int duration;
  private List<UserQuestion> questions;
  private InterviewCategory category;
}

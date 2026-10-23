package com.example.capstone.domainModels;

/**
 * Domain model representing an interview category (e.g., Software Engineering,
 * Data Science, Product Management, etc.) used to categorize mock interview sessions.
 */

public class InterviewCategory {
  private int id;
  private String name;

  public InterviewCategory() {}

  public InterviewCategory(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
}

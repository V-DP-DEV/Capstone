package com.example.capstone.domainModels;

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

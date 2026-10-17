package com.example.capstone.domainModels;

import java.util.List;

public class UserInterview {
  private int id;
  private String name;
  private String difficulty;
  private int totalQuestions;
  private int duration;
  private List<UserQuestion> questions;
  private InterviewCategory category;

  public UserInterview() {
  }

  public UserInterview(String name, String difficulty) {
    this.name = name;
    this.difficulty = difficulty;
  }

  public UserInterview(int id, String name, String difficulty, int totalQuestions, int duration, List<UserQuestion> questions, InterviewCategory category) {
    this.id = id;
    this.name = name;
    this.difficulty = difficulty;
    this.totalQuestions = totalQuestions;
    this.duration = duration;
    this.questions = questions;
    this.category = category;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public int getTotalQuestions() {
    return totalQuestions;
  }

  public void setTotalQuestions(int totalQuestions) {
    this.totalQuestions = totalQuestions;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public List<UserQuestion> getQuestions() {
    return questions;
  }

  public void setQuestions(List<UserQuestion> questions) {
    this.questions = questions;
  }

  public InterviewCategory getCategory() {
    return category;
  }

  public void setCategory(InterviewCategory category) {
    this.category = category;
  }
}


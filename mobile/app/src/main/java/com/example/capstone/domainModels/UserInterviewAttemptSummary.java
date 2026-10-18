package com.example.capstone.domainModels;

import java.util.List;

public class UserInterviewAttemptSummary {
  private String name;
  private long id;
  private String difficulty;
  private long completedAt;

  // Stored as ints to match backend 0/1 numbers, preventing Gson crash
  private int analysed;
  private int completedInTime;
  private int reviewed;

  private String analysedStatus;
  private long totalSecondsSpent;
  private long estimatedMinutes;
  private List<String> questionTypes;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public long getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(long completedAt) {
    this.completedAt = completedAt;
  }

  // Expose as boolean for the rest of your app
  public boolean isAnalysed() {
    return analysed == 1;
  }

  public void setAnalysed(boolean analysed) {
    this.analysed = analysed ? 1 : 0;
  }

  public String getAnalysedStatus() {
    return analysedStatus;
  }

  public void setAnalysedStatus(String analysedStatus) {
    this.analysedStatus = analysedStatus;
  }

  public long getTotalSecondsSpent() {
    return totalSecondsSpent;
  }

  public void setTotalSecondsSpent(long totalSecondsSpent) {
    this.totalSecondsSpent = totalSecondsSpent;
  }

  public long getEstimatedMinutes() {
    return estimatedMinutes;
  }

  public void setEstimatedMinutes(long estimatedMinutes) {
    this.estimatedMinutes = estimatedMinutes;
  }

  // Expose as boolean for the rest of your app
  public boolean isCompletedInTime() {
    return completedInTime == 1;
  }

  public void setCompletedInTime(boolean completedInTime) {
    this.completedInTime = completedInTime ? 1 : 0;
  }

  public List<String> getQuestionTypes() {
    return questionTypes;
  }

  public void setQuestionTypes(List<String> questionTypes) {
    this.questionTypes = questionTypes;
  }

  // Expose as boolean for the rest of your app
  public boolean isReviewed() {
    return reviewed == 1;
  }

  public void setReviewed(boolean reviewed) {
    this.reviewed = reviewed ? 1 : 0;
  }
}

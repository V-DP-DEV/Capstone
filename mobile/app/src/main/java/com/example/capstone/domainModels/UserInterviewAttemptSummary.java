package com.example.capstone.domainModels;

import java.util.List;

public class UserInterviewAttemptSummary {
  private String name;
  private long id;

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

  public boolean isAnalysed() {
    return analysed;
  }

  public void setAnalysed(boolean analysed) {
    this.analysed = analysed;
  }

  public boolean isAnalysedStatus() {
    return analysedStatus;
  }

  public void setAnalysedStatus(boolean analysedStatus) {
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

  public boolean isCompletedInTime() {
    return completedInTime;
  }

  public void setCompletedInTime(boolean completedInTime) {
    this.completedInTime = completedInTime;
  }

  public List<String> getQuestionTypes() {
    return questionTypes;
  }

  public void setQuestionTypes(List<String> questionTypes) {
    this.questionTypes = questionTypes;
  }

  public boolean isReviewed() {
    return reviewed;
  }

  public void setReviewed(boolean reviewed) {
    this.reviewed = reviewed;
  }

  private String difficulty;
  private long completedAt;
  private boolean analysed;
  private boolean analysedStatus;
  private long totalSecondsSpent;
  private long estimatedMinutes;
  private boolean completedInTime;

  private boolean reviewed;
  private List<String> questionTypes;
}

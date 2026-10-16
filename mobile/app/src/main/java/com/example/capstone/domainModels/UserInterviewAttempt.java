package com.example.capstone.domainModels;

import java.util.List;

public class UserInterviewAttempt {


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

  public String  isAnalysedStatus() {
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

  public boolean isCompletedInTime() {
    return completedInTime;
  }

  public void setCompletedInTime(boolean completedInTime) {
    this.completedInTime = completedInTime;
  }

  public List<UserQuestionAttempt> getQuestions() {
    return questions;
  }

  public void setQuestions(List<UserQuestionAttempt> questions) {
    this.questions = questions;
  }

  public List<UserAttemptCompetencyFeedback> getCompetencyFeedbacks() {
    return competencyFeedbacks;
  }

  public void setCompetencyFeedbacks(List<UserAttemptCompetencyFeedback> competencyFeedbacks) {
    this.competencyFeedbacks = competencyFeedbacks;
  }
  public boolean isReviewed() {
    return reviewed;
  }

  public void setReviewed(boolean reviewed) {
    this.reviewed = reviewed;
  }
  private String name;
  private long id;
  private String difficulty;
  private long completedAt;
  private boolean analysed;
  private String analysedStatus;
  private boolean reviewed;

  private long totalSecondsSpent;
  private long estimatedMinutes;
  private boolean completedInTime;
  private List<UserQuestionAttempt> questions;
  private List<UserAttemptCompetencyFeedback> competencyFeedbacks;
}

package com.example.capstone.domainModels;

import java.util.List;

public class UserQuestionAttempt {

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getExpectedAnswer() {
    return expectedAnswer;
  }

  public void setExpectedAnswer(String expectedAnswer) {
    this.expectedAnswer = expectedAnswer;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public long getSecondsSpent() {
    return secondsSpent;
  }

  public void setSecondsSpent(long secondsSpent) {
    this.secondsSpent = secondsSpent;
  }

  public long getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(long maxScore) {
    this.maxScore = maxScore;
  }

  public long getScore() {
    return score;
  }

  public void setScore(long score) {
    this.score = score;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<UserConceptScores> getConceptScores() {
    return conceptScores;
  }

  private String id;
  private String question;
  private String answer;
  private String expectedAnswer;
  private String difficulty;
  private long secondsSpent;
  private long maxScore;
  private long score;
  private String type;

  public void setConceptScores(List<UserConceptScores> conceptScores) {
    this.conceptScores = conceptScores;
  }

  private List<UserConceptScores> conceptScores;
}

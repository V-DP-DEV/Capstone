package com.example.capstone.domainModels;

/**
 * Domain model representing user performance scores associated with a specific concept or skill.
 * Tracks individual score metrics, maximum achievable scores, and identification details.
 */
public class UserConceptScores {
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

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }

  public int getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(int maxScore) {
    this.maxScore = maxScore;
  }

  private int id;
  private String name;
  private double score;
  private int maxScore;
}

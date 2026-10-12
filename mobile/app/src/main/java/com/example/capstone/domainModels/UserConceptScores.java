package com.example.capstone.domainModels;

public class UserConceptScores {
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getConcept_id() {
    return concept_id;
  }

  public void setConcept_id(int concept_id) {
    this.concept_id = concept_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(int maxScore) {
    this.maxScore = maxScore;
  }

  private int id;
  private int concept_id;
  private String name;
  private int score;
  private int maxScore;
}

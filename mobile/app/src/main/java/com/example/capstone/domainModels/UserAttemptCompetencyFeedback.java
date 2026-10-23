package com.example.capstone.domainModels;

import java.util.List;
/**
 * Domain model representing structured competency feedback for an interview attempt.
 * Stores category-level score percentages, along with lists of identified strengths
 * and areas for improvement breakdown items.
 */
public class UserAttemptCompetencyFeedback {


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getCompetencyPercentage() {
    return competencyPercentage;
  }

  public void setCompetencyPercentage(double competencyPercentage) {
    this.competencyPercentage = competencyPercentage;
  }

  public List<UserAttemptCompetencyBreakdownFeedback> getStrengths() {
    return strengths;
  }

  public void setStrengths(List<UserAttemptCompetencyBreakdownFeedback> strengths) {
    this.strengths = strengths;
  }

  public List<UserAttemptCompetencyBreakdownFeedback> getImprovements() {
    return improvements;
  }

  public void setImprovements(List<UserAttemptCompetencyBreakdownFeedback> improvements) {
    this.improvements = improvements;
  }

  private String name;

  private double competencyPercentage;
  private List<UserAttemptCompetencyBreakdownFeedback> strengths;
  private List<UserAttemptCompetencyBreakdownFeedback> improvements;

}

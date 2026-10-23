package com.example.capstone.domainModels;
/**
 * Domain model representing granular feedback and scoring data for an individual
 * sub-metric breakdown within a competency category (e.g., specific strengths or improvement areas).
 */
public class UserAttemptCompetencyBreakdownFeedback {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public double getBreakdownPercentage() {
    return breakdownPercentage;
  }

  public void setBreakdownPercentage(double breakdownPercentage) {
    this.breakdownPercentage = breakdownPercentage;
  }
  private String feedback;
  private double breakdownPercentage;
}

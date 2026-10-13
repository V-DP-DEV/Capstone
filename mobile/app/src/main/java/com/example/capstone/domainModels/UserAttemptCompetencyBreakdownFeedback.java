package com.example.capstone.domainModels;

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

package com.example.capstone.domainModels;

/**
 * Domain model representing individual sub-metric breakdown analysis data
 * associated with a competency category (e.g., specific sub-skills or metrics).
 */
public class CompetenciesBreakdownAnalysis {
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  String name;
  double percentage;
}

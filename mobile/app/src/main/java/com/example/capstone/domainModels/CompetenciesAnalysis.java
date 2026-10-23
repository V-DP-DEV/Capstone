package com.example.capstone.domainModels;

import java.util.List;

/**
 * Domain model representing competency-level analysis data for an interview attempt.
 * Stores overall category names, score percentages, and nested sub-breakdown analytics.
 */

public class CompetenciesAnalysis {
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

  public void setCompetenciesBreakdownAnalysisList(List<CompetenciesBreakdownAnalysis> competenciesBreakdownAnalysisList) {
    this.competenciesBreakdownAnalysisList = competenciesBreakdownAnalysisList;
  }
  public List<CompetenciesBreakdownAnalysis> getCompetenciesBreakdownAnalysisList() {
    return competenciesBreakdownAnalysisList;
  }
  String name;
  double percentage;
  List<CompetenciesBreakdownAnalysis> competenciesBreakdownAnalysisList;
}

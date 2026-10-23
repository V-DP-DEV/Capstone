package com.example.capstone.domainModels;

public class ConceptAnalysis {
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTimesTested() {
    return timesTested;
  }

  public void setTimesTested(int timesTested) {
    this.timesTested = timesTested;
  }

  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  String name;
  int timesTested;
  double percentage;
}

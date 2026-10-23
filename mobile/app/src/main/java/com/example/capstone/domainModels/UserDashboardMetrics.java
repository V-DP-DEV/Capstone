package com.example.capstone.domainModels;

/**
 * Domain model representing dashboard performance metrics and summary statistics for a user,
 * including interview completion counts for the current month, current week, and difficult sessions.
 */

public class UserDashboardMetrics {

  private int totalMonth;
  private int totalWeek;
  private int totalDifficult;

  public int getTotalMonth() {
    return totalMonth;
  }

  public void setTotalMonth(int totalMonth) {
    this.totalMonth = totalMonth;
  }

  public int getTotalWeek() {
    return totalWeek;
  }

  public void setTotalWeek(int totalWeek) {
    this.totalWeek = totalWeek;
  }

  public int getTotalDifficult() {
    return totalDifficult;
  }

  public void setTotalDifficult(int totalDifficult) {
    this.totalDifficult = totalDifficult;
  }
}

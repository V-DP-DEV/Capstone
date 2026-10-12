package com.example.capstone.domainModels;

import java.util.List;

public class UserInterviewAttempt {
  private String name;
  private long id;
  private String difficulty;
  private long completedAt;
  private boolean analysed;
  private boolean analysedStatus;
  private long totalSecondsSpent;
  private long estimatedMinutes;
  private boolean completedInTime;
  private List<UserQuestionAttempt> questions;
}

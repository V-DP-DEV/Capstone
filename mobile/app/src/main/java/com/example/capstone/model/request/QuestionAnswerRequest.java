package com.example.capstone.model.request;

public class QuestionAnswerRequest {
  private int id;
  private String answer;
  private int secondsSpent;

  public QuestionAnswerRequest(int id, String answer,int secondsSpent) {
    this.id = id;
    this.answer = answer;
    this.secondsSpent = secondsSpent;
  }
}

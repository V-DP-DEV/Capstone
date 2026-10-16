package com.example.capstone.model.request;

public class QuestionAnswerRequest {
  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public int getSecondsSpent() {
    return secondsSpent;
  }

  public void setSecondsSpent(int secondsSpent) {
    this.secondsSpent = secondsSpent;
  }

  private String answer;
  private int secondsSpent;

  public QuestionAnswerRequest(int id, String answer,int secondsSpent) {
    this.id = id;
    this.answer = answer;
    this.secondsSpent = secondsSpent;
  }
}

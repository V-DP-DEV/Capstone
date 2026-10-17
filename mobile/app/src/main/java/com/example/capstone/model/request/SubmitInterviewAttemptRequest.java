package com.example.capstone.model.request;

import java.util.List;

public class SubmitInterviewAttemptRequest {
  public void setId(int id) {
    this.id = id;
  }
  public void setAnswers(List<QuestionAnswerRequest> answers) {
    this.answers = answers;
  }
  private int id;

  private List<QuestionAnswerRequest> answers;
}

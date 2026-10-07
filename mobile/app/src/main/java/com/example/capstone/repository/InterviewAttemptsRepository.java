package com.example.capstone.repository;

import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.InterviewAttemptsService;

public class InterviewAttemptsRepository implements IInterviewAttemptsRepository{
  private final InterviewAttemptsService interviewAttemptsService;
  public InterviewAttemptsRepository(InterviewAttemptsService interviewAttemptsService){
    this.interviewAttemptsService = interviewAttemptsService;
  }

  public void deleteAllInterviewAttempts(ApiCallback<Void> callback){
    interviewAttemptsService.deleteAllInterviewAttempts(callback);
  }

  @Override
  public void submitInterviewAttempt(SubmitInterviewAttemptRequest attemptRequest,ApiCallback<Void> callback) {
    interviewAttemptsService.submitInterviewAttempt(attemptRequest,callback);
  }
}

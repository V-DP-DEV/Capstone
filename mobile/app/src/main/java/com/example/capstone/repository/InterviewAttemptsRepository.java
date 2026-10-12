package com.example.capstone.repository;

import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.model.request.AnalyseRequest;
import com.example.capstone.model.request.DeleteAttemptRequest;
import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.model.response.SubmitAttemptResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.InterviewAttemptsService;

import java.util.List;

public class InterviewAttemptsRepository implements IInterviewAttemptsRepository{
  private final InterviewAttemptsService interviewAttemptsService;
  public InterviewAttemptsRepository(InterviewAttemptsService interviewAttemptsService){
    this.interviewAttemptsService = interviewAttemptsService;
  }
  public void deleteAllInterviewAttempts(ApiCallback<Void> callback){
    interviewAttemptsService.deleteAllInterviewAttempts(callback);
  }

  public void deleteInterviewAttempt(DeleteAttemptRequest deleteAttemptRequest, ApiCallback<Void> callback){
    interviewAttemptsService.deleteInterviewAttempt(deleteAttemptRequest,callback);
  }

  @Override
  public void submitInterviewAttempt(SubmitInterviewAttemptRequest attemptRequest,ApiCallback<SubmitAttemptResponse> callback) {
    interviewAttemptsService.submitInterviewAttempt(attemptRequest,callback);
  }

  @Override
  public void requestAnalysis(AnalyseRequest analyseRequest, ApiCallback<Void> callback) {
    interviewAttemptsService.requestAnalysis(analyseRequest,callback);
  }

  @Override
  public void getInterviewAttempt(int interviewAttemptId, ApiCallback<UserInterviewAttempt> callback) {
    interviewAttemptsService.getInterviewAttempt(interviewAttemptId,callback);
  }

  @Override
  public void getInterviewAttempts(String sortBy, String sortOrder, ApiCallback<List<UserInterviewAttemptSummary>> callback) {
    interviewAttemptsService.getInterviewAttempts(sortBy,sortOrder,callback);
  }
}

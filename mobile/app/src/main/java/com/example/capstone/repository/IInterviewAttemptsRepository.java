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

public interface IInterviewAttemptsRepository {
  void deleteAllInterviewAttempts( ApiCallback<Void> callback);
  void deleteInterviewAttempt(DeleteAttemptRequest deleteAttemptRequest, ApiCallback<Void> callback);
  void submitInterviewAttempt(SubmitInterviewAttemptRequest attemptRequest,ApiCallback<SubmitAttemptResponse> callback);
  void requestAnalysis(AnalyseRequest analyseRequest, ApiCallback<Void> callback);
  void getInterviewAttempt(int interviewAttemptId, ApiCallback<UserInterviewAttempt> callback);
  void getInterviewAttempts(String Sortby, String SortOrder, ApiCallback<List<UserInterviewAttemptSummary>> callback);
}

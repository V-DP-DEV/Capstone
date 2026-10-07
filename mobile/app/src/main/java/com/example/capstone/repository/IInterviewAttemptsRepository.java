package com.example.capstone.repository;

import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.InterviewAttemptsService;

public interface IInterviewAttemptsRepository {
  void deleteAllInterviewAttempts( ApiCallback<Void> callback);
  void submitInterviewAttempt(SubmitInterviewAttemptRequest attemptRequest,ApiCallback<Void> callback);
}

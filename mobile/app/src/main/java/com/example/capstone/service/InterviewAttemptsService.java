package com.example.capstone.service;

import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;

public class InterviewAttemptsService {
  private final ApiClient apiClient;

  public InterviewAttemptsService(ApiClient apiClient){
    this.apiClient = apiClient;
  }

  public void deleteAllInterviewAttempts(ApiCallback<Void> callback){

  }

  public void submitInterviewAttempt(
          SubmitInterviewAttemptRequest attemptRequest,
          ApiCallback<Void> callback
  ) {
    ApiRequest request =
            new ApiRequest("interviewAttempt/submitAttempt");

    request.setMethodPost();
    request.setRequiresAuthentication(true);
    request.setBody(attemptRequest);

    apiClient.execute(request, Void.class, callback);
  }
}

package com.example.capstone.service;

import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.model.request.AnalyseRequest;
import com.example.capstone.model.request.DeleteAttemptRequest;
import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.model.response.SubmitAttemptResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class InterviewAttemptsService {
  private final ApiClient apiClient;

  public InterviewAttemptsService(ApiClient apiClient){
    this.apiClient = apiClient;
  }

  public void deleteAllInterviewAttempts(ApiCallback<Void> callback){

  }

  public void deleteInterviewAttempt(DeleteAttemptRequest deleteAttemptRequest, ApiCallback<Void> callback){

  }

  public void submitInterviewAttempt(
          SubmitInterviewAttemptRequest attemptRequest,
          ApiCallback<SubmitAttemptResponse> callback
  ) {
    ApiRequest request =
            new ApiRequest("interviewAttempt/submitAttempt");

    request.setMethodPost();
    request.setRequiresAuthentication(true);
    request.setBody(attemptRequest);

    apiClient.execute(request, SubmitAttemptResponse.class, callback);
  }

  public void requestAnalysis(AnalyseRequest analyseRequest, ApiCallback<Void> callback){

  }
  public void getInterviewAttempt(int interviewAttemptId, ApiCallback<UserInterviewAttempt> callback) {

  }

  public void getInterviewAttempts(String Sortby, String SortOrder, ApiCallback<List<UserInterviewAttemptSummary>> callback) {
    //add specifics
    ApiRequest request = new ApiRequest("");
    Type type = new TypeToken<List<UserInterviewAttemptSummary>>() {}.getType();

    apiClient.execute(request,type,callback);
  }
}

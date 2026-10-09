package com.example.capstone.service;

import com.example.capstone.domainModels.InterviewCategory;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class InterviewService {
  private final ApiClient apiClient;
  public InterviewService(ApiClient apiClient){
    this.apiClient = apiClient;
  }

  private <T> void getInterview(
          long interviewId,
          Type responseType,
          ApiCallback<T> callback
  ) {
    ApiRequest request = new ApiRequest("interview/getInterview");
    request.setMethodGET();
    request.setRequiresAuthentication(true);
    request.addParams("id", String.valueOf(interviewId));

    apiClient.execute(request, responseType, callback);


  }

  private <T> void getInterviews(
          long categoryId,
          String name,
          String difficulty,
          Type responseType,
          ApiCallback<T> callback
  ) {
    ApiRequest request = new ApiRequest("interview/getInterviews");
    request.setMethodGET();
    request.setRequiresAuthentication(true);

    if (categoryId > 0) {
      request.addParams("categoryId", String.valueOf(categoryId));
    }

    if (name != null && !name.trim().isEmpty()) {
      request.addParams("name", name);
    }

    if (difficulty != null && !difficulty.trim().isEmpty()) {
      request.addParams("difficulty", difficulty);
    }

    apiClient.execute(request, responseType, callback);
  }

  public void getUserInterviews(
          long categoryId,
          String name,
          String difficulty,
          ApiCallback<List<UserInterviewSummary>> callback
  ) {
    Type type = new TypeToken<List<UserInterviewSummary>>() {}.getType();
    getInterviews(
            categoryId,
            name,
            difficulty,
            type,
            callback
    );
  }

  public void getCategories(ApiCallback<List<InterviewCategory>> callback) {
    ApiRequest request = new ApiRequest("interview/getAllInterviewCategories");
    request.setMethodGET();
    request.setRequiresAuthentication(false);

    Type type = new TypeToken<List<InterviewCategory>>() {}.getType();
    apiClient.execute(request, type, callback);
  }

  public void getUserInterview(long interviewId, ApiCallback<UserInterview> callback) {
    getInterview(interviewId,UserInterview.class,callback);
  }
}

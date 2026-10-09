package com.example.capstone.service;

import com.example.capstone.domainModels.InterviewCategory;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;

public class InterviewService {
  private final ApiClient apiClient;
  public InterviewService(ApiClient apiClient){
    this.apiClient = apiClient;
  }

  private <T> void getInterview(
          long interviewId,
          Class<T> responseType,
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
          Class<T> responseType,
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
          ApiCallback<UserInterviewSummary> callback
  ) {
    getInterviews(
            categoryId,
            name,
            difficulty,
            UserInterviewSummary.class,
            callback
    );
  }

  public void getCategories(ApiCallback<InterviewCategory> callback) {
    ApiRequest request = new ApiRequest("interview/getAllInterviewCategories");
    request.setMethodGET();
    request.setRequiresAuthentication(false);

    apiClient.execute(request, InterviewCategory.class, callback);
  }

  public void getUserInterview(long interviewId, ApiCallback<UserInterview> callback) {
  }
}

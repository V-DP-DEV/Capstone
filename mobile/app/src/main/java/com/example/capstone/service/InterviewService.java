package com.example.capstone.service;

import com.example.capstone.domainModels.InterviewCategory;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;

public class InterviewService {
  private final ApiClient apiClient;
  public InterviewService(ApiClient apiClient){
    this.apiClient = apiClient;
  }

  private <T> void getInterview(long interviewId, Class<T> responseType, ApiCallback<T> callback) {

  }

  private <T> void getInterviews(long categoryId,String name, Class<T> responseType, ApiCallback<T> callback) {

  }

  public void getUserInterview(long interviewId, ApiCallback<UserInterview> callback) {
    getInterview(interviewId, UserInterview.class,callback);
  }

  public void getUserInterviews(long categoryId,String name, ApiCallback<UserInterviewSummary> callback){
    getInterviews(categoryId,name, UserInterviewSummary.class,callback);
  }

  public void getCategories(ApiCallback<InterviewCategory> callback){

  }
}

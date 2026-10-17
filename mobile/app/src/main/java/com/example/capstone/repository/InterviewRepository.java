package com.example.capstone.repository;

import com.example.capstone.domainModels.InterviewCategory;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.InterviewService;

import java.util.List;

public class InterviewRepository implements IInterviewRepository{
  private final InterviewService interviewService;
  public InterviewRepository(InterviewService interviewService){
    this.interviewService =interviewService;
  }

  @Override
  public void getUserInterview(long interviewId, ApiCallback<UserInterview> callback) {
    interviewService.getUserInterview(interviewId, callback);
  }
  @Override
  public void getUserInterviews(
          long categoryId,
          String name,
          String difficulty,
          ApiCallback<List<UserInterviewSummary>> callback
  ) {
    interviewService.getUserInterviews(
            categoryId,
            name,
            difficulty,
            callback
    );
  }

  @Override
  public void getCategories(ApiCallback<List<InterviewCategory>> callback) {
    interviewService.getCategories(callback);
  }
}

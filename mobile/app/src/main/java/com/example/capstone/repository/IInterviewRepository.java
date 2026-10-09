package com.example.capstone.repository;

import com.example.capstone.domainModels.InterviewCategory;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.network.ApiCallback;

import java.util.List;

public interface IInterviewRepository {
  void getUserInterview(
      long interviewId,
      ApiCallback<UserInterview> callback
  );

  void getUserInterviews(
      long categoryId,String name,String difficulty,ApiCallback<List<UserInterviewSummary>> callback
  );

  void getCategories(
      ApiCallback<List<InterviewCategory>> callback
  );
}


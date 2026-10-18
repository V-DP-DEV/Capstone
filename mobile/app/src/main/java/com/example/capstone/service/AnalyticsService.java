package com.example.capstone.service;

import com.example.capstone.domainModels.CompetenciesAnalysis;
import com.example.capstone.domainModels.ConceptAnalysis;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AnalyticsService {
  private final ApiClient apiClient;
  public AnalyticsService(ApiClient apiClient) {
    this.apiClient=apiClient;
  }
  public void getCompetencies(int lastInterviews, String sortBy, String sortOrder, ApiCallback<List<CompetenciesAnalysis>> callback) {
    ApiRequest request = new ApiRequest("analytics/competencies");
    request.setRequiresAuthentication(true);
    request.setMethodGET();

    request.addParams("lastInterviews",String.valueOf(lastInterviews));

    if (sortBy != null && !sortBy.isEmpty()) {
      request.addParams("sortBy", sortBy);
    }

    if (sortOrder != null && !sortOrder.isEmpty()) {
      request.addParams("sortOrder", sortOrder);
    }

    Type type = new TypeToken<
        List<CompetenciesAnalysis>>() {}.getType();

    apiClient.execute(request,type,callback);
  }

  public void getConcepts(int lastInterviews, String sortBy, String sortOrder, ApiCallback<List<ConceptAnalysis>> callback) {
    ApiRequest request = new ApiRequest("analytics/concepts");
    request.setRequiresAuthentication(true);
    request.setMethodGET();

    request.addParams("lastInterviews",String.valueOf(lastInterviews));

    if (sortBy != null && !sortBy.isEmpty()) {
      request.addParams("sortBy", sortBy);
    }

    if (sortOrder != null && !sortOrder.isEmpty()) {
      request.addParams("sortOrder", sortOrder);
    }

    Type type = new TypeToken<
        List<ConceptAnalysis>>() {}.getType();
    apiClient.execute(request,type,callback);
  }
}

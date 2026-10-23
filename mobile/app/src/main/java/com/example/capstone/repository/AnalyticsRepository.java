package com.example.capstone.repository;

import com.example.capstone.domainModels.CompetenciesAnalysis;
import com.example.capstone.domainModels.ConceptAnalysis;
import com.example.capstone.domainModels.UserDashboardMetrics;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.AnalyticsService;
import com.example.capstone.service.AuthService;

import java.util.List;

public class AnalyticsRepository implements IAnalyticsRepository {
  private AnalyticsService analyticsService;
  public AnalyticsRepository(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @Override
  public void getCompetencies(int lastInterviews, String sortBy, String sortOrder, ApiCallback<List<CompetenciesAnalysis>> callback) {
    analyticsService.getCompetencies(lastInterviews,sortBy,sortOrder,callback);
  }

  @Override
  public void getConcepts(int lastInterviews, String sortBy, String sortOrder, ApiCallback<List<ConceptAnalysis>> callback) {
    analyticsService.getConcepts(lastInterviews,sortBy,sortOrder,callback);
  }

  @Override
  public void getUserDashboard(ApiCallback<UserDashboardMetrics> callback) {
    analyticsService.getUserDashboard(callback);
  }
}

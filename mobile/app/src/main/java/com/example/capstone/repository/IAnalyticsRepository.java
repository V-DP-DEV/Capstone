package com.example.capstone.repository;

import com.example.capstone.domainModels.CompetenciesAnalysis;
import com.example.capstone.domainModels.ConceptAnalysis;
import com.example.capstone.network.ApiCallback;

import java.util.List;

public interface IAnalyticsRepository {
  void getCompetencies(int lastInterviews, String sortBy, String sortOrder, ApiCallback<List<CompetenciesAnalysis>> callback);
  void getConcepts(int lastInterviews,String sortBy,String sortOrder,ApiCallback<List<ConceptAnalysis>> callback);
}

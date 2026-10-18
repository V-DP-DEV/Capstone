package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.CompetenciesAnalysis;
import com.example.capstone.domainModels.ConceptAnalysis;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IAnalyticsRepository;

import java.util.List;

public class SkillsAnalysisViewModel extends ViewModel {

    private final IAnalyticsRepository analyticsRepository;

    private final MutableLiveData<List<CompetenciesAnalysis>> competencies = new MutableLiveData<>();
    private final MutableLiveData<List<ConceptAnalysis>> concepts = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCoreSkillsSelected = new MutableLiveData<>(true);

    public SkillsAnalysisViewModel(IAnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public LiveData<List<CompetenciesAnalysis>> getCompetencies() { return competencies; }
    public LiveData<List<ConceptAnalysis>> getConcepts() { return concepts; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsCoreSkillsSelected() { return isCoreSkillsSelected; }

    public void setCoreSkillsSelected(boolean selected) {
        isCoreSkillsSelected.setValue(selected);
    }

    public void loadCompetencies(int lastInterviews, String sortBy, String sortOrder) {
        isLoading.setValue(true);
        error.setValue(null);

        analyticsRepository.getCompetencies(lastInterviews, sortBy, sortOrder, new ApiCallback<List<CompetenciesAnalysis>>() {
            @Override
            public void onSuccess(ApiResponse<List<CompetenciesAnalysis>> response) {
                isLoading.setValue(false);
                competencies.setValue(response.getData());
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage() != null ? e.getMessage() : "Failed to load competencies.");
            }

            @Override
            public void onHttpError(ApiResponse<List<CompetenciesAnalysis>> response) {
                isLoading.setValue(false);
                error.setValue(response.getError() != null ? response.getError().getMessage() : "HTTP error loading competencies.");
            }
        });
    }

    public void loadConcepts(int lastInterviews, String sortBy, String sortOrder) {
        isLoading.setValue(true);
        error.setValue(null);

        analyticsRepository.getConcepts(lastInterviews, sortBy, sortOrder, new ApiCallback<List<ConceptAnalysis>>() {
            @Override
            public void onSuccess(ApiResponse<List<ConceptAnalysis>> response) {
                isLoading.setValue(false);
                concepts.setValue(response.getData());
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage() != null ? e.getMessage() : "Failed to load concepts.");
            }

            @Override
            public void onHttpError(ApiResponse<List<ConceptAnalysis>> response) {
                isLoading.setValue(false);
                error.setValue(response.getError() != null ? response.getError().getMessage() : "HTTP error loading concepts.");
            }
        });
    }
}
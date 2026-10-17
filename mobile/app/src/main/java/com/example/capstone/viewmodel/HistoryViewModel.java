package com.example.capstone.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.model.request.DeleteAttemptRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.network.ApiClient;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.repository.InterviewAttemptsRepository;
import com.example.capstone.service.InterviewAttemptsService;
import com.example.capstone.util.SecureSession;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    private final IInterviewAttemptsRepository repository;

    private final MutableLiveData<List<UserInterviewAttemptSummary>> historyList = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();

    public enum FilterType { ALL, HIGHEST, LOWEST }
    private FilterType currentFilter = FilterType.ALL;
    private List<UserInterviewAttemptSummary> rawAttemptsList = new ArrayList<>();

    public HistoryViewModel(IInterviewAttemptsRepository repository) {
        this.repository = repository;
    }

    public static HistoryViewModel create(Context context) {
        ApiClient apiClient = new ApiClient(new SecureSession(context), new Gson());
        InterviewAttemptsService service = new InterviewAttemptsService(apiClient);
        IInterviewAttemptsRepository repository = new InterviewAttemptsRepository(service);
        return new HistoryViewModel(repository);
    }

    public LiveData<List<UserInterviewAttemptSummary>> getHistoryList() {
        return historyList;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }

    public void loadHistory() {
        isLoading.setValue(true);
        error.setValue(null);

        repository.getInterviewAttempts(null, null, new ApiCallback<List<UserInterviewAttemptSummary>>() {
            @Override
            public void onSuccess(ApiResponse<List<UserInterviewAttemptSummary>> response) {
                isLoading.setValue(false);
                if (response.getData() != null) {
                    rawAttemptsList = response.getData();
                    applyFilterAndPost(rawAttemptsList, currentFilter);
                } else {
                    rawAttemptsList = new ArrayList<>();
                    historyList.setValue(rawAttemptsList);
                }
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                // Log or display the exact exception message causing the failure
                error.setValue("Error: " + e.getMessage());
            }

            @Override
            public void onHttpError(ApiResponse<List<UserInterviewAttemptSummary>> response) {
                isLoading.setValue(false);
                if (response.getError() != null) {
                    error.setValue(response.getError().getMessage());
                } else {
                    // This is where HTTP status codes get formatted
                    error.setValue("Server error (HTTP " + response.getStatusCode() + ")");
                }
            }
        });
    }

    public void deleteAttempt(int attemptId) {
        isLoading.setValue(true);
        error.setValue(null);

        DeleteAttemptRequest deleteRequest = new DeleteAttemptRequest();


        repository.deleteInterviewAttempt(deleteRequest, new ApiCallback<Void>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                isLoading.setValue(false);
                deleteSuccess.setValue(true);
                loadHistory();
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                error.setValue("Failed to delete interview attempt.");
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                isLoading.setValue(false);
                if (response.getError() != null) {
                    error.setValue(response.getError().getMessage());
                } else {
                    error.setValue("Failed to delete attempt.");
                }
            }
        });
    }

    public void setFilter(FilterType filterType) {
        this.currentFilter = filterType;
        if (rawAttemptsList != null) {
            applyFilterAndPost(rawAttemptsList, filterType);
        }
    }

    private void applyFilterAndPost(List<UserInterviewAttemptSummary> list, FilterType filterType) {
        List<UserInterviewAttemptSummary> sortedList = new ArrayList<>(list);

        switch (filterType) {
            case HIGHEST:
                Collections.sort(sortedList, (a, b) -> Long.compare(b.getTotalSecondsSpent(), a.getTotalSecondsSpent()));
                break;
            case LOWEST:
                Collections.sort(sortedList, (a, b) -> Long.compare(a.getTotalSecondsSpent(), b.getTotalSecondsSpent()));
                break;
            case ALL:
            default:
                break;
        }

        historyList.setValue(sortedList);
    }
}
package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.model.request.DeleteAttemptRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IInterviewAttemptsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * ViewModel responsible for managing interview attempt history, sorting/filtering performance scores,
 * loading past session summaries, and handling attempt deletions in the MVVM architecture.
 */
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
                error.setValue("Error: " + e.getMessage());
            }

            @Override
            public void onHttpError(ApiResponse<List<UserInterviewAttemptSummary>> response) {
                isLoading.setValue(false);
                if (response.getError() != null) {
                    error.setValue(response.getError().getMessage());
                } else {
                    error.setValue("Server error (HTTP " + response.getStatusCode() + ")");
                }
            }
        });
    }

    public void deleteAttempt(int attemptId) {
        isLoading.setValue(true);
        error.setValue(null);

        DeleteAttemptRequest deleteRequest = new DeleteAttemptRequest();
        deleteRequest.setId(attemptId);


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
                Collections.sort(sortedList, (a, b) -> Double.compare(b.getScore(), a.getScore()));
                break;
            case LOWEST:
                Collections.sort(sortedList, (a, b) -> Double.compare(a.getScore(), b.getScore()));
                break;
            case ALL:
            default:
                break;
        }

        historyList.setValue(sortedList);
    }
}
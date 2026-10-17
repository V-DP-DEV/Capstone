package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.model.request.DeleteAttemptRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IInterviewAttemptsRepository;

import java.util.List;

/**
 * ViewModel responsible for managing interview history summaries, sorting filters,
 * detailed attempt fetching, and deletion requests via IInterviewAttemptsRepository.
 */
public class HistoryViewModel extends ViewModel {

    public enum FilterType {
        ALL,
        HIGHEST, // Sorts by completedAt DESC
        LOWEST   // Sorts by completedAt ASC
    }

    private final IInterviewAttemptsRepository repository;

    private final MutableLiveData<List<UserInterviewAttemptSummary>> historyList = new MutableLiveData<>();
    private final MutableLiveData<UserInterviewAttempt> selectedAttempt = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();

    private FilterType currentFilter = FilterType.ALL;

    public HistoryViewModel(IInterviewAttemptsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<UserInterviewAttemptSummary>> getHistoryList() {
        return historyList;
    }

    public LiveData<UserInterviewAttempt> getSelectedAttempt() {
        return selectedAttempt;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }

    public void setSelectedAttempt(UserInterviewAttempt attempt) {
        selectedAttempt.setValue(attempt);
    }

    public void loadHistory() {
        setFilter(currentFilter);
    }

    /**
     * Sets active filter and triggers backend-driven sorting.
     */
    public void setFilter(FilterType filterType) {
        this.currentFilter = filterType;

        String sortBy = "completedAt";
        String sortOrder = null;

        switch (filterType) {
            case HIGHEST:
                sortOrder = "DESC";
                break;
            case LOWEST:
                sortOrder = "ASC";
                break;
            case ALL:
            default:
                sortBy = null;
                sortOrder = null;
                break;
        }

        fetchAttempts(sortBy, sortOrder);
    }

    private void fetchAttempts(String sortBy, String sortOrder) {
        isLoading.setValue(true);
        error.setValue(null);

        repository.getInterviewAttempts(sortBy, sortOrder, new ApiCallback<List<UserInterviewAttemptSummary>>() {
            @Override
            public void onSuccess(ApiResponse<List<UserInterviewAttemptSummary>> response) {
                isLoading.setValue(false);
                if (response != null) {
                    historyList.setValue(response.getData());
                }
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage() != null ? e.getMessage() : "Failed to load history.");
            }

            @Override
            public void onHttpError(ApiResponse<List<UserInterviewAttemptSummary>> response) {
                isLoading.setValue(false);
                if (response != null && response.getError() != null) {
                    error.setValue(response.getError().getMessage());
                } else {
                    error.setValue("Failed to load interview history.");
                }
            }
        });
    }

    public void loadAttemptDetails(int attemptId) {
        isLoading.setValue(true);
        error.setValue(null);

        repository.getInterviewAttempt(attemptId, new ApiCallback<UserInterviewAttempt>() {
            @Override
            public void onSuccess(ApiResponse<UserInterviewAttempt> response) {
                isLoading.setValue(false);
                if (response != null) {
                    selectedAttempt.setValue(response.getData());
                }
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage() != null ? e.getMessage() : "Failed to load attempt details.");
            }

            @Override
            public void onHttpError(ApiResponse<UserInterviewAttempt> response) {
                isLoading.setValue(false);
                if (response != null && response.getError() != null) {
                    error.setValue(response.getError().getMessage());
                } else {
                    error.setValue("Failed to load attempt details.");
                }
            }
        });
    }

    public void deleteAttempt(int attemptId) {
        isLoading.setValue(true);
        error.setValue(null);

        DeleteAttemptRequest request = new DeleteAttemptRequest();
        request.setId(attemptId); // DeleteAttemptRequest takes an int ID[cite: 11]

        repository.deleteInterviewAttempt(request, new ApiCallback<Void>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                isLoading.setValue(false);
                deleteSuccess.setValue(true);
                loadHistory();
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage() != null ? e.getMessage() : "Failed to delete attempt.");
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                isLoading.setValue(false);
                if (response != null && response.getError() != null) {
                    error.setValue(response.getError().getMessage());
                } else {
                    error.setValue("Failed to delete attempt.");
                }
            }
        });
    }
}
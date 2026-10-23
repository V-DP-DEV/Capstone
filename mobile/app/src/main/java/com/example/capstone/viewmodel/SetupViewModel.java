package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.InterviewCategory;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IInterviewRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * ViewModel responsible for managing mock interview setup state, including loading available categories,
 * filtering and retrieving interview summaries, handling user selections, and repository communication in the MVVM architecture.
 */
public class SetupViewModel extends ViewModel {

    private final IInterviewRepository interviewRepository;

    private final MutableLiveData<List<InterviewCategory>> categories =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<List<UserInterviewSummary>> interviews =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<UserInterviewSummary> selectedInterview =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public SetupViewModel(IInterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public LiveData<List<InterviewCategory>> getCategories() {
        return categories;
    }

    public LiveData<List<UserInterviewSummary>> getInterviews() {
        return interviews;
    }

    public LiveData<UserInterviewSummary> getSelectedInterview() {
        return selectedInterview;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadCategories() {
        error.setValue(null);
        loading.setValue(true);

        interviewRepository.getCategories(
                new ApiCallback<List<InterviewCategory>>() {

                    @Override
                    public void onSuccess(ApiResponse<List<InterviewCategory>> response) {
                        loading.setValue(false);
                        if (response != null && response.getData() != null) {
                            categories.setValue(response.getData());
                        } else {
                            error.setValue("Unable to load interview categories.");
                        }
                    }

                    @Override
                    public void onGeneralError(Exception e) {
                        loading.setValue(false);
                        error.setValue("Something went wrong while loading categories.");
                    }

                    @Override
                    public void onHttpError(ApiResponse<List<InterviewCategory>> response) {
                        loading.setValue(false);
                        error.setValue(getErrorMessage(response, "Unable to load interview categories."));
                    }
                });
    }

    public void loadInterviews(long categoryId, String name, String difficulty) {
        error.setValue(null);
        loading.setValue(true);

        interviewRepository.getUserInterviews(
                categoryId,
                name,
                difficulty,
                new ApiCallback<List<UserInterviewSummary>>() {

                    @Override
                    public void onSuccess(ApiResponse<List<UserInterviewSummary>> response) {
                        loading.setValue(false);
                        if (response != null && response.getData() != null) {
                            interviews.setValue(response.getData());
                        } else {
                            interviews.setValue(new ArrayList<>());
                            error.setValue("No interview data was returned.");
                        }
                    }

                    @Override
                    public void onGeneralError(Exception e) {
                        loading.setValue(false);
                        error.setValue("Something went wrong while loading interviews.");
                    }

                    @Override
                    public void onHttpError(ApiResponse<List<UserInterviewSummary>> response) {
                        loading.setValue(false);
                        error.setValue(getErrorMessage(response, "Unable to load interviews."));
                    }
                });
    }

    public void selectInterview(UserInterviewSummary interview) {
        selectedInterview.setValue(interview);
    }

    public void clearSelection() {
        selectedInterview.setValue(null);
    }

    public void clearError() {
        error.setValue(null);
    }

    private String getErrorMessage(ApiResponse<?> response, String fallbackMessage) {
        if (response != null
                && response.getError() != null
                && response.getError().getMessage() != null
                && !response.getError().getMessage().trim().isEmpty()) {
            return response.getError().getMessage();
        }
        return fallbackMessage;
    }
}
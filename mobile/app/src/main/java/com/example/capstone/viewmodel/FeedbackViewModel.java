
package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.InterviewAttemptsRepository;
import com.example.capstone.util.Resource;

public class FeedbackViewModel extends ViewModel {

    private final InterviewAttemptsRepository repository;

    private final MutableLiveData<Resource<UserInterviewAttempt>>
            feedback = new MutableLiveData<>();

    public FeedbackViewModel(InterviewAttemptsRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<UserInterviewAttempt>> getFeedback() {
        return feedback;
    }

    public void loadFeedback(int interviewAttemptId) {

        feedback.setValue(Resource.loading());

        repository.getInterviewAttempt(
                interviewAttemptId,
                new ApiCallback<UserInterviewAttempt>() {

                    @Override
                    public void onSuccess(
                            ApiResponse<UserInterviewAttempt> response) {

                        feedback.postValue(
                                Resource.success(response.getData())
                        );
                    }

                    @Override
                    public void onGeneralError(Exception e) {

                        String message = e.getMessage() != null
                                ? e.getMessage()
                                : "Unable to load feedback";

                        feedback.postValue(Resource.error(message));
                    }

                    @Override
                    public void onHttpError(
                            ApiResponse<UserInterviewAttempt> response) {

                        String message = "Failed to load feedback";

                        if (response.getError() != null
                                && response.getError().getMessage() != null) {
                            message = response.getError().getMessage();
                        }

                        feedback.postValue(Resource.error(message));
                    }
                }
        );
    }
}

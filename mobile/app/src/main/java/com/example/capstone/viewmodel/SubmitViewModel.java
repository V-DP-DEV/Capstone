
package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.model.request.QuestionAnswerRequest;
import com.example.capstone.model.response.SubmitAttemptResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IInterviewAttemptsRepository;

import java.util.List;

public class SubmitViewModel extends ViewModel {

    private final IInterviewAttemptsRepository attemptsRepository;

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> submitSuccess =
            new MutableLiveData<>(false);

    private final MutableLiveData<Integer> interviewAttemptId =
            new MutableLiveData<>();

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public SubmitViewModel(
            IInterviewAttemptsRepository attemptsRepository) {
        this.attemptsRepository = attemptsRepository;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getSubmitSuccess() {
        return submitSuccess;
    }

    public LiveData<Integer> getInterviewAttemptId() {
        return interviewAttemptId;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void submitInterview(
            int interviewId,
            List<QuestionAnswerRequest> answers) {

        error.setValue(null);
        submitSuccess.setValue(false);
        interviewAttemptId.setValue(null);

        if (interviewId <= 0) {
            error.setValue("A valid interview must be selected.");
            return;
        }

        if (answers == null || answers.isEmpty()) {
            error.setValue("There are no answers to submit.");
            return;
        }

        SubmitInterviewAttemptRequest request =
                new SubmitInterviewAttemptRequest();

        request.setId(interviewId);
        request.setAnswers(answers);

        loading.setValue(true);

        attemptsRepository.submitInterviewAttempt(
                request,
                new ApiCallback<SubmitAttemptResponse>() {

                    @Override
                    public void onSuccess(
                            ApiResponse<SubmitAttemptResponse> response) {
                        loading.postValue(false);

                        SubmitAttemptResponse data =
                                response == null ? null : response.getData();

                        if (data == null || data.getInterviewAttemptId() <= 0) {
                            error.postValue(
                                    "The submission response did not contain a valid attempt ID.");
                            return;
                        }

                        interviewAttemptId.postValue(
                                data.getInterviewAttemptId());
                        submitSuccess.postValue(true);
                    }

                    @Override
                    public void onGeneralError(Exception e) {
                        loading.postValue(false);
                        error.postValue(
                                "Something went wrong while submitting your interview.");
                    }

                    @Override
                    public void onHttpError(
                            ApiResponse<SubmitAttemptResponse> response) {
                        loading.postValue(false);

                        String message = "Interview submission failed.";

                        if (response != null
                                && response.getError() != null
                                && response.getError().getMessage() != null
                                && !response.getError().getMessage()
                                .trim().isEmpty()) {
                            message = response.getError().getMessage();
                        }

                        error.postValue(message);
                    }
                });
    }

    public void clearError() {
        error.setValue(null);
    }
}
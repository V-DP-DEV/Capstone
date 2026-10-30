package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserQuestion;
import com.example.capstone.model.request.QuestionAnswerRequest;
import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.model.response.SubmitAttemptResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.repository.IInterviewRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel responsible for managing interview session state, question navigation,
 * real-time user response caching, and final submission logic in the MVVM architecture.
 */
public class AnswerViewModel extends ViewModel {

    private final IInterviewRepository interviewRepository;
    private final IInterviewAttemptsRepository interviewAttemptsRepository;
    private final MutableLiveData<UserInterview> currentInterview =
        new MutableLiveData<>();

    private final MutableLiveData<Integer> currentQuestionIndex =
        new MutableLiveData<>(0);

    private final MutableLiveData<String> currentAnswer =
        new MutableLiveData<>("");

    private final MutableLiveData<String> error =
        new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading =
        new MutableLiveData<>(false);

    private final Map<Integer, QuestionAnswerRequest> answers =
        new LinkedHashMap<>();
    private long attemptId = 0;
    public long getAttemptId(){
        return attemptId;
    }
    public MutableLiveData<Boolean> getIsSubmitted() {
        return isSubmitted;
    }

    private final MutableLiveData<Boolean> isSubmitted=
        new MutableLiveData<>(false);

    public AnswerViewModel(IInterviewRepository interviewRepository, IInterviewAttemptsRepository interviewAttemptsRepository) {
        this.interviewRepository = interviewRepository;
        this.interviewAttemptsRepository = interviewAttemptsRepository;
    }

    // Observable state

    public LiveData<UserInterview> getCurrentInterview() {
        return currentInterview;
    }
    public Map<Integer, QuestionAnswerRequest> getAnswers(){return answers;}

    public LiveData<Integer> getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public LiveData<String> getCurrentAnswer() {
        return currentAnswer;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Interview

    public void loadInterview(int id) {
        isLoading.setValue(true);
        error.setValue(null);

        interviewRepository.getUserInterview(
            id,
            new ApiCallback<UserInterview>() {
                @Override
                public void onSuccess(
                    ApiResponse<UserInterview> response) {
                    isLoading.setValue(false);
                    UserInterview interview = response.getData();

                    if (interview == null) {
                        error.setValue("Interview not found.");
                        isLoading.setValue(false);
                        return;
                    }

                    currentInterview.setValue(interview);
                    currentQuestionIndex.setValue(0);
                    currentAnswer.setValue("");
                    answers.clear();

                }

                @Override
                public void onGeneralError(Exception e) {
                    isLoading.setValue(false);
                    error.setValue(
                        e.getMessage() != null
                            ? e.getMessage()
                            : "Failed to load interview.");
                }

                @Override
                public void onHttpError(
                    ApiResponse<UserInterview> response) {
                    error.setValue(response.getError().getMessage());
                }
            });
    }

    public UserQuestion getCurrentQuestion() {
        List<UserQuestion> questions = getQuestions();
        Integer index = currentQuestionIndex.getValue();

        if (index == null || index < 0 || index >= questions.size()) {
            return null;
        }

        return questions.get(index);
    }

    public List<UserQuestion> getQuestions() {
        UserInterview interview = currentInterview.getValue();

        if (interview == null || interview.getQuestions() == null) {
            return new ArrayList<>();
        }

        return interview.getQuestions();
    }

    // Navigation

    public boolean moveToNextQuestion(String answer, int seconds) {
        Integer index = currentQuestionIndex.getValue();

        if (index == null) {
            return false;
        }

        return moveToQuestion(index + 1, answer, seconds);
    }

    public boolean moveToPreviousQuestion(String answer, int seconds) {
        Integer index = currentQuestionIndex.getValue();

        if (index == null) {
            return false;
        }

        return moveToQuestion(index - 1, answer, seconds);
    }

    private boolean moveToQuestion(
        int targetIndex,
        String answer,
        int seconds) {

        List<UserQuestion> questions = getQuestions();

        if (targetIndex < 0 || targetIndex >= questions.size()) {
            return false;
        }

        if (!saveCurrentResponse(answer, seconds)) {
            return false;
        }

        currentQuestionIndex.setValue(targetIndex);

        Integer questionId =
            parseQuestionId(questions.get(targetIndex).getId());

        QuestionAnswerRequest saved = questionId == null
            ? null
            : answers.get(questionId);

        currentAnswer.setValue(
            saved == null ? "" : saved.getAnswer());

        error.setValue(null);
        return true;
    }

    // Save the answer and time for the question being left

    public boolean saveCurrentResponse(String answer, int seconds) {
        UserQuestion question = getCurrentQuestion();

        if (question == null) {
            error.setValue("No current question is available.");
            return false;
        }

        Integer questionId = parseQuestionId(question.getId());

        if (questionId == null) {
            error.setValue("Invalid question ID.");
            return false;
        }

        QuestionAnswerRequest previous = answers.get(questionId);

        int previousSeconds = previous == null
            ? 0
            : previous.getSecondsSpent();

        QuestionAnswerRequest response = new QuestionAnswerRequest(
            questionId,
            answer == null ? "" : answer,
            previousSeconds + Math.max(0, seconds)
        );

        response.setId(questionId);

        answers.put(questionId, response);

        error.setValue(null);
        return true;
    }

    // Submission

    private Integer parseQuestionId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(id.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getSavedAnswerForQuestion(UserQuestion question) {
        if (question == null) return "";
        Integer questionId = parseQuestionId(question.getId());
        if (questionId == null) return "";
        QuestionAnswerRequest request = answers.get(questionId);
        return request != null ? request.getAnswer() : "";
    }

    public void submitInterview() {
        SubmitInterviewAttemptRequest request =
            buildSubmissionRequest();
        if (request == null) {
            return;
        }

        isLoading.setValue(true);
        error.setValue(null);

        interviewAttemptsRepository.submitInterviewAttempt(request, new ApiCallback<SubmitAttemptResponse>() {
            @Override
            public void onSuccess(ApiResponse<SubmitAttemptResponse> response) {
                isSubmitted.setValue(true);
                isLoading.setValue(false);
                attemptId = response.getData().getInterviewAttemptId();
            }

            @Override
            public void onGeneralError(Exception e) {
                error.setValue("Failed to submit attempt");
                isLoading.setValue(false);
            }

            @Override
            public void onHttpError(ApiResponse<SubmitAttemptResponse> response) {
                error.setValue(response.getError().getMessage());
                isLoading.setValue(false);
            }
        });

        // Call your repository's submission method here.
    }


    public SubmitInterviewAttemptRequest buildSubmissionRequest() {
        UserInterview interview = getCurrentInterview().getValue();
        if (interview == null) {
            error.setValue("Interview session data is missing.");
            return null;
        }
        int interviewId = interview.getId();
        List<QuestionAnswerRequest> submissionAnswers =
            new ArrayList<>();

        for (UserQuestion question : getQuestions()) {
            Integer questionId = parseQuestionId(question.getId());

            if (questionId == null) {
                error.setValue("A question has an invalid ID.");
                return null;
            }

            QuestionAnswerRequest response = answers.get(questionId);

            if (response == null) {
                response = new QuestionAnswerRequest(
                    questionId, "", 0);
            }

            submissionAnswers.add(response);
        }

        SubmitInterviewAttemptRequest request =
            new SubmitInterviewAttemptRequest();

        request.setId(interviewId);
        request.setAnswers(submissionAnswers);

        error.setValue(null);
        return request;
    }

    public void clearViewModel(){
        currentAnswer.setValue("");
        currentInterview.setValue(null);
        currentQuestionIndex.setValue(0);
        error.setValue("");
        isLoading.setValue(false);
        isSubmitted.setValue(false);
        answers.clear();;
    }
}
package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.domainModels.UserQuestion;
import com.example.capstone.domainModels.UserQuestionAttempt;
import com.example.capstone.model.request.AnalyseRequest;
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

public class ReviewViewModel extends ViewModel {
  private final IInterviewAttemptsRepository interviewAttemptsRepository;
  private final MutableLiveData<UserInterviewAttempt> currentInterviewAttempt =
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

  public MutableLiveData<Boolean> getIsAnalysing() {
    return isAnalysing;
  }

  private final MutableLiveData<Boolean> isAnalysing=
      new MutableLiveData<>(false);
  private final MutableLiveData<String> analysisError =
      new MutableLiveData<>("");
  private final MutableLiveData<Boolean> analysed =
      new MutableLiveData<>(false);


  public ReviewViewModel(IInterviewAttemptsRepository interviewAttemptsRepository) {
    this.interviewAttemptsRepository = interviewAttemptsRepository;
  }

  public MutableLiveData<String> getAnalysisError() {return analysisError;}

  public MutableLiveData<Boolean> getAnalysed(){return analysed;}
  // Observable state

  public LiveData<UserInterviewAttempt> getCurrentInterviewAttempt() {
    return currentInterviewAttempt;
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

  public void loadInterview(long id) {
    isLoading.setValue(true);
    error.setValue(null);
    System.out.println(id);

    interviewAttemptsRepository.getInterviewAttempt(
        (int)id,
        new ApiCallback<UserInterviewAttempt>() {
          @Override
          public void onSuccess(
              ApiResponse<UserInterviewAttempt> response) {
            isLoading.setValue(false);
            UserInterviewAttempt interview = response.getData();

            if (interview == null) {
              error.setValue("Interview not found.");
              isLoading.setValue(false);
              return;
            }

            currentInterviewAttempt.setValue(interview);
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
              ApiResponse<UserInterviewAttempt> response) {
            error.setValue(response.getError().getMessage());
          }
        });
  }

  public UserQuestionAttempt getCurrentQuestion() {
    List<UserQuestionAttempt> questions = getQuestions();
    Integer index = currentQuestionIndex.getValue();

    if (index == null || index < 0 || index >= questions.size()) {
      return null;
    }

    return questions.get(index);
  }

  public List<UserQuestionAttempt> getQuestions() {
    UserInterviewAttempt interview = currentInterviewAttempt.getValue();

    if (interview == null || interview.getQuestions() == null) {
      return new ArrayList<>();
    }

    return interview.getQuestions();
  }

  // Navigation

  public boolean moveToNextQuestion() {
    Integer index = currentQuestionIndex.getValue();

    if (index == null) {
      return false;
    }

    return moveToQuestion(index + 1);
  }

  public boolean moveToPreviousQuestion() {
    Integer index = currentQuestionIndex.getValue();

    if (index == null) {
      return false;
    }

    return moveToQuestion(index - 1);
  }

  public boolean moveToQuestion(
      int targetIndex) {

    List<UserQuestionAttempt> questions = getQuestions();

    if (targetIndex < 0 || targetIndex >= questions.size()) {
      return false;
    }

    currentQuestionIndex.setValue(targetIndex);
    System.out.println(targetIndex);
    error.setValue(null);
    return true;
  }

  public void analyseAttempt() {
    AnalyseRequest request = new AnalyseRequest();
    request.setId((int)currentInterviewAttempt.getValue().getId());
    isAnalysing.setValue(true);
    error.setValue(null);

    interviewAttemptsRepository.requestAnalysis(request, new ApiCallback<Void>() {
      @Override
      public void onSuccess(ApiResponse<Void> response) {
        isAnalysing.setValue(false);
        analysed.setValue(true);
      }

      @Override
      public void onGeneralError(Exception e) {
        analysisError.setValue("Unexpected error occured");
        isAnalysing.setValue(false);
      }

      @Override
      public void onHttpError(ApiResponse<Void> response) {
        analysisError.setValue(response.getError().getMessage());
        isAnalysing.setValue(false);
      }
    });

    // Call your repository's submission method here.
  }

  public void clearViewModel(){
    currentAnswer.setValue("");
    currentInterviewAttempt.setValue(null);
    currentQuestionIndex.setValue(0);
    error.setValue("");
    isLoading.setValue(false);
    isAnalysing.setValue(false);
    analysed.setValue(false);
    answers.clear();;
  }
}

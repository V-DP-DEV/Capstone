
package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserQuestion;
import com.example.capstone.model.request.QuestionAnswerRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnswerViewModel extends ViewModel {

    private UserInterview currentInterview;

    private final Map<Integer, String> answers = new LinkedHashMap<>();
    private final Map<Integer, Integer> secondsSpent = new LinkedHashMap<>();

    private final MutableLiveData<Integer> currentQuestionIndex =
            new MutableLiveData<>(0);

    private final MutableLiveData<String> currentAnswer =
            new MutableLiveData<>("");

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public LiveData<Integer> getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public LiveData<String> getCurrentAnswer() {
        return currentAnswer;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void setInterview(UserInterview interview) {
        currentInterview = interview;
        answers.clear();
        secondsSpent.clear();
        currentQuestionIndex.setValue(0);
        currentAnswer.setValue("");
        error.setValue(null);
    }

    public UserInterview getCurrentInterview() {
        return currentInterview;
    }

    public List<UserQuestion> getQuestions() {
        if (currentInterview == null
                || currentInterview.getQuestions() == null) {
            return new ArrayList<>();
        }

        return currentInterview.getQuestions();
    }

    public void setAnswer(String answer) {
        currentAnswer.setValue(answer == null ? "" : answer);
        saveCurrentAnswer(answer);
    }

    public String getAnswerForQuestion(int questionIndex) {
        List<UserQuestion> questions = getQuestions();

        if (questionIndex < 0 || questionIndex >= questions.size()) {
            return "";
        }

        Integer questionId = parseQuestionId(
                questions.get(questionIndex).getId());

        if (questionId == null) {
            return "";
        }

        String savedAnswer = answers.get(questionId);
        return savedAnswer == null ? "" : savedAnswer;
    }

    public void saveCurrentAnswer(String answer) {
        UserQuestion question = getCurrentQuestion();

        if (question == null) {
            error.setValue("No current question is available.");
            return;
        }

        Integer questionId = parseQuestionId(question.getId());

        if (questionId == null) {
            error.setValue(
                    "The question has an invalid ID. The answer cannot be saved.");
            return;
        }

        answers.put(questionId, answer == null ? "" : answer);
        error.setValue(null);
    }

    public void recordTimeSpent(int questionIndex, int seconds) {
        List<UserQuestion> questions = getQuestions();

        if (questionIndex < 0 || questionIndex >= questions.size()) {
            error.setValue("Invalid question index.");
            return;
        }

        Integer questionId = parseQuestionId(
                questions.get(questionIndex).getId());

        if (questionId == null) {
            error.setValue(
                    "The question has an invalid ID. Time cannot be recorded.");
            return;
        }

        secondsSpent.put(questionId, Math.max(0, seconds));
        error.setValue(null);
    }

    public boolean moveToQuestion(int questionIndex) {
        List<UserQuestion> questions = getQuestions();

        if (questionIndex < 0 || questionIndex >= questions.size()) {
            error.setValue("That question does not exist.");
            return false;
        }

        saveCurrentAnswer(currentAnswer.getValue());
        currentQuestionIndex.setValue(questionIndex);
        currentAnswer.setValue(getAnswerForQuestion(questionIndex));
        error.setValue(null);
        return true;
    }

    public boolean moveToNextQuestion() {
        Integer index = currentQuestionIndex.getValue();

        if (index == null) {
            index = 0;
        }

        return moveToQuestion(index + 1);
    }

    public boolean moveToPreviousQuestion() {
        Integer index = currentQuestionIndex.getValue();

        if (index == null) {
            index = 0;
        }

        return moveToQuestion(index - 1);
    }

    public UserQuestion getCurrentQuestion() {
        Integer index = currentQuestionIndex.getValue();

        if (index == null) {
            return null;
        }

        List<UserQuestion> questions = getQuestions();

        if (index < 0 || index >= questions.size()) {
            return null;
        }

        return questions.get(index);
    }

    public List<QuestionAnswerRequest> buildSubmissionAnswers() {
        saveCurrentAnswer(currentAnswer.getValue());

        List<QuestionAnswerRequest> result = new ArrayList<>();

        for (UserQuestion question : getQuestions()) {
            Integer questionId = parseQuestionId(question.getId());

            if (questionId == null) {
                error.setValue(
                        "A question has an invalid ID. Please check the interview data.");
                return new ArrayList<>();
            }

            String answer = answers.get(questionId);
            Integer time = secondsSpent.get(questionId);

            result.add(new QuestionAnswerRequest(
                    questionId,
                    answer == null ? "" : answer,
                    time == null ? 0 : time
            ));
        }

        error.setValue(null);
        return result;
    }

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
}
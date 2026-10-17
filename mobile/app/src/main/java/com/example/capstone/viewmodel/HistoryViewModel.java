package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserQuestionAttempt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    public enum FilterType {
        ALL,
        HIGHEST,
        LOWEST
    }

    private final MutableLiveData<List<UserInterviewAttempt>> historyList = new MutableLiveData<>();
    private final MutableLiveData<UserInterviewAttempt> selectedAttempt = new MutableLiveData<>();
    private final List<UserInterviewAttempt> rawAttemptsList = new ArrayList<>();
    private FilterType currentFilter = FilterType.ALL;

    public LiveData<List<UserInterviewAttempt>> getHistoryList() {
        return historyList;
    }

    public void setSelectedAttempt(UserInterviewAttempt attempt) {
        selectedAttempt.setValue(attempt);
    }

    public LiveData<UserInterviewAttempt> getSelectedAttempt() {
        return selectedAttempt;
    }

    public void loadHistory() {
        // TODO: Replace with repository call when available
        rawAttemptsList.clear();
        rawAttemptsList.addAll(getSampleAttempts());
        applyFilter(currentFilter);
    }

    public void setFilter(FilterType filterType) {
        this.currentFilter = filterType;
        applyFilter(filterType);
    }

    private void applyFilter(FilterType filterType) {
        List<UserInterviewAttempt> sortedList = new ArrayList<>(rawAttemptsList);

        switch (filterType) {
            case HIGHEST:
                Collections.sort(sortedList, (a1, a2) ->
                        Double.compare(calculateScorePercentage(a2), calculateScorePercentage(a1)));
                break;

            case LOWEST:
                Collections.sort(sortedList, (a1, a2) ->
                        Double.compare(calculateScorePercentage(a1), calculateScorePercentage(a2)));
                break;

            case ALL:
            default:
                break;
        }

        historyList.setValue(sortedList);
    }

    private double calculateScorePercentage(UserInterviewAttempt attempt) {
        double totalEarnedScore = 0;
        double totalMaxScore = 0;

        if (attempt.getQuestions() != null && !attempt.getQuestions().isEmpty()) {
            for (UserQuestionAttempt qa : attempt.getQuestions()) {
                totalEarnedScore += qa.getScore();
                totalMaxScore += qa.getMaxScore();
            }
        }

        return totalMaxScore > 0 ? (totalEarnedScore / totalMaxScore) * 100.0 : 0.0;
    }

    private List<UserInterviewAttempt> getSampleAttempts() {
        List<UserInterviewAttempt> list = new ArrayList<>();

        UserInterviewAttempt attempt1 = new UserInterviewAttempt();
        attempt1.setId(1L);
        attempt1.setName("C# Developer");
        attempt1.setDifficulty("Hard");
        attempt1.setCompletedInTime(true);
        attempt1.setCompletedAt(System.currentTimeMillis());

        list.add(attempt1);
        return list;
    }
}